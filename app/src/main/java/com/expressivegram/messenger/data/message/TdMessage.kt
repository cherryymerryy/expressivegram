package com.expressivegram.messenger.data.message

import androidx.compose.runtime.Stable
import com.expressivegram.messenger.data.reply.TdReplyTo
import org.drinkless.tdlib.TdApi
import java.util.Date

@Stable
data class TdMessage(
    val id: Long,
    val senderName: String,
    val senderObject: TdApi.MessageSender,
    val content: TdApi.MessageContent,
    val sentDate: Date,
    val text: String? = null,
    val reply: TdReplyTo? = null,
    val isFromMe: Boolean,
    val senderPhoto: TdApi.File? = null,
    val mediaAlbumId: Long = 0L
)