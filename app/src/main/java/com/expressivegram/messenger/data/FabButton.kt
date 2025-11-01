package com.expressivegram.messenger.data

import androidx.compose.ui.graphics.vector.ImageVector

data class FabButton(
    val text: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)
