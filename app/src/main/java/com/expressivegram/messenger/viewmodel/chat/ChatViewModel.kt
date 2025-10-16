package com.expressivegram.messenger.viewmodel.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expressivegram.messenger.extensions.send
import com.expressivegram.messenger.utils.Log
import com.expressivegram.messenger.utils.TdUtility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi

class ChatViewModel : ViewModel() {
    private val _chat = mutableStateOf<TdApi.Chat?>(null)
    val chat: State<TdApi.Chat?> = _chat

    private val _messages = MutableStateFlow<List<TdApi.Message>>(emptyList())
    val messages: StateFlow<List<TdApi.Message>> = _messages.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val instance = TdUtility.getInstance()

                instance.updates
                    .filterIsInstance<TdApi.UpdateNewMessage>()
                    .onEach { update ->
                        if (update.message.chatId != _chat.value?.id) {
                            return@onEach
                        }

                        _messages.update { currentList ->
                            listOf(update.message) + currentList
                        }
                    }
                    .launchIn(viewModelScope)
            } catch (ex: Exception) {
                Log.e(ex)
            }
        }
    }

    fun openChat(chatId: Long = 0) {
        TdUtility
            .getInstance()
            .getClient()
            .send(TdApi.OpenChat(chat.value?.id ?: chatId))
    }

    fun getChat(chatId: Long) {
        TdUtility
            .getInstance()
            .getClient()
            .send(TdApi.GetChat(chatId)) {
                if (it is TdApi.Chat) {
                    _chat.value = it
                }
            }
    }

    fun getMessages(chatId: Long = 0) {
        TdUtility
            .getInstance()
            .getClient()
            .send(
                TdApi.GetChatHistory(
                    _chat.value?.id ?: chatId,
                    _chat.value?.lastMessage?.id ?: 0,
                    0,
                    100,
                    false
                )
            ) {
                if (it is TdApi.Messages) {
                    _messages.value = it.messages.toList()
                }
            }
    }
}