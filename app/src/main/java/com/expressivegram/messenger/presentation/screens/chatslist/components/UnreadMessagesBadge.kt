package com.expressivegram.messenger.presentation.screens.chatslist.components

import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun UnreadMessagesBadge(modifier: Modifier = Modifier, unreadMessagesCount: Int) {
    if (unreadMessagesCount > 0) {
        Badge(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Text(
                text = "$unreadMessagesCount",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp
            )
        }
    }
}