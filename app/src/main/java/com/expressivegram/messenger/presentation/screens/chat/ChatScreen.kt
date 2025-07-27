package com.expressivegram.messenger.presentation.screens.chat

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.expressivegram.messenger.presentation.screens.chat.components.ChatBottomBar
import com.expressivegram.messenger.presentation.screens.chat.components.ChatTopBar

@Composable
fun ChatScreen(chatId: Long) {
    Scaffold(
        topBar = { ChatTopBar() },
        bottomBar = { ChatBottomBar() }
    ) { ip ->
        LazyColumn(
            modifier = Modifier.padding(ip)
        ) {
            item {
                Text("chatID: $chatId")
            }
        }
    }
}

@Preview()
@Composable
fun PreviewChatScreen() {
    ChatScreen(0)
}