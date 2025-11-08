package com.expressivegram.messenger.extensions

import com.expressivegram.messenger.data.chat.ChatState
import com.expressivegram.messenger.data.chat.ChatType
import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.utils.UserConfig
import org.drinkless.tdlib.TdApi

suspend fun TdApi.Chat.getChatTitle(): String {
    return when (this.type) {
        is TdApi.ChatTypeBasicGroup -> title
        is TdApi.ChatTypeSupergroup -> title
        is TdApi.ChatTypePrivate -> {
            val instance = TdUtility.getInstance().getClient()
            val user = instance.execute(TdApi.GetUser(id))

            when (user.type) {
                is TdApi.UserTypeRegular -> {
                    if (id == (UserConfig.getInstance().getCurrentUser()?.id ?: 0)) {
                        "Saved Messages"
                    } else {
                        title
                    }
                }
                is TdApi.UserTypeDeleted -> "🗑️ Deleted Account"
                is TdApi.UserTypeUnknown -> "❓ Unknown User"
                else -> title
            }
        }
        is TdApi.ChatTypeSecret -> title
        else -> "Unknown title"
    }
}

fun TdApi.Chat.isDefaultForum(): Boolean {
    return this.viewAsTopics && this.type is TdApi.ChatTypeSupergroup
}

fun TdApi.Chat.isChannel(): Boolean {
    return if (this.type is TdApi.ChatTypeSupergroup)
            (this.type as TdApi.ChatTypeSupergroup).isChannel
    else
        false
}

suspend fun TdApi.Chat.getLastMessageText(): String {
    val text = this.lastMessage?.getMessageContent() ?: "❓ Unsupported message content"
    val author = when (this.type) {
        is TdApi.ChatTypeSupergroup -> {
            if (this.isChannel()) {
                ""
            } else {
                this.lastMessage?.getSenderName()
            }
        }
        is TdApi.ChatTypeSecret -> ""
        is TdApi.ChatTypePrivate -> ""
        else -> this.lastMessage?.getSenderName()
    }

    return author + (if ((author ?: "").isNotEmpty()) ": " else "") + text
}

suspend fun TdApi.Chat.toChatType(): ChatType {
    return when (this.type) {
        is TdApi.ChatTypeSecret -> ChatType.Secret
        is TdApi.ChatTypePrivate -> ChatType.Private
        is TdApi.ChatTypeBasicGroup -> ChatType.Group
        is TdApi.ChatTypeSupergroup -> {
            if ((this.type as TdApi.ChatTypeSupergroup).isChannel) {
                ChatType.Channel
            } else if (this.isDefaultForum()) {
                ChatType.Forum
            } else {
                val client = TdUtility.getInstance().getClient()
                val supergroupId = (this.type as TdApi.ChatTypeSupergroup).supergroupId
                val supergroup = client.execute(TdApi.GetSupergroup(supergroupId))
                if (supergroup.isForum) {
                    ChatType.Forum
                } else {
                    ChatType.Group
                }
            }
        }
        else -> ChatType.Unknown

    }
}

suspend fun TdApi.Chat.toChatState(): ChatState {
    val client = TdUtility.getInstance().getClient()
    val myId = UserConfig.getInstance().getClientUserId()
    return ChatState(
        id = id,
        title = this.getChatTitle(),
        type = this.toChatType(),
        photo = photo?.small,
        actions = mutableListOf(),
        permissions = permissions,
        emojiStatus = emojiStatus,
        background = background,
        messageSenderId = messageSenderId,
        membersCount = when (type) {
            is TdApi.ChatTypeBasicGroup -> {
                val basicGroupId = (type as TdApi.ChatTypeBasicGroup).basicGroupId
                val basicGroup = client.execute(TdApi.GetBasicGroup(basicGroupId))
                basicGroup.memberCount
            }
            is TdApi.ChatTypeSupergroup -> {
                val supergroupId = (type as TdApi.ChatTypeSupergroup).supergroupId
                val supergroup = client.execute(TdApi.GetSupergroup(supergroupId))
                supergroup.memberCount
            }
            else -> 0
        },
        userStatus = when (type) {
            is TdApi.ChatTypePrivate -> {
                val user = client.execute(TdApi.GetUser(id))
                user.status
            }
            else -> null
        },
        memberStatus = when (type) {
            is TdApi.ChatTypeSupergroup -> {
                val supergroupId = (type as TdApi.ChatTypeSupergroup).supergroupId
                val supergroupFullInfo = client.execute(TdApi.GetSupergroupFullInfo(supergroupId))
                if (supergroupFullInfo.canGetMembers) {
                    client.execute(
                        TdApi.GetChatMember(
                            id,
                            if (messageSenderId != null) messageSenderId else TdApi.MessageSenderUser(myId)
                        )
                    ).status
                } else {
                    null
                }
            }
            else -> null
        },
        hasForumTabs = when (type) {
            is TdApi.ChatTypeSupergroup -> {
                val supergroupId = (type as TdApi.ChatTypeSupergroup).supergroupId
                val supergroupFullInfo = client.execute(TdApi.GetSupergroup(supergroupId))
                supergroupFullInfo.hasForumTabs
            }
            else -> false
        }
    )
}

fun ChatType.isGroup(): Boolean {
    return this == ChatType.Group || this == ChatType.Forum
}

fun ChatType.isPrivate(): Boolean {
    return this == ChatType.Private || this == ChatType.Secret
}