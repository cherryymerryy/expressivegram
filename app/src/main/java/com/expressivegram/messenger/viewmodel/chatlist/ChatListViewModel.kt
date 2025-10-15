package com.expressivegram.messenger.viewmodel.chatlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expressivegram.messenger.extensions.execute
import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.utils.UserConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi

class ChatListViewModel : ViewModel() {
    private val _isFoldersLoading = mutableStateOf(true)
    val isFoldersLoading: State<Boolean> = _isFoldersLoading

    private val _chats = MutableStateFlow<List<TdApi.Chat>>(emptyList())
    val chats: StateFlow<List<TdApi.Chat>> = _chats.asStateFlow()

    private val _folders = mutableStateOf<List<TdApi.ChatFolderInfo>>(emptyList())
    val folders: State<List<TdApi.ChatFolderInfo>> = _folders

    private val _pinnedChatsIds = mutableListOf<Long>()

    init {
        val instance = TdUtility.getInstance()

        instance.updates
            .filterIsInstance<TdApi.UpdateChatPosition>()
            .onEach { updateChatPosition ->
//                _chats.update { currentList ->
//                    val newList = currentList.toMutableList()
//                    val chat = newList.firstOrNull { it.id == updateChatPosition.chatId }
//
//                    if (chat == null) {
//                        return@onEach
//                    }
//
//                    newList.remove(chat)
//                    newList.add(updateChatPosition.position.order.toInt(), chat)
//                    newList
//                }
            }
            .launchIn(viewModelScope)

        instance.updates
            .filterIsInstance<TdApi.UpdateNewMessage>()
            .onEach { updateNewMessage ->
                val newElementIndex = _pinnedChatsIds.lastIndex + 1
                val isPinned = _pinnedChatsIds.contains(updateNewMessage.message.chatId)

                _chats.update { currentList ->
                    val newList = currentList.toMutableList()
                    val localChat = newList.firstOrNull { it.id == updateNewMessage.message.chatId }

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
            .filterIsInstance<TdApi.UpdateChatFolders>()
            .onEach { updateChatFolders ->
                _isFoldersLoading.value = true

                if (updateChatFolders.chatFolders == null) {
                    _isFoldersLoading.value = false
                    return@onEach
                }

                _folders.value = updateChatFolders.chatFolders.asList()
                UserConfig.getInstance().setFolders(_folders.value)
                _isFoldersLoading.value = false
            }
            .launchIn(viewModelScope)
    }

    suspend fun loadChats(chatList: TdApi.ChatList) {
        val instance = TdUtility.getInstance().getClient()
        val chatsIds = instance.execute(TdApi.GetChats(chatList, 100))
        val chats = mutableListOf<TdApi.Chat>()

        _pinnedChatsIds.clear()

        chatsIds.chatIds.forEach { id ->
            val chat = instance.execute(TdApi.GetChat(id))
            chats.removeAll { it.id == chat.id }

            val currentPosition = chat.positions.firstOrNull { it.list.constructor == chatList.constructor }
            if (currentPosition != null) {
                if (currentPosition.isPinned) {
                    _pinnedChatsIds.add(chat.id)
                    chats.add(_pinnedChatsIds.lastIndex, chat)
                }
            }

            if (!chats.contains(chat)) {
                chats.add(chat)
            }
        }

        _chats.update { currentList ->
            val newList = currentList.toMutableList()
            newList.removeAll(newList)
            newList.addAll(chats)
            newList
        }
    }
}