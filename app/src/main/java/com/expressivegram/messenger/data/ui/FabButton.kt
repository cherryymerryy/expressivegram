package com.expressivegram.messenger.data.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class FabButton(
    val text: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)