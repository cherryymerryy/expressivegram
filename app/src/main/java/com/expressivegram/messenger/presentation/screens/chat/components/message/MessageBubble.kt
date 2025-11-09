package com.expressivegram.messenger.presentation.screens.chat.components.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.data.reply.TdReplyTo
import com.expressivegram.messenger.presentation.components.preferences.ListItemPosition
import org.drinkless.tdlib.TdApi

@Composable
fun MessageBubble(
    modifier: Modifier = Modifier,
    text: String?,
    content: TdApi.MessageContent,
    reply: TdReplyTo? = null,
    forward: TdApi.MessageForwardInfo? = null,
    isFromMe: Boolean,
    position: ListItemPosition
) {
    val (backgroundColor, foregroundColor) = messageColors(isFromMe)

    val shape = when (position) {
        ListItemPosition.Single -> MaterialTheme.shapes.medium
        ListItemPosition.Top -> MaterialTheme.shapes.medium.copy(
            bottomStart = MaterialTheme.shapes.extraSmall.bottomStart,
            bottomEnd = MaterialTheme.shapes.extraSmall.bottomEnd
        )

        ListItemPosition.Bottom -> MaterialTheme.shapes.medium.copy(
            topStart = MaterialTheme.shapes.extraSmall.topStart,
            topEnd = MaterialTheme.shapes.extraSmall.topEnd
        )

        ListItemPosition.Middle -> MaterialTheme.shapes.extraSmall
    }

    Column(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .padding(8.dp)
            .defaultMinSize(minWidth = 70.dp)
    ) {
        reply?.let {
            ReplyCell(
                reply = it,
                isFromMe = isFromMe,
                onClick = {  }
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        text?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = foregroundColor
            )
        }
    }
}