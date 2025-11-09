package com.expressivegram.messenger.utils

import com.expressivegram.messenger.extensions.send
import org.drinkless.tdlib.TdApi

object SendMessageHelper {
    fun sendMessage(chatId: Long, messageThreadId: Long? = 0, text: String) {
        val client = TdUtility.getInstance().getClient()
        val req = TdApi.SendMessage(
            chatId,
            messageThreadId ?: 0L,
            null,
            null,
            null,
            TdApi.InputMessageText(
                TdApi.FormattedText(
                    text,
                    emptyArray<TdApi.TextEntity>()
                ),
                null,
                true
            )
        )
        client.send(req)
    }
}