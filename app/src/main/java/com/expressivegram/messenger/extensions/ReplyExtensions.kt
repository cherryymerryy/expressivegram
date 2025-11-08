package com.expressivegram.messenger.extensions

import com.expressivegram.messenger.data.reply.TdMessageOrigin
import com.expressivegram.messenger.data.reply.TdReplyTo
import com.expressivegram.messenger.data.reply.TdReplyToMessage
import com.expressivegram.messenger.data.reply.TdReplyToStory
import com.expressivegram.messenger.data.reply.TdReplyToUnknown
import org.drinkless.tdlib.TdApi

fun TdApi.MessageReplyTo.toTdReply(): TdReplyTo {
    return when (this) {
        is TdApi.MessageReplyToMessage -> {
            TdReplyToMessage(
                senderId = origin?.getOrigin()?.chatId ?: 0,
                senderName = "TODO()",
                chatId = chatId,
                messageId = messageId,
                content = content,
                quote = quote,
                origin = origin?.getOrigin(),
                originSendDate = originSendDate
            )
        }
        is TdApi.MessageReplyToStory -> {
            TdReplyToStory(
                senderId = storyPosterChatId,
                senderName = "TODO()",
                storyId = storyId
            )
        }
        else -> {
            TdReplyToUnknown(this.constructor)
        }
    }
}

fun TdApi.MessageOrigin.getOrigin(): TdMessageOrigin {
    return when (this) {
        is TdApi.MessageOriginChat -> {
            TdMessageOrigin(
                chatId = senderChatId,
                name = authorSignature
            )
        }
        is TdApi.MessageOriginChannel -> {
            TdMessageOrigin(
                chatId = chatId,
                messageId = messageId,
                name = authorSignature
            )
        }
        is TdApi.MessageOriginUser -> {
            TdMessageOrigin(
                chatId = senderUserId
            )
        }
        is TdApi.MessageOriginHiddenUser -> {
            TdMessageOrigin(
                name = senderName
            )
        }
        else -> TdMessageOrigin()
    }
}