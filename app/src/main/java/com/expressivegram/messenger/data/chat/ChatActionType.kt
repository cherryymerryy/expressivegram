package com.expressivegram.messenger.data.chat

enum class ChatActionType {
    Typing,
    Cancel,
    Unknown,
    RecordingVideo,
    RecordingVideoNote,
    RecordingVoiceNote,
    UploadingVideo,
    UploadingVoiceNote,
    UploadingVideoNote,
    UploadingPhoto,
    UploadingDocument,
    ChoosingSticker,
    ChoosingLocation,
    ChoosingContact,
    StartPlayingGame,
    WatchingAnimations
}