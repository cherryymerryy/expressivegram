package com.expressivegram.messenger.presentation.screens.chatslist.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Adjust
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun UnreadMessagesBadge(unreadMessagesCount: Int) {
    BadgedBox(
        badge = {
            if (unreadMessagesCount > 0) {
                Badge(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ) {
                    Text("$unreadMessagesCount")
                }
            }
        }
    ) {
        Icon(
            imageVector = Icons.Outlined.Adjust,
            contentDescription = "Unread messages"
        )
    }
}