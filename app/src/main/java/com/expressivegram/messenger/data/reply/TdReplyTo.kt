package com.expressivegram.messenger.data.reply

sealed class TdReplyTo(
    open val senderId: Long,
    open val senderName: String
)
