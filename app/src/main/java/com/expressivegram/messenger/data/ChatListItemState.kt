package com.expressivegram.messenger.data

import androidx.compose.runtime.Immutable
import org.drinkless.tdlib.TdApi
import java.time.LocalDate

@Immutable
data class ChatListItemState(
    val chatId: Long,
    var title: String,
    val chatType: ChatType,
    var lastMessageText: String,
    var unreadCount: Int,
    val photo: TdApi.File?,
    val isForum: Boolean,
    var lastForumTopicName: String? = null,
    val isFromMe: Boolean,
    val isViewed: Boolean,
    val sentDate: LocalDate? = null
)