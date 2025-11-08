package com.expressivegram.messenger.data.reply

import androidx.compose.runtime.Immutable

@Immutable
data class TdMessageOrigin(
    val chatId: Long = 0,
    val messageId: Long = 0,
    val name: String? = null
)
