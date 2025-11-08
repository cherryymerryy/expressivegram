package com.expressivegram.messenger.extensions

import com.expressivegram.messenger.data.chat.ChatAction
import com.expressivegram.messenger.data.chat.ChatActionType
import org.drinkless.tdlib.TdApi

fun TdApi.UserStatus.toText(): String {
    return when (this) {
        is TdApi.UserStatusEmpty -> "Empty"
        is TdApi.UserStatusOnline -> "Online"
        is TdApi.UserStatusOffline -> "Offline"
        is TdApi.UserStatusRecently -> "Last seen recently"
        is TdApi.UserStatusLastMonth -> "Last seen last month"
        is TdApi.UserStatusLastWeek -> "Last seen last week"
        else -> "Unknown"
    }
}

fun TdApi.MessageSender.toId(): Long {
    return when (this) {
        is TdApi.MessageSenderChat -> this.chatId
        is TdApi.MessageSenderUser -> this.userId
        else -> 0
    }
}

fun TdApi.ChatAction.toType(): ChatActionType {
    return when (this) {
        is TdApi.ChatActionTyping -> ChatActionType.Typing
        is TdApi.ChatActionCancel -> ChatActionType.Cancel
        is TdApi.ChatActionRecordingVideo -> ChatActionType.RecordingVideo
        is TdApi.ChatActionUploadingVideo -> ChatActionType.UploadingVideo
        is TdApi.ChatActionRecordingVoiceNote -> ChatActionType.RecordingVoiceNote
        is TdApi.ChatActionUploadingVoiceNote -> ChatActionType.UploadingVoiceNote
        is TdApi.ChatActionUploadingPhoto -> ChatActionType.UploadingPhoto
        is TdApi.ChatActionUploadingDocument -> ChatActionType.UploadingDocument
        is TdApi.ChatActionChoosingSticker -> ChatActionType.ChoosingSticker
        is TdApi.ChatActionChoosingLocation -> ChatActionType.ChoosingLocation
        is TdApi.ChatActionChoosingContact -> ChatActionType.ChoosingContact
        is TdApi.ChatActionStartPlayingGame -> ChatActionType.StartPlayingGame
        is TdApi.ChatActionRecordingVideoNote -> ChatActionType.RecordingVideoNote
        is TdApi.ChatActionUploadingVideoNote -> ChatActionType.UploadingVoiceNote
        is TdApi.ChatActionWatchingAnimations -> ChatActionType.WatchingAnimations
        else -> ChatActionType.Unknown
    }
}

fun TdApi.UpdateChatAction.toChatAction(): ChatAction {
    return ChatAction(
        senderId = senderId.toId(),
        type = action.toType(),
        messageThreadId = messageThreadId,
        progress = when (action) {
            is TdApi.ChatActionUploadingVideo -> (action as TdApi.ChatActionUploadingVideo).progress
            is TdApi.ChatActionUploadingVoiceNote -> (action as TdApi.ChatActionUploadingVoiceNote).progress
            is TdApi.ChatActionUploadingPhoto -> (action as TdApi.ChatActionUploadingPhoto).progress
            is TdApi.ChatActionUploadingDocument -> (action as TdApi.ChatActionUploadingDocument).progress
            is TdApi.ChatActionUploadingVideoNote -> (action as TdApi.ChatActionUploadingVideoNote).progress
            else -> null
        }
    )
}

fun TdApi.MessageSender.getSenderId(): Long {
    return when (this) {
        is TdApi.MessageSenderChat -> this.chatId
        is TdApi.MessageSenderUser -> this.userId
        else -> 0
    }
}