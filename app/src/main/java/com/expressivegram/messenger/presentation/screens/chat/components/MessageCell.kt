package com.expressivegram.messenger.presentation.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.extensions.execute
import com.expressivegram.messenger.extensions.getMessageContent
import com.expressivegram.messenger.extensions.getSenderId
import com.expressivegram.messenger.extensions.getSenderName
import com.expressivegram.messenger.presentation.components.profile.ChatPhotoItem
import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.utils.UserConfig
import org.drinkless.tdlib.TdApi

@Composable
fun MessageCell(
    message: TdApi.Message
) {
    var user by remember { mutableStateOf<TdApi.User?>(null) }
    var senderName by remember { mutableStateOf("") }

    val instance = TdUtility.getInstance().getClient()

    LaunchedEffect(message.id) {
        user = instance.execute(TdApi.GetUser(message.getSenderId()))
        senderName = message.getSenderName()
    }

    val isMe = message.getSenderId() == (UserConfig.getInstance().getCurrentUser()?.id ?: 0)
    val backgroundColor = if (isMe) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer
    val foregroundColor = if (isMe) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        if (!isMe) {
            ChatPhotoItem(
                name = senderName,
                photo = user?.profilePhoto?.small,
                modifier = Modifier.align(alignment = Alignment.Bottom)
            )
        }

        Spacer(
            modifier = Modifier
                .size(8.dp)
        )

        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(backgroundColor)
                .padding(8.dp)
                .defaultMinSize(minWidth = 100.dp)
        ) {
            Column {
                if (!isMe) {
                    Text(
                        text = senderName,
                        color = foregroundColor,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                Text(
                    text = message.getMessageContent(),
                    color = foregroundColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}