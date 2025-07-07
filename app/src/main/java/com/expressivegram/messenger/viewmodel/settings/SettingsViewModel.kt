package com.expressivegram.messenger.viewmodel.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.utils.UserConfig
import com.expressivegram.messenger.utils.execute
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi

class SettingsViewModel : ViewModel() {
    private val _personalChat = mutableStateOf<TdApi.Chat?>(null)
    val personalChat: State<TdApi.Chat?> = _personalChat

    fun updateUserConfig() {
        viewModelScope.launch {
            if (!UserConfig.Companion.getInstance().isInitialized()) {
                UserConfig.Companion.initialize()
            }
        }
    }

    fun updatePersonalChat(chatId: Long) {
        if (chatId == 0L) return

        val client = TdUtility.Companion.getInstance().getClient()

        viewModelScope.launch {
            _personalChat.value = client.execute(TdApi.GetChat(chatId))
        }
    }
}