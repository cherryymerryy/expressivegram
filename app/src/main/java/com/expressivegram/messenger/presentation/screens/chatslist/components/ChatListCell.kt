package com.expressivegram.messenger.presentation.screens.chatslist.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.presentation.components.profile.ChatPhotoItem
import org.drinkless.tdlib.TdApi

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatListCell(chat: TdApi.Chat) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            item {
                ChatPhotoItem(
                    picture = chat.photo,
                    chatTitle = chat.title
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = chat.title,
                        fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                        fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "message",
                        fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                        fontSize = MaterialTheme.typography.labelLarge.fontSize,
                        fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                        fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                ) {
                    Text("date")

                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null
                    )
                }
            }
        }
    }
}