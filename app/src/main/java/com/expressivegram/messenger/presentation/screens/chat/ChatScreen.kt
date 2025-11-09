package com.expressivegram.messenger.presentation.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expressivegram.messenger.data.chat.ChatType
import com.expressivegram.messenger.presentation.screens.chat.components.bars.ChannelBottomBar
import com.expressivegram.messenger.presentation.screens.chat.components.bars.ChatBottomBar
import com.expressivegram.messenger.presentation.screens.chat.components.bars.ChatTopBar
import com.expressivegram.messenger.presentation.screens.chat.components.forum.ForumTopicTabs
import com.expressivegram.messenger.presentation.screens.chat.components.lists.HistoryList
import com.expressivegram.messenger.utils.SendMessageHelper
import com.expressivegram.messenger.viewmodel.chat.ChatViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalMaterial3ExpressiveApi
@Composable
fun ChatScreen(
    chatId: Long,
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = viewModel(factory = ChatViewModel.provideFactory(chatId))
) {
    val chatState by viewModel.chatState.collectAsStateWithLifecycle()
    if (chatState == null) {
        return
    }

    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val forumTopics by viewModel.forumTopics.collectAsStateWithLifecycle()
    val messageThreadId by viewModel.messageThreadId.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .imePadding()
    ) {
        chatState?.let {
            ChatTopBar(
                chat = it,
                onBackPressed = { onBackClick() }
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (chatState?.type == ChatType.Forum) {
                ForumTopicTabs(
                    topics = forumTopics,
                    hasForumTabs = chatState?.hasForumTabs == true,
                    onTabClick = { messageThreadId ->
                        scope.launch(Dispatchers.IO) {
                            viewModel.getMessagesInThread(
                                messageThreadId = messageThreadId,
                                fromMessageId = 0,
                                offset = -50,
                                limit = 100
                            )
                        }
                    }
                ) {
                    messages.reverse()
                    HistoryList(messages)
                }
            } else {
                HistoryList(messages)
            }

            if (chatState?.type == ChatType.Channel) {
                ChannelBottomBar(
                    memberStatus = chatState?.memberStatus,
                    permissions = chatState?.permissions
                )
            } else {
                ChatBottomBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    onSendClick = { text ->
                        SendMessageHelper.sendMessage(
                            chatId = chatId,
                            messageThreadId = messageThreadId,
                            text = text
                        )
                    },
                    memberStatus = chatState?.memberStatus,
                    permissions = chatState?.permissions
                )
            }
        }
    }
}