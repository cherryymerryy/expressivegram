package com.expressivegram.messenger.data.reply

import androidx.compose.runtime.Stable
import org.drinkless.tdlib.TdApi

@Stable
data class TdReplyToMessage(
    override val senderId: Long,
    override val senderName: String,
    val chatId: Long,
    val messageId: Long,
    val content: TdApi.MessageContent? = null,
    val quote: TdApi.TextQuote? = null,
    val origin: TdMessageOrigin? = null,
    val originSendDate: Int = 0
) : TdReplyTo(senderId, senderName)
