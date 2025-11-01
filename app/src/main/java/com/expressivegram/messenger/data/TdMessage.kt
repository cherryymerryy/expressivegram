package com.expressivegram.messenger.data

import org.drinkless.tdlib.TdApi
import java.time.LocalDate

data class TdMessage(
    val id: Long,
    val sender: TdApi.MessageSender,
    val content: TdApi.InputMessageContent,
    val sentDate: LocalDate,
    val text: String? = null,
    val reply: TdMessage? = null
)