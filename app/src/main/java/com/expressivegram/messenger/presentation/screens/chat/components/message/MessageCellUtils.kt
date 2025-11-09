package com.expressivegram.messenger.presentation.screens.chat.components.message

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun messageColors(isFromMe: Boolean): Pair<Color, Color> {
    return if (isFromMe)
        MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
    else
        MaterialTheme.colorScheme.surfaceContainer to MaterialTheme.colorScheme.onSurface
}