package com.expressivegram.messenger.presentation.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarColors
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatBottomBar(
    modifier: Modifier = Modifier,
    onSendClick: (String) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(true) }
    val textFieldState = rememberTextFieldState()

    HorizontalFloatingToolbar(
        modifier = modifier
            .offset(y = -FloatingToolbarDefaults.ScreenOffset)
            .zIndex(1f)
            .padding(horizontal = 12.dp),
        expanded = expanded,
        colors = FloatingToolbarColors(
            toolbarContainerColor = MaterialTheme.colorScheme.primary,
            toolbarContentColor = MaterialTheme.colorScheme.onPrimary,
            fabContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            fabContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        IconButton(onClick = { /*TODO*/ }, enabled = false) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add"
            )
        }
        IconButton(onClick = { /*TODO*/ }, enabled = false) {
            Icon(
                imageVector = Icons.Filled.CameraAlt,
                contentDescription = "Camera"
            )
        }
        if (textFieldState.text.isEmpty()) {
            IconButton(onClick = { /*TODO*/ }, enabled = false) {
                Icon(
                    imageVector = Icons.Filled.KeyboardVoice,
                    contentDescription = "Voice message"
                )
            }
        }
        TextField(
            modifier = Modifier.weight(1f).height(54.dp),
            state = textFieldState,
            shape = CircleShape,
            placeholder = { Text(text = "Message") },
            trailingIcon = {
                if (textFieldState.text.isNotEmpty()) {
                    IconButton(onClick = { onSendClick(textFieldState.text.toString()) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.EmojiEmotions,
                            contentDescription = "Emoji"
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            )
        )
    }
}