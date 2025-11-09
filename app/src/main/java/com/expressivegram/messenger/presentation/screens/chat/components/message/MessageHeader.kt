package com.expressivegram.messenger.presentation.screens.chat.components.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MessageHeader(
    modifier: Modifier = Modifier,
    senderName: String,
    isFromMe: Boolean,
) {
    val foregroundColor = messageColors(isFromMe).second

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (!isFromMe) {
            Text(
                text = senderName,
                textAlign = TextAlign.Start,
                color = foregroundColor,
                style = MaterialTheme.typography.titleSmall
            )
        }

        Text(
            text = "date",
            textAlign = TextAlign.End,
            color = foregroundColor,
            style = MaterialTheme.typography.titleSmall
        )
    }
}