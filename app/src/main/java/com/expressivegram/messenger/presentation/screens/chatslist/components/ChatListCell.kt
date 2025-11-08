package com.expressivegram.messenger.presentation.screens.chatslist.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.ui.tooling.preview.Wallpapers.BLUE_DOMINATED_EXAMPLE
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.data.chat.ChatType
import com.expressivegram.messenger.data.chatlist.ChatListItemState
import com.expressivegram.messenger.presentation.components.preferences.ListItemPosition
import com.expressivegram.messenger.presentation.components.profile.ChatPhotoItem
import com.expressivegram.messenger.presentation.theme.ExpressivegramTheme
import com.expressivegram.messenger.utils.DateUtility
import org.drinkless.tdlib.TdApi
import java.util.Date

@ExperimentalMaterial3ExpressiveApi
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

    TdApi.SendMessage()

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
                .padding(12.dp),
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

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = DateUtility.getFormattedDate(item.sentDate, "HH:mm"),
                        style = titleFont,
                        maxLines = 1,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    UnreadMessagesBadge(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        unreadMessagesCount = item.unreadCount
                    )
                }

                if (item.chatType == ChatType.Forum && item.lastForumTopicName != null) {
                    Text(
                        text = item.lastForumTopicName!!,
                        style = subtitleFont,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                    )
                }

                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        overflow = TextOverflow.Ellipsis,
                        text =  item.lastMessageText,
                        style = subtitleFont,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
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
        }
    }
}

@ExperimentalMaterial3ExpressiveApi
@Preview(wallpaper = BLUE_DOMINATED_EXAMPLE)
@Preview(wallpaper = BLUE_DOMINATED_EXAMPLE, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ChatListCellPreview() {
    val data = ChatListItemState(
        100L,
        "group test chat list cell",
        lastMessageText = "True titlуяяяяяяяяяяяяяяяяяяяяяяяяяяzzzzяMy text",
        unreadCount = 2,
        photo = null,
        lastForumTopicName = "General",
        isFromMe = true,
        isViewed = false,
        chatType = ChatType.Group,
        sentDate = Date(),
        lastReadOutboxMessageId = 0
    )

    val data2 = data.copy(
        title = "forum test chat list cell",
        chatType = ChatType.Forum,
        unreadCount = 42,
        isFromMe = false
    )

    val data3 = data.copy(
        title = "private test chat list cell",
        chatType = ChatType.Private,
        unreadCount = 0
    )

    ExpressivegramTheme {
        Column {
            ChatListCell(
                item = data,
                onClick = { },
                position = ListItemPosition.Top
            )
            Spacer(modifier = Modifier.size(2.dp))
            ChatListCell(
                item = data2,
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
                item = data3,
                onClick = { },
                position = ListItemPosition.Single
            )
        }
    }
}