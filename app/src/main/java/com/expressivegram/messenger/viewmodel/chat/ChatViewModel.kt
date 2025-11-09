package com.expressivegram.messenger.viewmodel.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.expressivegram.messenger.data.chat.ChatActionType
import com.expressivegram.messenger.data.chat.ChatState
import com.expressivegram.messenger.data.chat.ChatType
import com.expressivegram.messenger.data.message.TdMessageState
import com.expressivegram.messenger.extensions.execute
import com.expressivegram.messenger.extensions.getSenderId
import com.expressivegram.messenger.extensions.isGroup
import com.expressivegram.messenger.extensions.isPrivate
import com.expressivegram.messenger.extensions.toChatAction
import com.expressivegram.messenger.extensions.toChatState
import com.expressivegram.messenger.extensions.toTdMessage
import com.expressivegram.messenger.presentation.components.preferences.ListItemPosition
import com.expressivegram.messenger.utils.Log
import com.expressivegram.messenger.utils.TdUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi

class ChatViewModel(private val chatId: Long) : ViewModel() {
    private val _chat = mutableStateOf<TdApi.Chat?>(null)

    private val _chatState = MutableStateFlow<ChatState?>(null)
    val chatState = _chatState.asStateFlow()

    private val _messages = MutableStateFlow<MutableList<TdMessageState>>(mutableListOf())
    val messages = _messages.asStateFlow()

    private val _forumTopics = MutableStateFlow<TdApi.ForumTopics?>(null)
    val forumTopics = _forumTopics.asStateFlow()

    private val _messageThreadId = MutableStateFlow<Long>(0)
    val messageThreadId = _messageThreadId.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            val instance = TdUtility.getInstance()
            val client = instance.getClient()

            _chat.value = client.execute(TdApi.GetChat(chatId))
            client.execute(TdApi.OpenChat(_chat.value?.id ?: chatId))

            if (_chat.value == null) {
                return@launch
            }

            val chat = _chat.value!!
            val newChatState = chat.toChatState()
            _chatState.value = newChatState
            if (_chatState.value == null) {
                return@launch
            }

            if (newChatState.type == ChatType.Forum) {
                val req = TdApi.GetForumTopics(
                    chatId,
                    "",
                    0,
                    0,
                    0,
                    100
                )
                _forumTopics.value = client.execute(req)
            }

            val history = getMessages(
                fromMessageId = chat.lastMessage?.id ?: 0L,
                offset = -50,
                limit = 100,
                onlyLocal = false
            )

            _messages.value = history ?: mutableListOf()

            if (_chatState.value?.type?.isPrivate() == true) {
                instance.updates
                    .filterIsInstance<TdApi.UpdateUserStatus>()
                    .onEach { update ->
                        if (update.userId != _chatState.value?.id) {
                            return@onEach
                        }
                        _chatState.update { it?.copy(userStatus = update.status) }
                    }
                    .launchIn(viewModelScope)
            } else if (_chatState.value?.type?.isGroup() == true) {
                instance.updates
                    .filterIsInstance<TdApi.UpdateChatOnlineMemberCount>()
                    .onEach { update ->
                        if (update.chatId != _chatState.value?.id) {
                            return@onEach
                        }
                        _chatState.update { it?.copy(onlineMembersCount = update.onlineMemberCount) }
                    }
                    .launchIn(viewModelScope)
            }

            instance.updates
                .filterIsInstance<TdApi.UpdateNewMessage>()
                .onEach { update ->
                    if (update.message.chatId != _chat.value?.id) {
                        return@onEach
                    }

                    val tdMessage = update.message.toTdMessage()
                    _messages.update { current ->
                        val newMessages = listOf(TdMessageState(message = tdMessage))
                        val combined = (newMessages + current).distinctBy { it.message.id }
                        recalculateMessagePositions(combined.toMutableList())
                    }
                }
                .launchIn(viewModelScope)

            instance.updates
                .filterIsInstance<TdApi.UpdateMessageEdited>()
                .onEach { update ->
                    if (update.chatId != _chat.value?.id) {
                        return@onEach
                    }

                    _messages.update { currentList ->
                        val index = currentList.indexOfFirst { it.message.id == update.messageId }
                        if (index == -1) {
                            return@update currentList
                        }

                        var item = currentList[index]

                        try {
                            val req = TdApi.GetMessage(
                                update.chatId,
                                update.messageId
                            )
                            val msg = instance.getClient().execute(req)
                            item = item.copy(message = msg.toTdMessage()) // Update message within the state
                        } catch (ex: Exception) {
                            Log.e(ex)
                        }

                        currentList.toMutableList().apply { this[index] = item }
                    }
                }
                .launchIn(viewModelScope)

            instance.updates
                .filterIsInstance<TdApi.UpdateChatAction>()
                .onEach { update ->
                    if (update.chatId != _chatState.value?.id) {
                        return@onEach
                    }

                    val action = update.toChatAction()
                    _chatState.update { currentState ->
                        if (currentState == null) {
                            return@update null
                        }

                        val newActions = currentState.actions.toMutableList().apply {
                            removeAll { it.senderId == action.senderId }
                            if (action.type != ChatActionType.Cancel) {
                                add(action)
                            }
                        }

                        currentState.copy(actions = newActions)
                    }
                }
                .launchIn(viewModelScope)

            instance.updates
                .filterIsInstance<TdApi.UpdateSupergroup>()
                .onEach { update ->
                    if (_chat.value?.type?.constructor != TdApi.ChatTypeSupergroup.CONSTRUCTOR) {
                        return@onEach
                    }

                    val supergroupId = (_chat.value?.type as TdApi.ChatTypeSupergroup).supergroupId
                    if (update.supergroup.id != supergroupId) {
                        return@onEach
                    }

                    _chatState.update { currentState ->
                        if (currentState == null) {
                            return@update null
                        }

                        currentState.copy(
                            memberStatus = update.supergroup.status,
                            type = when {
                                update.supergroup.isForum -> ChatType.Forum
                                update.supergroup.isChannel -> ChatType.Channel
                                update.supergroup.isBroadcastGroup -> ChatType.Broadcast
                                else -> currentState.type
                            }
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun recalculateMessagePositions(messages: MutableList<TdMessageState>): MutableList<TdMessageState> {
        if (messages.isEmpty()) return messages

        val result = mutableListOf<TdMessageState>()

        for (i in messages.indices) {
            val current = messages[i]
            val prevSender = messages.getOrNull(i - 1)?.message?.senderObject?.getSenderId()
            val nextSender = messages.getOrNull(i + 1)?.message?.senderObject?.getSenderId()
            val curSender = current.message.senderObject.getSenderId()

            val position = when {
                prevSender != curSender && nextSender != curSender -> ListItemPosition.Single
                prevSender != curSender && nextSender == curSender -> ListItemPosition.Top
                prevSender == curSender && nextSender == curSender -> ListItemPosition.Middle
                else -> ListItemPosition.Bottom
            }

            result.add(current.copy(position = position))
        }

        return result
    }

    suspend fun getMessages(
        fromMessageId: Long,
        offset: Int,
        limit: Int,
        onlyLocal: Boolean
    ): MutableList<TdMessageState>? {
        if (_chat.value == null && _chatState.value == null) {
            return null
        }

        val client = TdUtility.getInstance().getClient()
        val req = TdApi.GetChatHistory(
            _chat.value?.id ?: 0,
            fromMessageId,
            offset,
            limit,
            onlyLocal
        )

        val chatHistory = client.execute(req)
        if (chatHistory is TdApi.Messages) {
            val newMessages = chatHistory.messages
                .map { TdMessageState(
                    message = it.toTdMessage(),
                    position = ListItemPosition.Single
                ) }

            _messages.update { current ->
                val combined = (current + newMessages)
                    .distinctBy { it.message.id }
                    .sortedBy  { it.message.id }

                recalculateMessagePositions(combined.toMutableList())
            }

            return newMessages.toMutableList()
        }

        return null
    }

    suspend fun getMessagesInThread(
        messageThreadId: Long,
        fromMessageId: Long,
        offset: Int,
        limit: Int
    ): MutableList<TdMessageState>? {
        if (_chat.value == null && _chatState.value == null) {
            return null
        }

        _messageThreadId.update { messageThreadId }

        val client = TdUtility.getInstance().getClient()
        val req = TdApi.GetMessageThreadHistory(
            _chat.value?.id ?: 0,
            messageThreadId,
            fromMessageId,
            offset,
            limit
        )

        val chatHistory = client.execute(req)
        if (chatHistory is TdApi.Messages) {
            val newMessages = chatHistory.messages
                .map {
                    TdMessageState(
                        message = it.toTdMessage(),
                        position = ListItemPosition.Single
                    )
                }

            _messages.update { current ->
                val combined = newMessages
                    .distinctBy { it.message.id }
                    .sortedBy  { it.message.id }

                recalculateMessagePositions(combined.toMutableList())
            }

            return newMessages.toMutableList()
        }

        return null
    }

    companion object {
        fun provideFactory(chatId: Long): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ChatViewModel(chatId) as T
                }
            }
    }
}