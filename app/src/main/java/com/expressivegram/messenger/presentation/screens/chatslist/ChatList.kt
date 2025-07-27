package com.expressivegram.messenger.presentation.screens.chatslist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.expressivegram.messenger.presentation.screens.chatslist.components.ChatListCell
import com.expressivegram.messenger.presentation.screens.chatslist.components.FoldersListCell
import com.expressivegram.messenger.utils.UserConfig
import com.expressivegram.messenger.viewmodel.chatlist.ChatListViewModel
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatList(
    chatList: TdApi.ChatList,
    navController: NavController,
    viewModel: ChatListViewModel = viewModel()
) {
    val isFoldersLoading by viewModel.isFoldersLoading
    val chats by viewModel.chats.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadChats(chatList)
    }

    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
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
                        bottomStart = MaterialTheme.shapes.small.bottomStart,
                        bottomEnd = MaterialTheme.shapes.small.bottomEnd,
                        topStart = MaterialTheme.shapes.small.topStart,
                        topEnd = MaterialTheme.shapes.small.topEnd
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (chats.isNotEmpty()) {
                items(chats, key = { it.id }) { chat ->
                    val rememberedOnClick = remember(chat.id) {
                        { navController.navigate("chat/${chat.id}") }
                    }
                    ChatListCell(
                        chat = chat,
                        onClick = rememberedOnClick
                    )
                }
            } else {
                item {
                    LoadingIndicator()
                }

                item {
                    Text(
                        text = "Chats not found, try to send messages!"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewChatList() {
    ChatList(TdApi.ChatListMain(), rememberNavController())
}