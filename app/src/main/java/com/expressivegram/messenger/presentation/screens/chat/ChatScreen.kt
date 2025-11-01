package com.expressivegram.messenger.presentation.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expressivegram.messenger.extensions.send
import com.expressivegram.messenger.presentation.screens.chat.components.ChatBottomBar
import com.expressivegram.messenger.presentation.screens.chat.components.ChatTopBar
import com.expressivegram.messenger.presentation.screens.chat.components.MessageCell
import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.viewmodel.chat.ChatViewModel
import org.drinkless.tdlib.TdApi

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatScreen(
    chatId: Long,
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = viewModel()
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(chatId) {
        viewModel.getChat(chatId)
        viewModel.openChat(chatId)
        viewModel.getMessages(chatId)
    }

    val chat by viewModel.chat
    val messages by viewModel.messages.collectAsStateWithLifecycle()

    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(index = 0)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxHeight()
            .imePadding(),
        topBar = {
            ChatTopBar(
                chat,
                onBackPressed = { onBackClick() }
            )
        }
    ) { ip ->
        Box(
            modifier = Modifier
                .padding(ip)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(
                        horizontal = 6.dp,
                    )
                    .fillMaxSize(),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true,
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    MessageCell(msg)
                }
            }

            ChatBottomBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                onSendClick = { text ->
                    TdUtility
                        .getInstance()
                        .getClient()
                        .send(
                            TdApi.SendMessage(
                                chatId,
                                0,
                                null,
                                null,
                                null,
                                TdApi.InputMessageText(
                                    TdApi.FormattedText(
                                        text,
                                        null
                                    ),
                                    null,
                                    false
                                )
                            )
                        )
                }
            )
        }
    }
}