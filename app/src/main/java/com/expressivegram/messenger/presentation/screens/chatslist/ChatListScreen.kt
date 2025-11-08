package com.expressivegram.messenger.presentation.screens.chatslist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers.RED_DOMINATED_EXAMPLE
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expressivegram.messenger.presentation.components.preferences.ListItemPosition
import com.expressivegram.messenger.presentation.screens.chatslist.components.ChatListCell
import com.expressivegram.messenger.presentation.screens.chatslist.components.FoldersListCell
import com.expressivegram.messenger.presentation.theme.ExpressivegramTheme
import com.expressivegram.messenger.utils.UserConfig
import com.expressivegram.messenger.viewmodel.chatlist.ChatListViewModel
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatListScreen(
    onChatClick: (Long) -> Unit,
    viewModel: ChatListViewModel = viewModel()
) {
    val isFoldersLoading by viewModel.isFoldersLoading
    val chats by viewModel.chatItems.collectAsStateWithLifecycle()
    val list by viewModel.currentChatList

    val scope = rememberCoroutineScope()

    DisposableEffect(list) {
        val job = scope.launch {
            viewModel.loadChats(list)
        }

        onDispose { job.cancel() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(
                shape = MaterialTheme.shapes.small.copy(
                    topStart = MaterialTheme.shapes.extraLargeIncreased.topStart,
                    topEnd = MaterialTheme.shapes.extraLargeIncreased.topEnd,
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp)
                )
            )
            .background(MaterialTheme.colorScheme.surface)
    ) {
        val folders = UserConfig.getInstance().getFolders()
        if (!isFoldersLoading && folders != null) {
            FoldersListCell(
                folders = folders,
                onClick = { index ->
                    viewModel.setChatList(
                        when (index) {
                            0 -> TdApi.ChatListMain()
                            else -> TdApi.ChatListFolder(folders[index - 1].id)
                        }
                    )
                }
            )
        }

        val lazyListState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clip(
                    shape = MaterialTheme.shapes.small.copy(
                        topStart = MaterialTheme.shapes.medium.topStart,
                        topEnd = MaterialTheme.shapes.medium.topEnd,
                        bottomStart = CornerSize(0.dp),
                        bottomEnd = CornerSize(0.dp)
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            state = lazyListState
        ) {
            if (chats.isNotEmpty()) {
                itemsIndexed(
                    items = chats,
                    key = { _, item -> item.chatId }
                ) { index, item ->
                    ChatListCell(
                        item = item,
                        onClick = onChatClick,
                        position = when (index) {
                            0 -> {
                                if (chats.size == 1) {
                                    ListItemPosition.Single
                                } else {
                                    ListItemPosition.Top
                                }
                            }
                            chats.lastIndex -> ListItemPosition.Bottom
                            else -> ListItemPosition.Middle
                        }
                    )
                }
            } else {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularWavyProgressIndicator()
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    wallpaper = RED_DOMINATED_EXAMPLE
)
@Composable
fun TestChatListScreen() {
    ExpressivegramTheme {
        ChatListScreen(
            onChatClick = { }
        )
    }
}