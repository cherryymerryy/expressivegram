package com.expressivegram.messenger.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expressivegram.messenger.viewmodel.auth.LoginViewModel
import org.drinkless.tdlib.TdApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel()
) {
    val textFieldState = rememberTextFieldState()

    val placeholderText by viewModel.placeholderText
    val authState by viewModel.authState

    val isBusy by viewModel.isBusy
    val isError by viewModel.isError
    val isSecureField by viewModel.isSecureField

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onNextClicked(textFieldState.text as String)
                }
            ) {
                if (isBusy) {
                    return@FloatingActionButton
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.NavigateNext,
                    contentDescription = "nextStep"
                )
            }
        },
    ) { ip ->
        Column(
            modifier = Modifier
                .padding(ip)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isSecureField) {
                var passwordHidden by rememberSaveable { mutableStateOf(true) }

                OutlinedSecureTextField(
                    state = textFieldState,
                    enabled = !isBusy,
                    isError = isError,
                    label = { Text(placeholderText) },
                    placeholder = { Text(placeholderText) },
                    trailingIcon = {
                        val description = if (passwordHidden) "Show password" else "Hide password"
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                            tooltip = { PlainTooltip { Text(description) } },
                            state = rememberTooltipState(),
                        ) {
                            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                                val visibilityIcon = if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                Icon(
                                    imageVector = visibilityIcon,
                                    contentDescription = description
                                )
                            }
                        }
                    },
                    textObfuscationMode = if (passwordHidden) TextObfuscationMode.RevealLastTyped else TextObfuscationMode.Visible,
                    onKeyboardAction = {
                        KeyboardActions(
                            onDone = {
                                viewModel.onNextClicked(textFieldState.text as String)
                            }
                        )
                    }
                )
            } else {
                OutlinedTextField(
                    state = textFieldState,
                    enabled = !isBusy,
                    isError = isError,
                    label = { Text(placeholderText) },
                    placeholder = { Text(placeholderText) },
                    lineLimits = TextFieldLineLimits.SingleLine,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = when (authState?.constructor) {
                            TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> KeyboardType.Phone
                            TdApi.AuthorizationStateWaitEmailCode.CONSTRUCTOR,
                            TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> KeyboardType.Decimal
                            TdApi.AuthorizationStateWaitEmailAddress.CONSTRUCTOR -> KeyboardType.Email
                            else -> KeyboardType.Unspecified
                        }
                    ),
                    onKeyboardAction = {
                        KeyboardActions(
                            onDone = {
                                viewModel.onNextClicked(textFieldState.text as String)
                            }
                        )
                        
                    }
                )
            }
        }
    }
}