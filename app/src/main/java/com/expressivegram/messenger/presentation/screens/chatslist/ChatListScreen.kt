package com.expressivegram.messenger.presentation.screens.chatslist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expressivegram.messenger.presentation.screens.chatslist.components.ChatListCell
import com.expressivegram.messenger.presentation.screens.chatslist.components.FoldersListCell
import com.expressivegram.messenger.utils.UserConfig
import com.expressivegram.messenger.viewmodel.chatlist.ChatListViewModel
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatListScreen(
    chatList: TdApi.ChatList,
    onChatClick: (Long) -> Unit,
    viewModel: ChatListViewModel = viewModel()
) {
    val isFoldersLoading by viewModel.isFoldersLoading
    val chats by viewModel.chats.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadChats(chatList)
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        if (!isFoldersLoading || UserConfig.getInstance().getFolders() != null) {
            val folders = UserConfig.getInstance().getFolders()!!

            FoldersListCell(
                folders = folders,
                onClick = { index ->
                    scope.launch {
                        viewModel.loadChats(
                            chatList = if (index == 0) {
                                TdApi.ChatListMain()
                            } else {
                                TdApi.ChatListFolder(folders[index - 1].id)
                            }
                        )
                    }
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .clip(
                    MaterialTheme.shapes.large.copy(
                        bottomStart = MaterialTheme.shapes.large.bottomStart,
                        bottomEnd = MaterialTheme.shapes.large.bottomEnd,
                        topStart = MaterialTheme.shapes.large.topStart,
                        topEnd = MaterialTheme.shapes.large.topEnd
                    )
                )
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (chats.isNotEmpty()) {
                items(chats, key = { it.id }) { chat ->
                    ChatListCell(
                        chat = chat,
                        onClick = { onChatClick(chat.id) }
                    )
                }
            } else {
                item {
                    NotFoundScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NotFoundScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoadingIndicator()
        Text(
            text = "Chats not found, try to send messages!"
        )
    }
}