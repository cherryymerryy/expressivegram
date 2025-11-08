package com.expressivegram.messenger.data.reply

import androidx.compose.runtime.Immutable

@Immutable
data class TdReplyToUnknown(
    val constructorId: Int
) : TdReplyTo(0, "Unknown")
