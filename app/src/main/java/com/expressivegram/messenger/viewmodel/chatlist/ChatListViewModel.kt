package com.expressivegram.messenger.viewmodel.chatlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expressivegram.messenger.utils.Log
import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.utils.execute
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi

class ChatListViewModel : ViewModel() {
    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _chats = mutableStateOf<List<TdApi.Chat>>(emptyList())
    val chats: State<List<TdApi.Chat>> = _chats

    fun loadChats(chatList: TdApi.ChatList) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val client = TdUtility.getInstance().getClient()
                val result = client.execute(TdApi.GetChats(chatList, 100))
                val foundedChats: ArrayList<TdApi.Chat> = ArrayList()

                for (chatId in result.chatIds) {
                    val chat = client.execute(TdApi.GetChat(chatId))
                    if (chat == null) continue
                    foundedChats.add(chat)
                }

                _chats.value = foundedChats

            } catch (e: Exception) {
                Log.e(e)
                _chats.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}