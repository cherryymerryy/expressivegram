package com.expressivegram.messenger.extensions

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

fun TdApi.Chat.isForum(): Boolean {
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