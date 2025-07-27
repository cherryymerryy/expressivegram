package com.expressivegram.messenger.viewmodel.chatlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expressivegram.messenger.utils.Log
import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.utils.UserConfig
import com.expressivegram.messenger.utils.execute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi

class ChatListViewModel : ViewModel() {
    private val _isFoldersLoading = mutableStateOf(true)
    val isFoldersLoading: State<Boolean> = _isFoldersLoading

    private val _chats = MutableStateFlow<List<TdApi.Chat>>(emptyList())
    val chats: StateFlow<List<TdApi.Chat>> = _chats.asStateFlow()

    private val _folders = mutableStateOf<List<TdApi.ChatFolderInfo>>(emptyList())
    val folders: State<List<TdApi.ChatFolderInfo>> = _folders

    init {
        viewModelScope.launch {
            try {
                val instance = TdUtility.getInstance()

                instance.updates
                    .filterIsInstance<TdApi.UpdateNewChat>()
                    .onEach { updateNewChat ->
                        _chats.update { currentList ->
                            val newList = currentList.toMutableList()
                            newList.removeAll { it.id == updateNewChat.chat.id }
                            newList.add(0, updateNewChat.chat)
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
            } catch(e: Exception) {
                Log.e(
                    tr = e,
                    tag = "ChatListViewModel",
                    msg = "Something went wrong while trying process received TDLib update:"
                )
            }
        }
    }

    fun loadChats(chatList: TdApi.ChatList) {
        viewModelScope.launch {
            try {
                _chats.update { currentList ->
                    val newList = currentList.toMutableList()
                    newList.removeAll(newList)
                    newList
                }
                TdUtility
                    .getInstance()
                    .getClient()
                    .execute(TdApi.LoadChats(chatList, 100))
            } catch (e: Exception) {
                Log.e(e)
            }
        }
    }
}