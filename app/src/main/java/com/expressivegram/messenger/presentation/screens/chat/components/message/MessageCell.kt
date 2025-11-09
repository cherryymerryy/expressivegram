package com.expressivegram.messenger.presentation.screens.chat.components.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.data.chat.ChatType
import com.expressivegram.messenger.data.message.TdMessageState
import com.expressivegram.messenger.presentation.components.preferences.ListItemPosition
import com.expressivegram.messenger.presentation.components.profile.ChatPhotoItem

@ExperimentalMaterial3ExpressiveApi
@Composable
fun MessageCell(
    state: TdMessageState
) {
    val message = state.message
    val position = state.position

    val arrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start
    val alignment = if (message.isFromMe) Alignment.End else Alignment.Start
    val isNotMidPosition = position == ListItemPosition.Top || position == ListItemPosition.Single

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = arrangement
    ) {
        if (!message.isFromMe && (position == ListItemPosition.Bottom || position == ListItemPosition.Single)) {
            ChatPhotoItem(
                name = message.senderName,
                photo = message.senderPhoto,
                chatType = ChatType.Private,
                modifier = Modifier.align(alignment = Alignment.Bottom)
            )
        } else if (position == ListItemPosition.Top || position == ListItemPosition.Middle) {
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
            if (isNotMidPosition) {
                MessageHeader(
                    modifier = Modifier.align(alignment),
                    senderName = message.senderName,
                    isFromMe = message.isFromMe
                )
            }

            MessageBubble(
                modifier = Modifier.align(alignment),
                text = message.text,
                content = message.content,
                reply = message.reply,
                forward = null,
                isFromMe = message.isFromMe,
                position = position
            )
        }
    }
}