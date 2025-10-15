package com.expressivegram.messenger.presentation.screens.chatslist.components

import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.extensions.getChatTitle
import com.expressivegram.messenger.extensions.getMessageContent
import org.drinkless.tdlib.TdApi

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatListCell(
    chat: TdApi.Chat,
    onClick: () -> Unit
) {
    var title by remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit) {
        title = chat.getChatTitle()
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

             Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = chat.lastMessage?.getMessageContent() ?: "❓ Unsupported message content",
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text("date")
            }
        }
    }
}