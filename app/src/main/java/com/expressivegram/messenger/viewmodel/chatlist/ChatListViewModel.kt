package com.expressivegram.messenger.viewmodel.chatlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expressivegram.messenger.data.TdLibException
import com.expressivegram.messenger.data.chat.ChatType
import com.expressivegram.messenger.data.chatlist.ChatListItemState
import com.expressivegram.messenger.extensions.execute
import com.expressivegram.messenger.extensions.getChatTitle
import com.expressivegram.messenger.extensions.getForumTopicId
import com.expressivegram.messenger.extensions.getLastMessageText
import com.expressivegram.messenger.extensions.getSenderId
import com.expressivegram.messenger.extensions.isChannel
import com.expressivegram.messenger.extensions.isDefaultForum
import com.expressivegram.messenger.utils.DateUtility
import com.expressivegram.messenger.utils.DownloadController
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

    private val _folders = mutableStateOf<List<TdApi.ChatFolderInfo>>(emptyList())
    val folders: State<List<TdApi.ChatFolderInfo>> = _folders

    private val _pinnedChatsIds = mutableListOf<Long>()

    private val _chatItems = MutableStateFlow<List<ChatListItemState>>(emptyList())
    val chatItems: StateFlow<List<ChatListItemState>> = _chatItems.asStateFlow()

    private val _currentChatList = mutableStateOf<TdApi.ChatList>(TdApi.ChatListMain())
    val currentChatList: State<TdApi.ChatList> = _currentChatList

    init {
        val instance = TdUtility.getInstance()

        instance.updates
            .filterIsInstance<TdApi.UpdateChatUnreadMentionCount>()
            .onEach { update ->
                _chatItems.update { currentList ->
                    val index = currentList.indexOfFirst { it.chatId == update.chatId }
                    if (index == -1) {
                        return@update currentList
                    }

                    val oldItem = currentList[index]
                    val newItem = oldItem.copy(
                        unreadMentionsCount = update.unreadMentionCount
                    )

                    if (oldItem == newItem) return@update currentList

                    currentList.toMutableList().apply { this[index] = newItem }
                }
            }
            .launchIn(viewModelScope)

        instance.updates
            .filterIsInstance<TdApi.UpdateChatUnreadReactionCount>()
            .onEach { update ->
                _chatItems.update { currentList ->
                    val index = currentList.indexOfFirst { it.chatId == update.chatId }
                    if (index == -1) {
                        return@update currentList
                    }

                    val oldItem = currentList[index]
                    val newItem = oldItem.copy(
                        unreadReactionsCount = update.unreadReactionCount
                    )

                    if (oldItem == newItem) return@update currentList

                    currentList.toMutableList().apply { this[index] = newItem }
                }
            }
            .launchIn(viewModelScope)

        instance.updates
            .filterIsInstance<TdApi.UpdateChatReadOutbox>()
            .onEach { update ->
                _chatItems.update { currentList ->
                    val index = currentList.indexOfFirst { it.chatId == update.chatId }
                    if (index == -1) {
                        return@update currentList
                    }

                    val oldItem = currentList[index]
                    val isViewed = update.lastReadOutboxMessageId == oldItem.lastReadOutboxMessageId
                    val newItem = oldItem.copy(
                        isViewed = isViewed,
                        lastReadOutboxMessageId = update.lastReadOutboxMessageId,
                        unreadCount = if (isViewed) 0 else oldItem.unreadCount
                    )

                    if (oldItem == newItem) return@update currentList

                    currentList.toMutableList().apply { this[index] = newItem }
                }
            }
            .launchIn(viewModelScope)

        instance.updates
            .filterIsInstance<TdApi.UpdateChatReadInbox>()
            .onEach { update ->
                _chatItems.update { currentList ->
                    val index = currentList.indexOfFirst { it.chatId == update.chatId }
                    if (index == -1) {
                        return@update currentList
                    }

                    val oldItem = currentList[index]
                    val newItem = oldItem.copy(
                        unreadCount = update.unreadCount
                    )

                    if (oldItem == newItem) return@update currentList

                    currentList.toMutableList().apply { this[index] = newItem }
                }
            }
            .launchIn(viewModelScope)

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
                    var newItem = oldItem.copy(
                        lastForumTopicName = getLastTopicName(
                            oldItem,
                            update.lastMessage
                        ),
                        isFromMe = update.lastMessage?.getSenderId() == UserConfig.getInstance().getCurrentUser()?.id,
                        lastMessageText = update.lastMessage?.getLastMessageText() ?: "❓ Unsupported message content",
                    )

                    currentList.toMutableList().apply { this[index] = newItem }
                }
            }
            .launchIn(viewModelScope)

        instance.updates
            .filterIsInstance<TdApi.UpdateChatViewAsTopics>()
            .onEach { update ->
                _chatItems.update { currentList ->
                    val index = currentList.indexOfFirst { it.chatId == update.chatId }
                    if (index == -1) {
                        return@update currentList
                    }

                    val oldItem = currentList[index]
                    val newItem = oldItem.copy(
                        chatType = if (update.viewAsTopics) ChatType.Forum else oldItem.chatType
                    )

                    currentList.toMutableList().apply { this[index] = newItem }
                }
            }
            .launchIn(viewModelScope)

        instance.updates
            .filterIsInstance<TdApi.UpdateChatPhoto>()
            .onEach { update ->
                _chatItems.update { currentList ->
                    val index = currentList.indexOfFirst { it.chatId == update.chatId }
                    if (index == -1) {
                        return@update currentList
                    }

                    val oldItem = currentList[index]
                    val newItem = oldItem.copy(
                        photo = update.photo?.small
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

        instance.updates
            .filterIsInstance<TdApi.UpdateChatPhoto>()
            .onEach { update ->
                _chatItems.update { currentList ->
                    val index = currentList.indexOfFirst { it.chatId == update.chatId }
                    if (index == -1) return@update currentList

                    val oldItem = currentList[index]
                    val newItem = oldItem.copy(photo = update.photo?.small)
                    DownloadController.getInstance().downloadFile(update.photo?.small?.id ?: 0)

                    currentList.toMutableList().apply { this[index] = newItem }
                }
            }
            .launchIn(viewModelScope)

        instance.updates
            .filterIsInstance<TdApi.UpdateFile>()
            .onEach { update ->
                _chatItems.update { currentList ->
                    val index = currentList.indexOfFirst { it.photo?.id == update.file.id }
                    if (index == -1) return@update currentList

                    val file = instance.getClient().execute(TdApi.GetFile(update.file.id))

                    val oldItem = currentList[index]
                    val newItem = oldItem.copy(photo = file)

                    currentList.toMutableList().apply { this[index] = newItem }
                }
            }
            .launchIn(viewModelScope)
    }

    fun setChatList(list: TdApi.ChatList) {
        _currentChatList.value = list
    }

    suspend fun getLastTopicName(itemState: ChatListItemState, lastMessage: TdApi.Message?) : String? {
        if (lastMessage == null) {
            return null
        }

        return if (itemState.chatType == ChatType.Forum) {
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

                        val topicName = if (chat.isDefaultForum()) {
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

                        val isFromMe = chat.lastMessage?.getSenderId() == UserConfig.getInstance().getClientUserId()

                        Pair(chat, ChatListItemState(
                            chatId = chat.id,
                            title = chat.getChatTitle(),
                            chatType = when (chat.type) {
                                is TdApi.ChatTypePrivate -> ChatType.Private
                                is TdApi.ChatTypeSecret -> ChatType.Secret
                                is TdApi.ChatTypeBasicGroup -> ChatType.Group
                                is TdApi.ChatTypeSupergroup -> {
                                    if (chat.isDefaultForum()) {
                                        ChatType.Forum
                                    } else if (chat.isChannel()) {
                                        ChatType.Channel
                                    } else {
                                        ChatType.Group
                                    }
                                }

                                else -> ChatType.Group
                            },
                            photo = chat.photo?.small,
                            lastMessageText = chat.lastMessage?.getLastMessageText() ?: "❓ Unsupported message content",
                            unreadCount = chat.unreadCount,
                            unreadMentionsCount = chat.unreadMentionCount,
                            unreadReactionsCount = chat.unreadReactionCount,
                            lastForumTopicName = topicName,
                            isFromMe = isFromMe,
                            isViewed = chat.lastMessage?.id == chat.lastReadOutboxMessageId,
                            lastReadOutboxMessageId = chat.lastReadOutboxMessageId,
                            sentDate = DateUtility.getDateFromUnix(chat.lastMessage?.date ?: 0)
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