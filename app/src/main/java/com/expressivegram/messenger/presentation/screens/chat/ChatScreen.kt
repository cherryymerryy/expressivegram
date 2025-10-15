package com.expressivegram.messenger.presentation.screens.chat

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expressivegram.messenger.extensions.send
import com.expressivegram.messenger.presentation.screens.chat.components.ChatBottomBar
import com.expressivegram.messenger.presentation.screens.chat.components.ChatTopBar
import com.expressivegram.messenger.presentation.screens.chat.components.MessageCell
import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.viewmodel.chat.ChatViewModel
import org.drinkless.tdlib.TdApi

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

    Scaffold(
        topBar = {
            ChatTopBar(
                chat,
                onBackPressed = { onBackClick }
            )
        },
        bottomBar = {
            ChatBottomBar(
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
    ) { ip ->
        LazyColumn(
            modifier = Modifier.padding(ip),
            state = lazyListState
        ) {
            items(messages) { msg ->
                MessageCell(msg)
            }
        }
    }
}