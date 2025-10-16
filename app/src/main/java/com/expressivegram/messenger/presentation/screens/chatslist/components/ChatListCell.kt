package com.expressivegram.messenger.presentation.screens.chatslist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expressivegram.messenger.extensions.isForum
import com.expressivegram.messenger.presentation.components.profile.ChatPhotoItem
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
            ChatPhotoItem(
                name = title,
                photo = chat.photo?.small
            )

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
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1
                    )
                }

                Text(
                    text = lastMessageText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
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