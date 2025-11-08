package com.expressivegram.messenger.data.chatlist

import androidx.compose.runtime.Immutable
import com.expressivegram.messenger.data.chat.ChatType
import org.drinkless.tdlib.TdApi
import java.util.Date

@Immutable
data class ChatListItemState(
    val chatId: Long,
    var title: String,
    val chatType: ChatType,
    var lastMessageText: String,
    var unreadCount: Int,
    val photo: TdApi.File?,
    var lastForumTopicName: String? = null,
    val isFromMe: Boolean,
    val isViewed: Boolean,
    val sentDate: Date,
    val lastReadOutboxMessageId: Long
)