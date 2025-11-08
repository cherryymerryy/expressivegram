package com.expressivegram.messenger.presentation.screens.chat.components.message

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.data.reply.TdReplyTo
import com.expressivegram.messenger.data.reply.TdReplyToMessage
import com.expressivegram.messenger.data.reply.TdReplyToStory
import com.expressivegram.messenger.data.reply.TdReplyToUnknown
import com.expressivegram.messenger.presentation.theme.ExpressivegramTheme
import org.drinkless.tdlib.TdApi

@Composable
fun ReplyCell(
    reply: TdReplyTo,
    isFromMe: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val topTextFont = typography.titleSmall
    val bottomTextFont = typography.bodySmall

    val background = if (isFromMe) colorScheme.secondary else colorScheme.surfaceContainerHigh
    val foreground = if (isFromMe) colorScheme.onSecondary else colorScheme.onSurface

    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(background)
            .clickable(true) { onClick }
            .padding(4.dp)
    ) {
        Text(
            text = reply.senderName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            style = topTextFont,
            color = foreground
        )
        Text(
            text = when (reply) {
                is TdReplyToMessage -> {
                    reply.senderName
                }
                is TdReplyToStory -> "Story"
                is TdReplyToUnknown -> "Unknown"
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            style = bottomTextFont,
            color = foreground
        )
    }
}

@Preview(name = "reply_cell_default")
@Preview(name = "reply_cell_night_mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TestReplyCell() {
    val data = TdReplyToMessage(
        senderId = 0,
        senderName = "my bro",
        chatId = -100,
        messageId = 0,
        content = TdApi.MessageText(
            TdApi.FormattedText(
                "my real cool text",
                emptyArray<TdApi.TextEntity>()
            ),
            null,
            null
        ),
        quote = null,
        origin = null,
        originSendDate = 0,
    )

    ExpressivegramTheme {
        ReplyCell(
            reply = data,
            isFromMe = false,
            onClick = {  }
        )
    }
}