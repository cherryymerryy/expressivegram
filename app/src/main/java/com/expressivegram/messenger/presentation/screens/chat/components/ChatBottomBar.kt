package com.expressivegram.messenger.presentation.screens.chat.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.EmojiEmotions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

@Composable
fun ChatBottomBar(onSendClick: (String) -> Unit) {
    val text = remember { mutableStateOf("") }

    BottomAppBar(
        actions = {
            IconButton(
                onClick = { },
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }
            OutlinedTextField(
                modifier = Modifier.clip(CircleShape),
                shape = CircleShape,
                value = text.value,
                onValueChange = { text.value = it },
                placeholder = { Text("Your message...") }
            )
            IconButton(
                onClick = {  },
            ) {
                Icon(
                    imageVector = Icons.Rounded.EmojiEmotions,
                    contentDescription = null
                )
            }
            IconButton(
                onClick = { onSendClick(text.value) },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Send,
                    contentDescription = null
                )
            }
        }
    )
}