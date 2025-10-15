package com.expressivegram.messenger.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expressivegram.messenger.viewmodel.auth.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel()
) {
    val inputValue by viewModel.inputValue
    val placeholderText by viewModel.placeholderText
    val isBusy by viewModel.isBusy

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onNextClicked() },
            ) {
                if (isBusy) return@FloatingActionButton
                Text("Next")
            }
        },
    ) { ip ->
        Column(
            modifier = Modifier.padding(ip).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = inputValue,
                onValueChange = { viewModel.onInputValueChange(it) },
                placeholder = { Text(placeholderText) },
                enabled = !isBusy,
                singleLine = true,
                maxLines = 1
            )
        }
    }
}