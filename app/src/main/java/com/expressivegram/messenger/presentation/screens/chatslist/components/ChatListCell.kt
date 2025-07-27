package com.expressivegram.messenger.presentation.screens.chatslist.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.presentation.components.profile.ChatPhotoItem
import org.drinkless.tdlib.TdApi

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatListCell(
    chat: TdApi.Chat,
    onClick: () -> Unit
) {
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
            // Раскомментируйте и используйте ChatPhotoItem
            // ChatPhotoItem(
            //     picture = chat.photo,
            //     chatTitle = chat.title
            // )

            // Spacer(modifier = Modifier.width(8.dp))

            // Основная информация о чате
            Column(
                modifier = Modifier.weight(1f) // Занимает всё доступное пространство
            ) {
                Text(
                    text = chat.title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1 // Ограничение в одну строку, чтобы избежать переносов
                )
                Text(
                    text = "message", // Здесь должно быть реальное последнее сообщение
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Дата и статус
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text("date") // Реальная дата
                // Icon(...)
            }
        }
    }
}