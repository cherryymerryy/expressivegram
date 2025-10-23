package com.expressivegram.messenger.viewmodel.chatlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expressivegram.messenger.data.ChatListItemState
import com.expressivegram.messenger.data.TdLibException
import com.expressivegram.messenger.extensions.execute
import com.expressivegram.messenger.extensions.getChatTitle
import com.expressivegram.messenger.extensions.getForumTopicId
import com.expressivegram.messenger.extensions.getLastMessageText
import com.expressivegram.messenger.extensions.isForum
import com.expressivegram.messenger.utils.Log
import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.utils.UserConfig
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.supervisorScope
import org.drinkless.tdlib.TdApi

class ChatListViewModel : ViewModel() {
    private val _isFoldersLoading = mutableStateOf(true)
    val isFoldersLoading: State<Boolean> = _isFoldersLoading

    private val _chats = MutableStateFlow<List<TdApi.Chat>>(emptyList())
    val chats: StateFlow<List<TdApi.Chat>> = _chats.asStateFlow()

    private val _folders = mutableStateOf<List<TdApi.ChatFolderInfo>>(emptyList())
    val folders: State<List<TdApi.ChatFolderInfo>> = _folders

    private val _pinnedChatsIds = mutableListOf<Long>()

    private val _chatItems = MutableStateFlow<List<ChatListItemState>>(emptyList())
    val chatItems: StateFlow<List<ChatListItemState>> = _chatItems.asStateFlow()

    init {
        val instance = TdUtility.getInstance()

        instance.updates
            .filterIsInstance<TdApi.UpdateNewMessage>()
            .onEach { update ->
                val newElementIndex = _pinnedChatsIds.lastIndex + 1
                val isPinned = _pinnedChatsIds.contains(update.message.chatId)

                _chatItems.update { currentList ->
                    val newList = currentList.toMutableList()
                    val localChat = newList.firstOrNull { it.chatId == update.message.chatId }

                    if (localChat == null) {
                        return@onEach
                    }

                    if (newList.indexOf(localChat) != newElementIndex && !isPinned) {
                        newList.remove(localChat)
                        newList.add(
                            newElementIndex,
                            localChat
                        )
                    }

                    newList
                }
            }
            .launchIn(viewModelScope)

        instance.updates
            .filterIsInstance<TdApi.UpdateChatLastMessage>()
            .onEach { update ->
                _chatItems.update { currentList ->
                    val index = currentList.indexOfFirst { it.chatId == update.chatId }
                    if (index == -1) {
                        return@update currentList
                    }

                    val oldItem = currentList[index]
                    val newItem = oldItem.copy(
                        lastMessageText = update.lastMessage?.getLastMessageText() ?: "❓",
                        lastForumTopicName = getLastTopicName(
                            oldItem,
                            update.lastMessage
                        )
                    )

                    currentList.toMutableList().apply { this[index] = newItem }
                }
            }
            .launchIn(viewModelScope)

        instance.updates
            .filterIsInstance<TdApi.UpdateChatTitle>()
            .onEach { update ->
                _chatItems.update { currentList ->
                    val index = currentList.indexOfFirst { it.chatId == update.chatId }
                    if (index == -1) return@update currentList

                    val oldItem = currentList[index]
                    val newItem = oldItem.copy(title = update.title)

                    currentList.toMutableList().apply { this[index] = newItem }
                }
            }
            .launchIn(viewModelScope)

        instance.updates
            .filterIsInstance<TdApi.UpdateChatReadInbox>()
            .onEach { update ->
                _chatItems.update { currentList ->
                    val index = currentList.indexOfFirst { it.chatId == update.chatId }
                    if (index == -1) return@update currentList

                    val oldItem = currentList[index]
                    val newItem = oldItem.copy(unreadCount = update.unreadCount)

                    currentList.toMutableList().apply { this[index] = newItem }
                }
            }
            .launchIn(viewModelScope)

        instance.updates
            .filterIsInstance<TdApi.UpdateChatFolders>()
            .onEach { update ->
                _isFoldersLoading.value = true

                if (update.chatFolders == null) {
                    _isFoldersLoading.value = false
                    return@onEach
                }

                _folders.value = update.chatFolders.asList()
                UserConfig.getInstance().setFolders(_folders.value)
                _isFoldersLoading.value = false
            }
            .launchIn(viewModelScope)
    }

    suspend fun getLastTopicName(itemState: ChatListItemState, lastMessage: TdApi.Message?) : String? {
        if (lastMessage == null) {
            return null
        }

        return if (itemState.isForum) {
            try {
                val topicId = lastMessage.getForumTopicId()
                if (topicId != 0L) {
                    TdUtility
                        .getInstance()
                        .getClient()
                        .execute(
                            TdApi.GetForumTopic(
                                itemState.chatId,
                                topicId)
                        ).info.name
                } else {
                    null
                }
            } catch (e: TdLibException) {
                Log.e(e, "Could not get forum topic for chat ${itemState.chatId}")
                "Тема не найдена"
            }
        } else {
            null
        }
    }

    suspend fun loadChats(chatList: TdApi.ChatList) {
        val client = TdUtility.getInstance().getClient()

        val chatsIds = try {
            client.execute(TdApi.GetChats(chatList, 100)).chatIds
        } catch (e: TdLibException) {
            Log.e(e, "Failed to get chat IDs")
            _chatItems.value = emptyList()
            return
        }

        val chatDataPairs = supervisorScope {
            chatsIds.map { id ->
                async {
                    try {
                        val defaultTopicName = "General"
                        val chat = client.execute(TdApi.GetChat(id))

                        val topicName = if (chat.isForum()) {
                            try {
                                val topicId = chat.lastMessage?.getForumTopicId() ?: 0
                                if (topicId != 0L) {
                                    client.execute(TdApi.GetForumTopic(chat.id, topicId)).info.name
                                } else {
                                    defaultTopicName
                                }
                            } catch (e: TdLibException) {
                                Log.e(e, "Could not get forum topic for chat ${chat.id}")
                                defaultTopicName
                            }
                        } else {
                            defaultTopicName
                        }

                        Pair(chat, ChatListItemState(
                            chatId = chat.id,
                            photo = chat.photo?.small,
                            title = chat.getChatTitle(),
                            lastMessageText = chat.getLastMessageText(),
                            unreadCount = chat.unreadCount,
                            isForum = chat.isForum(),
                            lastForumTopicName = topicName
                        ))
                    } catch (e: TdLibException) {
                        Log.e(e, "Failed to load chat $id")
                        null
                    }
                }
            }.mapNotNull { it.await() }
        }

        _pinnedChatsIds.clear()
        val sortedChatItems = mutableListOf<ChatListItemState>()

        chatDataPairs.forEach { (chat, state) ->
            val position = chat.positions.firstOrNull { it.list.constructor == chatList.constructor }
            if (position?.isPinned == true) {
                _pinnedChatsIds.add(chat.id)
                sortedChatItems.add(state)
            }
        }

        chatDataPairs.forEach { (chat, state) ->
            if (!_pinnedChatsIds.contains(chat.id)) {
                sortedChatItems.add(state)
            }
        }

        _chatItems.value = sortedChatItems
    }
}