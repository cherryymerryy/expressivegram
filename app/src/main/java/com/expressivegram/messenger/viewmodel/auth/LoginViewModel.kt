package com.expressivegram.messenger.viewmodel.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expressivegram.messenger.data.TdLibException
import com.expressivegram.messenger.extensions.execute
import com.expressivegram.messenger.utils.Log
import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.utils.UserConfig
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi

class LoginViewModel : ViewModel() {
    private val _authState = mutableStateOf<TdApi.AuthorizationState?>(null)
    val authState: State<TdApi.AuthorizationState?> = _authState

    val placeholderText: State<String> = derivedStateOf {
        when (_authState.value?.constructor) {
            TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR,
            TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> "Phone number"

            TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> "Confirmation code"
            TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> "Password, hint: ${(authState.value as TdApi.AuthorizationStateWaitPassword).passwordHint}"
            TdApi.AuthorizationStateReady.CONSTRUCTOR -> "Logged in!"
            else -> "Loading..."
        }
    }

    private val _isBusy = mutableStateOf(false)
    val isBusy: State<Boolean> = _isBusy

    private val _isError = mutableStateOf(false)
    val isError: State<Boolean> = _isError

    private val _isSecureField = mutableStateOf(false)
    val isSecureField: State<Boolean> = _isSecureField

    init {
        val instance = TdUtility.getInstance()

        viewModelScope.launch {
            _authState.value = instance.getClient().execute(TdApi.GetAuthorizationState())
        }

        instance.updates
            .filterIsInstance<TdApi.UpdateAuthorizationState>()
            .onEach { update ->
                val newState = update.authorizationState
                _authState.value = newState

                _isSecureField.value = when (newState) {
                    is TdApi.AuthorizationStateWaitPassword -> true
                    else -> false
                }

                if (newState is TdApi.AuthorizationStateReady) {
                    UserConfig.initialize()
                }
            }
            .launchIn(viewModelScope)
    }

    fun onNextClicked(input: String) {
        if (_isBusy.value) return

        val currentAuthState = _authState.value ?: return

        _isBusy.value = true
        _isError.value = false

        val instance = TdUtility.getInstance().getClient()

        viewModelScope.launch {
            try {
                when (currentAuthState.constructor) {

                    TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> {
                        val normalizedPhoneNumber = input.filter { it.isDigit() }
                        instance.send(
                            TdApi.SetAuthenticationPhoneNumber(
                                normalizedPhoneNumber,
                                TdApi.PhoneNumberAuthenticationSettings()
                            )
                        ) {
                            _isError.value = it is TdApi.Error
                        }
                    }

                    TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> {
                        instance.send(TdApi.CheckAuthenticationCode(input)) {
                            _isError.value = it is TdApi.Error
                        }
                    }

                    TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> {
                        instance.send(TdApi.CheckAuthenticationPassword(input)) {
                            _isError.value = it is TdApi.Error
                        }
                    }

                    else -> {
                        Log.Companion.e("ViewModel", "onNextClicked called in unhandled state: ${currentAuthState.javaClass.simpleName}")
                    }
                }
            } catch (e: TdLibException) {
                Log.Companion.e("ViewModel", "TDLib Error: ${e.error.message}")
            } catch (e: Exception) {
                Log.Companion.e(e, "ViewModel", "An unexpected error occurred")
            } finally {
                _isBusy.value = false
                _isError.value = false
            }
        }
    }
}