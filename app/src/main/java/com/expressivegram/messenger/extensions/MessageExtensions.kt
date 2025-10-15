package com.expressivegram.messenger.extensions

import org.drinkless.tdlib.TdApi

fun TdApi.Message.getMessageContent(): String {
    return when (this.content) {
        is TdApi.MessageText -> (content as TdApi.MessageText).text.text
        else -> "❓ Unsupported message content"
    }
}

fun TdApi.Message.getSenderId(): Long {
    return when (this.senderId) {
        is TdApi.MessageSenderChat -> (senderId as TdApi.MessageSenderChat).chatId
        is TdApi.MessageSenderUser -> (senderId as TdApi.MessageSenderUser).userId
        else -> chatId
    }
}