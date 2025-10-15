package com.expressivegram.messenger.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Main : NavKey

@Serializable
data object CallsList : NavKey, BottomNavItem {
    override val icon: ImageVector = Icons.Outlined.Call
    override val title: String = "Calls List"
}

@Serializable
data object ChatsList : NavKey, BottomNavItem {
    override val icon: ImageVector = Icons.Outlined.ChatBubbleOutline
    override val title: String = "Chats List"
}

@Serializable
data object Settings : NavKey, BottomNavItem {
    override val icon: ImageVector = Icons.Outlined.Settings
    override val title: String = "Settings"
}

@Serializable
data class Chat(val id: Long) : NavKey