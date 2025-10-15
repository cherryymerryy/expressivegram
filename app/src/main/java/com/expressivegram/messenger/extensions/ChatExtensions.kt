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