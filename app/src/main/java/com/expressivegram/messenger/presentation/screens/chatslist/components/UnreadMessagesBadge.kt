package com.expressivegram.messenger.presentation.screens.chatslist.components

import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun UnreadMessagesBadge(unreadMessagesCount: Int) {
    if (unreadMessagesCount > 0) {
        Badge(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        ) {
            Text("$unreadMessagesCount")
        }
    }
}