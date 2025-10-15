package com.expressivegram.messenger.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expressivegram.messenger.utils.Log
import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.utils.UserConfig
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi

sealed class AppState {
    object Loading : AppState()
    object NeedsAuth : AppState()
    object LoggedIn : AppState()
}

class AppViewModel : ViewModel() {

    private val _appState = mutableStateOf<AppState>(AppState.Loading)
    val appState: State<AppState> = _appState

    private val tdUtility = TdUtility.getInstance()

    init {
        tdUtility.authState
            .filterNotNull()
            .onEach { authState ->
                Log.e("Received auth state from TdUtility: ${authState.javaClass.simpleName}", "AppViewModel")
                updateAppState(authState)
            }
            .launchIn(viewModelScope)

        tdUtility.updates
            .filterIsInstance<TdApi.UpdateChatFolders>()
            .onEach { updateFolders ->
                UserConfig.getInstance().setFolders(updateFolders.chatFolders.asList())
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            try {
                TdUtility.initialize()
                Log.e("TDLib initialization process started.", "AppViewModel")
            } catch (e: Exception) {
                Log.e(e, "AppViewModel", "Failed to initialize TDLib")
            }
        }
    }

    private fun updateAppState(authState: TdApi.AuthorizationState) {
        val newAppState = when (authState.constructor) {
            TdApi.AuthorizationStateReady.CONSTRUCTOR -> AppState.LoggedIn

            TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR,
            TdApi.AuthorizationStateClosing.CONSTRUCTOR,
            TdApi.AuthorizationStateClosed.CONSTRUCTOR,
            TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR -> AppState.Loading

            else -> AppState.NeedsAuth
        }

        viewModelScope.launch {
            if (!UserConfig.Companion.getInstance().isInitialized()) {
                UserConfig.Companion.initialize()
            }
        }

        Log.e("Mapping to AppState: ${newAppState.javaClass.simpleName}", "AppViewModel")
        _appState.value = newAppState
    }
}