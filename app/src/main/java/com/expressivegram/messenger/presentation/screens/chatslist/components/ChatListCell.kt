package com.expressivegram.messenger.presentation.screens.chatslist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.expressivegram.messenger.extensions.getMessageContent
import com.expressivegram.messenger.extensions.isForum
import com.expressivegram.messenger.utils.DownloadController
import com.expressivegram.messenger.viewmodel.chatlist.ChatListCellViewModel
import com.expressivegram.messenger.viewmodel.chatlist.ChatListCellViewModelFactory
import org.drinkless.tdlib.TdApi

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatListCell(
    chat: TdApi.Chat,
    onClick: () -> Unit
) {
    val viewModelFactory = ChatListCellViewModelFactory(chat)
    val viewModel: ChatListCellViewModel = viewModel(
        key = chat.id.toString(),
        factory = viewModelFactory
    )

    val title by viewModel.title
    val lastMessageText by viewModel.lastMessageText
    val unreadMessagesCount by viewModel.unreadMessagesCount
    val lastForumTopic by viewModel.lastForumTopic
    val isForum = chat.isForum()
    var chatPhotoPath by remember { mutableStateOf("") }

    if (chat.photo?.small != null) {
        if (!(chat.photo?.small?.local?.isDownloadingCompleted ?: true)) {
            DownloadController.getInstance().downloadFile(chat.photo?.small?.id ?: 0)
        } else {
            chatPhotoPath = chat.photo?.small?.local?.path ?: ""
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: Make chat profile photo
            if (chatPhotoPath.isEmpty()) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Cyan)
                        .clip(
                            MaterialTheme.shapes.large.copy(
                                bottomStart = MaterialTheme.shapes.extraExtraLarge.bottomStart,
                                bottomEnd = MaterialTheme.shapes.extraExtraLarge.bottomEnd,
                                topStart = MaterialTheme.shapes.extraExtraLarge.topStart,
                                topEnd = MaterialTheme.shapes.extraExtraLarge.topEnd
                            )
                        )
                )
            } else {
                AsyncImage(
                    modifier = Modifier.size(32.dp),
                    model = chatPhotoPath,
                    contentDescription = title
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )

                if (isForum && lastForumTopic != null) {
                    Text(
                        text = lastForumTopic?.info?.name ?: "❌ Unknown topic",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1
                    )
                }

                Text(
                    text = if (isForum && lastForumTopic != null) {
                        lastForumTopic?.lastMessage?.getMessageContent() ?: lastMessageText
                    } else {
                        lastMessageText
                    },
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.End
            ) {
                UnreadMessagesBadge(unreadMessagesCount)
            }
        }
    }
}