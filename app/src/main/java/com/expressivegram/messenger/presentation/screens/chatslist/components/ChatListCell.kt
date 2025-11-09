package com.expressivegram.messenger.presentation.screens.chatslist.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers.BLUE_DOMINATED_EXAMPLE
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.data.chat.ChatType
import com.expressivegram.messenger.data.chatlist.ChatListItemState
import com.expressivegram.messenger.presentation.components.PrimaryIcon
import com.expressivegram.messenger.presentation.components.profile.ChatPhotoItem
import com.expressivegram.messenger.presentation.theme.ExpressivegramTheme
import com.expressivegram.messenger.utils.DateUtility
import java.util.Date

@ExperimentalMaterial3Api
@ExperimentalMaterial3ExpressiveApi
@Stable
@Composable
fun ChatListCell(
    item: ChatListItemState,
    onClick: (Long) -> Unit
) {
    var isBottomSheetExpanded: Boolean by remember { mutableStateOf(false) }

    val typography = MaterialTheme.typography
    val titleFont = typography.titleMedium
    val subtitleFont = typography.labelMedium
    val textColor = MaterialTheme.colorScheme.onSurface

    val formattedDate = remember(item.sentDate) {
        DateUtility.getFormattedDate(
            date = item.sentDate,
            formatPattern = "HH:mm"
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .clickable(true) { onClick(item.chatId) }
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onLongPress = {
//                        isBottomSheetExpanded = true
//                    }
//                )
//            }
//            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
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
                    color = textColor,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = formattedDate,
                    style = titleFont,
                    color = textColor,
                    maxLines = 1,
                )

                Spacer(modifier = Modifier.width(4.dp))

                Row(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (item.unreadMentionsCount > 0) {
                        PrimaryIcon(imageVector = Icons.Outlined.AlternateEmail)
                    }
                    if (item.unreadReactionsCount > 0) {
                        PrimaryIcon(imageVector = Icons.Outlined.Star)
                    }
                    if (item.unreadCount > 0) {
                        UnreadMessagesBadge(unreadMessagesCount = item.unreadCount)
                    }
                }
            }

            if (item.chatType == ChatType.Forum && item.lastForumTopicName != null) {
                Text(
                    text = item.lastForumTopicName!!,
                    style = subtitleFont,
                    color = textColor,
                    maxLines = 1,
                )
            }

            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    text =  item.lastMessageText,
                    style = subtitleFont,
                    color = textColor,
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

    if (isBottomSheetExpanded) {
        ChatListBottomSheet { isBottomSheetExpanded = false }
    }
}

@ExperimentalMaterial3Api
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
        unreadMentionsCount = 2,
        unreadReactionsCount = 3,
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
        unreadCount = 12345,
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
            )
            Spacer(modifier = Modifier.size(2.dp))
            ChatListCell(
                item = data2,
                onClick = { },
            )
            Spacer(modifier = Modifier.size(2.dp))
            ChatListCell(
                item = data,
                onClick = { },
            )

            Spacer(modifier = Modifier.size(16.dp))

            ChatListCell(
                item = data3,
                onClick = { },
            )
        }
    }
}