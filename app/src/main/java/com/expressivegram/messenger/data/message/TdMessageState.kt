package com.expressivegram.messenger.data.message

import androidx.compose.runtime.Immutable
import com.expressivegram.messenger.presentation.components.preferences.ListItemPosition

@Immutable
data class TdMessageState(
    val position: ListItemPosition = ListItemPosition.Single,
    val message: TdMessage
)