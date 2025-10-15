package com.expressivegram.messenger.presentation.screens.chat.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.drinkless.tdlib.TdApi

@Composable
fun MessageCell(message: TdApi.Message) {
    Text(message.content.toString())
}