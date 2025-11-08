package com.expressivegram.messenger.presentation.screens.chat.components.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.data.chat.ChatType
import com.expressivegram.messenger.data.message.TdMessageState
import com.expressivegram.messenger.presentation.components.preferences.ListItemPosition
import com.expressivegram.messenger.presentation.components.profile.ChatPhotoItem

@Composable
fun MessageCell(
    state: TdMessageState
) {
    val message = state.message
    val position = state.position

    val shape = when (position) {
        ListItemPosition.Single -> MaterialTheme.shapes.small
        ListItemPosition.Top -> MaterialTheme.shapes.small.copy(
            bottomStart = MaterialTheme.shapes.extraSmall.bottomStart,
            bottomEnd = MaterialTheme.shapes.extraSmall.bottomEnd
        )

        ListItemPosition.Bottom -> MaterialTheme.shapes.small.copy(
            topStart = MaterialTheme.shapes.extraSmall.topStart,
            topEnd = MaterialTheme.shapes.extraSmall.topEnd
        )

        ListItemPosition.Middle -> MaterialTheme.shapes.extraSmall
    }

    val backgroundColor = if (message.isFromMe) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer
    val foregroundColor = if (message.isFromMe) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface

    val horizontalArrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start
    val horizontalAlignment = if (message.isFromMe) Alignment.End else Alignment.Start

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement
    ) {
        if (!message.isFromMe && (position == ListItemPosition.Bottom || position == ListItemPosition.Single)) {
            ChatPhotoItem(
                name = message.senderName,
                photo = message.senderPhoto,
                chatType = ChatType.Private,
                modifier = Modifier.align(alignment = Alignment.Bottom)
            )
        } else if (!message.isFromMe) {
            Spacer(
                modifier = Modifier.width(54.dp)
            )
        }

        Spacer(
            modifier = Modifier
                .size(8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.Bottom)
        ) {
            Row(
                modifier = Modifier
                    .align(alignment = horizontalAlignment)
            ) {
                if (!message.isFromMe) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = message.senderName,
                        textAlign = TextAlign.Start,
                        color = foregroundColor,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                Text(
                    modifier = Modifier.weight(1f),
                    text = "date",
                    textAlign = TextAlign.End,
                    color = foregroundColor,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Column(
                modifier = Modifier
                    .clip(shape)
                    .background(backgroundColor)
                    .padding(8.dp)
                    .defaultMinSize(minWidth = 70.dp)
                    .align(alignment = horizontalAlignment)
            ) {
                message.reply?.let {
                    ReplyCell(
                        reply = message.reply,
                        isFromMe = message.isFromMe,
                        onClick = {  },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

                message.text?.let {
                    Text(
                        text = it,
                        color = foregroundColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}