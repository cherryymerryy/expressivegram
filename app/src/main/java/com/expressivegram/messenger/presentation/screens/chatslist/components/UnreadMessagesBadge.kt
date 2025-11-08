package com.expressivegram.messenger.presentation.screens.chatslist.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UnreadMessagesBadge(modifier: Modifier = Modifier, unreadMessagesCount: Int) {
    if (unreadMessagesCount > 0) {
        Badge(
            modifier = modifier.size(8.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    }
}