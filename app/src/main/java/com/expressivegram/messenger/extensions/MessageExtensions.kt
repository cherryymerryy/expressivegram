package com.expressivegram.messenger.extensions

import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.utils.UserConfig
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

suspend fun TdApi.Message.getSenderName(): String {
    val instance = TdUtility.getInstance().getClient()
    return when (this.senderId) {
        is TdApi.MessageSenderChat -> {
            val id = (senderId as TdApi.MessageSenderChat).chatId
            val chat = instance.execute(TdApi.GetChat(id))
            if (chat.isChannel()) "" else chat.getChatTitle()
        }

        is TdApi.MessageSenderUser -> {
            val id = (senderId as TdApi.MessageSenderUser).userId
            val chat = instance.execute(TdApi.GetChat(chatId))

            if (id == UserConfig.getInstance().getCurrentUser()?.id) {
                "You"
            }
            else if (chat.type is TdApi.ChatTypePrivate || chat.type is TdApi.ChatTypeSecret) {
                ""
            } else {
                val user = instance.execute(TdApi.GetUser(id))
                user.firstName + (if (user.lastName.isNotEmpty()) " ${user.lastName}" else "")
            }
        }

        else -> "$chatId"
    }
}