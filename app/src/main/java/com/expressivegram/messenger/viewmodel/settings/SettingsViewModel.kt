package com.expressivegram.messenger.viewmodel.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expressivegram.messenger.utils.UserConfig
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi

class SettingsViewModel : ViewModel() {
    private val _personalChat = mutableStateOf<TdApi.Chat?>(null)
    val personalChat: State<TdApi.Chat?> = _personalChat

    init {
        _personalChat.value = UserConfig.Companion.getInstance().getCurrentPersonalChat()
    }

    fun updateUserConfig() {
        viewModelScope.launch {
            if (!UserConfig.Companion.getInstance().isInitialized()) {
                UserConfig.Companion.initialize()
            }

            _personalChat.value = UserConfig.Companion.getInstance().getCurrentPersonalChat()
        }
    }
}