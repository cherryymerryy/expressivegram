package com.expressivegram.messenger.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.expressivegram.messenger.R

data class NavigationItem(
    val route: String,
    val icon: ImageVector,
    @param:StringRes val label: Int
)

val navigationItems = listOf(
    NavigationItem(
        route = Route.Calls.path,
        icon = Icons.Outlined.Call,
        label = R.string.nav_calls
    ),
    NavigationItem(
        route = Route.Chats.path,
        icon = Icons.Outlined.ChatBubble,
        label = R.string.nav_chats
    ),
    NavigationItem(
        route = Route.Settings.path,
        icon = Icons.Outlined.Settings,
        label = R.string.nav_settings
    )
)