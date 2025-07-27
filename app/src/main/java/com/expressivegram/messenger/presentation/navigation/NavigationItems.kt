package com.expressivegram.messenger.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.expressivegram.messenger.R

data class NavigationItem(
    val route: Route,
    val icon: ImageVector,
    @param:StringRes val label: Int
)

val navigationItems = listOf(
    NavigationItem(
        route = Route.CallsList,
        icon = Icons.Outlined.Call,
        label = R.string.nav_calls
    ),
    NavigationItem(
        route = Route.ChatsList,
        icon = Icons.Outlined.ChatBubble,
        label = R.string.nav_chats
    ),
    NavigationItem(
        route = Route.Settings,
        icon = Icons.Outlined.Settings,
        label = R.string.nav_settings
    )
)