package com.expressivegram.messenger.data.chat

import androidx.compose.runtime.Immutable

@Immutable
data class ChatAction(
    val senderId: Long,
    val type: ChatActionType,
    val messageThreadId: Long = 0,
    val progress: Int? = null
)
