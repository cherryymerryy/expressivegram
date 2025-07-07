package com.expressivegram.messenger.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.utils.Log
import kotlinx.coroutines.flow.filterIsInstance
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
        viewModelScope.launch {
            try {
                val initialState = TdUtility.initialize()
                Log.e("AppViewModel", "TDLib initialized. Initial state: ${initialState.javaClass.simpleName}")

                updateAppState(initialState)

                tdUtility.updates
                    .filterIsInstance<TdApi.UpdateAuthorizationState>()
                    .onEach { update ->
                        Log.e("AppViewModel", "Auth state updated via flow: ${update.authorizationState.javaClass.simpleName}")
                        updateAppState(update.authorizationState)
                    }
                    .launchIn(viewModelScope)

            } catch (e: Exception) {
                Log.e(e, "AppViewModel", "Failed to initialize TDLib")
            }
        }
    }

    private fun updateAppState(authState: TdApi.AuthorizationState) {
        _appState.value = when (authState.constructor) {
            TdApi.AuthorizationStateReady.CONSTRUCTOR -> AppState.LoggedIn
            TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> AppState.Loading
            TdApi.AuthorizationStateClosed.CONSTRUCTOR -> AppState.Loading
            TdApi.AuthorizationStateClosing.CONSTRUCTOR -> AppState.Loading
            TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR -> AppState.Loading
            else -> AppState.NeedsAuth
        }
    }
}
