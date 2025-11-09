package com.expressivegram.messenger.presentation.screens.chatslist.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@ExperimentalMaterial3Api
@Composable
fun ChatListBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequested: () -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequested
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        ) {
            Text(
                text = "Leave / Delete"
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        ) {
            Text(
                text = "Leave / Delete"
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        ) {
            Text(
                text = "Leave / Delete"
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        ) {
            Text(
                text = "Leave / Delete"
            )
        }
    }
}