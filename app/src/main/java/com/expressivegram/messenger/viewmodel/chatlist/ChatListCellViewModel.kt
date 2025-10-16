package com.expressivegram.messenger.viewmodel.chatlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.expressivegram.messenger.extensions.execute
import com.expressivegram.messenger.extensions.getChatTitle
import com.expressivegram.messenger.extensions.getForumTopicId
import com.expressivegram.messenger.extensions.getLastMessageText
import com.expressivegram.messenger.extensions.isForum
import com.expressivegram.messenger.utils.TdUtility
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi

class ChatListCellViewModel(chat: TdApi.Chat) : ViewModel() {
    private val _lastMessageText = mutableStateOf("")
    val lastMessageText: State<String> = _lastMessageText

    private val _lastForumTopic = mutableStateOf<TdApi.ForumTopic?>(null)
    val lastForumTopic: State<TdApi.ForumTopic?> = _lastForumTopic

    private val _title = mutableStateOf("")
    val title: State<String> = _title

    private val _unreadMessagesCount = mutableIntStateOf(0)
    val unreadMessagesCount: State<Int> = _unreadMessagesCount


    init {
        val instance = TdUtility.getInstance()

        viewModelScope.launch {
            _title.value = chat.getChatTitle()
            _lastMessageText.value = chat.getLastMessageText()
            _unreadMessagesCount.intValue = chat.unreadCount

            if (chat.isForum()) {
                _lastForumTopic.value = instance
                    .getClient()
                    .execute(
                        TdApi.GetForumTopic(
                            chat.id,
                            chat.lastMessage?.getForumTopicId() ?: 0
                        )
                    )
            }
        }

        instance.updates
            .filterIsInstance<TdApi.UpdateChatLastMessage>()
            .onEach {
                if (it.chatId != chat.id) {
                    return@onEach
                }

                _lastMessageText.value = it.lastMessage?.getLastMessageText() ?: "❓"

                if (chat.isForum()) {
                    _lastForumTopic.value = instance
                        .getClient()
                        .execute(
                            TdApi.GetForumTopic(
                                it.chatId,
                                it.lastMessage?.getForumTopicId() ?: 0
                            )
                        )
                }
            }
            .launchIn(viewModelScope)

        instance.updates
            .filterIsInstance<TdApi.UpdateChatTitle>()
            .onEach {
                if (it.chatId != chat.id) {
                    return@onEach
                }

                _title.value = it.title
            }
            .launchIn(viewModelScope)

        instance.updates
            .filterIsInstance<TdApi.UpdateChatReadInbox>()
            .onEach {
                if (it.chatId != chat.id) {
                    return@onEach
                }

                _unreadMessagesCount.intValue = it.unreadCount
            }
            .launchIn(viewModelScope)
    }
}

class ChatListCellViewModelFactory(private val chat: TdApi.Chat) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatListCellViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatListCellViewModel(chat) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}