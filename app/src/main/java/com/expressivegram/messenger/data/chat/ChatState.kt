package com.expressivegram.messenger.data.chat

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import org.drinkless.tdlib.TdApi

@Immutable
data class ChatState(
    val id: Long,
    val title: String,
    val type: ChatType,
    val photo: TdApi.File? = null,
    val actions: List<ChatAction>,
    val actionBar: TdApi.ChatActionBar? = null,
    val memberStatus: TdApi.ChatMemberStatus?,
    val permissions: TdApi.ChatPermissions,
    val emojiStatus: TdApi.EmojiStatus? = null,
    val background: TdApi.ChatBackground? = null,
    val messageSenderId: TdApi.MessageSender? = null,
    val membersCount: Int = 0,
    var onlineMembersCount: Int = 0,
    var userStatus: TdApi.UserStatus? = null,
    val firstUnreadMessageId: Long? = null,
    val hasForumTabs: Boolean = false,
    val clientData: String? = null
)
