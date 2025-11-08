package com.expressivegram.messenger.data.reply

import androidx.compose.runtime.Stable

@Stable
data class TdReplyToStory(
    override val senderId: Long,
    override val senderName: String,
    val storyId: Int
) : TdReplyTo(senderId, senderName)
