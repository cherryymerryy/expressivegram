package com.expressivegram.messenger.data

import androidx.compose.runtime.Immutable
import org.drinkless.tdlib.TdApi

@Immutable
data class ChatListItemState(
    val chatId: Long,
    var title: String,
    var lastMessageText: String,
    var unreadCount: Int,
    val photo: TdApi.File?,
    val isForum: Boolean,
    var lastForumTopicName: String? = null
)