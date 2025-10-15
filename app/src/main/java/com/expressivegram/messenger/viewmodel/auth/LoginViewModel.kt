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
    private val tdUtility = TdUtility.Companion.getInstance()
    private val client = tdUtility.getClient()

    private val _authState = mutableStateOf<TdApi.AuthorizationState?>(null)

    val placeholderText: State<String> = derivedStateOf {
        when (_authState.value?.constructor) {
            TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR,
            TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> "Phone number"

            TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> "Confirmation code"
            TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> "Password"
            TdApi.AuthorizationStateReady.CONSTRUCTOR -> "Logged in!"
            else -> "Loading..."
        }
    }

    private val _inputValue = mutableStateOf("")
    val inputValue: State<String> = _inputValue

    private val _isBusy = mutableStateOf(false)
    val isBusy: State<Boolean> = _isBusy

    init {
        viewModelScope.launch {
            _authState.value = client.execute(TdApi.GetAuthorizationState())
        }

        tdUtility.updates
            .filterIsInstance<TdApi.UpdateAuthorizationState>()
            .onEach { update ->
                val newState = update.authorizationState
                Log.Companion.e("ViewModel", "Auth state UPDATED via flow: ${newState.javaClass.simpleName}")
                _authState.value = newState
                _inputValue.value = ""

                if (newState is TdApi.AuthorizationStateReady) {
                    UserConfig.Companion.initialize()
                }
            }
            .launchIn(viewModelScope)
    }

    fun onInputValueChange(newValue: String) {
        _inputValue.value = newValue
    }

    fun onNextClicked() {
        if (_isBusy.value) return

        val currentAuthState = _authState.value ?: return

        viewModelScope.launch {
            _isBusy.value = true
            try {
                when (currentAuthState.constructor) {

                    TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> {
                        val normalizedPhoneNumber = _inputValue.value.filter { it.isDigit() }
                        Log.Companion.e("ViewModel", "Sending phone number: $normalizedPhoneNumber")
                        client.send(
                            TdApi.SetAuthenticationPhoneNumber(
                                normalizedPhoneNumber,
                                TdApi.PhoneNumberAuthenticationSettings()
                            ),
                            { Log.Companion.e(it.toString(), "LoginResult") },
                            { Log.Companion.e(it, "LoginError") }
                        )
                    }

                    TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> {
                        client.execute(TdApi.CheckAuthenticationCode(_inputValue.value))
                    }

                    TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> {
                        client.execute(TdApi.CheckAuthenticationPassword(_inputValue.value))
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
            }
        }
    }
}