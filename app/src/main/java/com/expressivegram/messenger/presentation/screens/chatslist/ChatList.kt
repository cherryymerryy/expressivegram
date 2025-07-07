package com.expressivegram.messenger.presentation.screens.chatslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expressivegram.messenger.presentation.screens.chatslist.components.ChatListCell
import com.expressivegram.messenger.presentation.screens.loading.LoadingScreen
import com.expressivegram.messenger.viewmodel.chatlist.ChatListViewModel
import org.drinkless.tdlib.TdApi

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatList(chatList: TdApi.ChatList, viewModel: ChatListViewModel = viewModel()) {
    val isLoading by viewModel.isLoading
    val chats by viewModel.chats

    LaunchedEffect(
        key1 = Unit
    ) {
        viewModel.loadChats(chatList)
    }

    if (isLoading) {
        LoadingScreen()
    } else {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clip(
                    MaterialTheme.shapes.large.copy(
                        bottomStart = MaterialTheme.shapes.small.bottomStart,
                        bottomEnd = MaterialTheme.shapes.small.bottomEnd,
                        topStart = MaterialTheme.shapes.small.topStart,
                        topEnd = MaterialTheme.shapes.small.topEnd
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (chats.isNotEmpty()) {
                items(chats) { chat ->
                    ChatListCell(chat)
                }
            } else {
                item {
                    LoadingIndicator()
                }

                item {
                    Text("Chats not found, try to send messages!")
                }
            }
        }
    }
}