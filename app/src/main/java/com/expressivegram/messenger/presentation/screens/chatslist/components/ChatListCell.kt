package com.expressivegram.messenger.presentation.screens.chatslist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.data.ChatListItemState
import com.expressivegram.messenger.data.ChatType
import com.expressivegram.messenger.presentation.components.preferences.ListItemPosition
import com.expressivegram.messenger.presentation.components.profile.ChatPhotoItem
import java.time.LocalDate

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatListCell(
    item: ChatListItemState,
    position: ListItemPosition,
    onClick: (Long) -> Unit
) {
    val shape = when (position) {
        ListItemPosition.Single -> MaterialTheme.shapes.large
        ListItemPosition.Top -> MaterialTheme.shapes.large.copy(
            bottomStart = MaterialTheme.shapes.extraSmall.bottomStart,
            bottomEnd = MaterialTheme.shapes.extraSmall.bottomEnd
        )

        ListItemPosition.Bottom -> MaterialTheme.shapes.large.copy(
            topStart = MaterialTheme.shapes.extraSmall.topStart,
            topEnd = MaterialTheme.shapes.extraSmall.topEnd
        )

        ListItemPosition.Middle -> MaterialTheme.shapes.extraSmall
    }

    val typography = MaterialTheme.typography
    val titleFont = typography.titleMedium
    val subtitleFont = typography.labelMedium

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape),
        onClick = { onClick(item.chatId) },
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = shape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChatPhotoItem(
                name = item.title,
                photo = item.photo,
                chatType = item.chatType
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        overflow = TextOverflow.Ellipsis,
                        text = item.title,
                        style = titleFont,
                        maxLines = 1
                    )
                    Text(
                        text = "HH:mm",
                        style = titleFont,
                        maxLines = 1
                    )
                }

                if (item.isForum && item.lastForumTopicName != null) {
                    Text(
                        text = item.lastForumTopicName!!,
                        style = subtitleFont,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1
                    )
                }

                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        overflow = TextOverflow.Ellipsis,
                        text = item.lastMessageText,
                        style = subtitleFont,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1
                    )

                    if (item.isFromMe) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = if (item.isViewed) {
                                Icons.Outlined.DoneAll
                            } else {
                                Icons.Outlined.Check
                            },
                            contentDescription = "view"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.End
            ) {
                UnreadMessagesBadge(item.unreadCount)
            }
        }
    }
}

@Preview
@Composable
fun ChatListCellPreview() {
    val data = ChatListItemState(
        100L,
        "True titlуяяяяяяяяяяяяяяяяяяяяяяяяяяя",
        lastMessageText = "TODO()",
        unreadCount = 2,
        photo = null,
        isForum = false,
        lastForumTopicName = "General",
        isFromMe = true,
        isViewed = false,
        chatType = ChatType.Group,
        sentDate = LocalDate.now()
    )

    Column {
        ChatListCell(
            item = data,
            onClick = { },
            position = ListItemPosition.Top
        )
        Spacer(modifier = Modifier.size(2.dp))
        ChatListCell(
            item = data,
            onClick = { },
            position = ListItemPosition.Middle
        )
        Spacer(modifier = Modifier.size(2.dp))
        ChatListCell(
            item = data,
            onClick = { },
            position = ListItemPosition.Bottom
        )

        Spacer(modifier = Modifier.size(16.dp))

        ChatListCell(
            item = data,
            onClick = { },
            position = ListItemPosition.Single
        )
    }
}