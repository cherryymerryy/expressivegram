package com.expressivegram.messenger.presentation.navigation

sealed class Route(val path: String) {
    data object Calls : Route("calls")
    data object Chats : Route("chats")
    data object Settings : Route("settings")

    companion object {
        fun fromPath(route: String?): Route? = when (route) {
            Calls.path -> Calls
            Chats.path -> Chats
            Settings.path -> Settings
            else -> null
        }
    }
}