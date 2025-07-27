// presentation/navigation/Route.kt

package com.expressivegram.messenger.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val path: String) {
    @Serializable
    data object Main : Route("main")

    @Serializable
    data object CallsList : Route("calls_list")

    @Serializable
    data object ChatsList : Route("chats_list")

    @Serializable
    data object Settings : Route("settings")

    @Serializable
    data object Chat : Route("chat/{id}")

    companion object {
        fun fromPath(route: String?): Route? = when (route) {
            Main.path -> Main
            CallsList.path -> CallsList
            ChatsList.path -> ChatsList
            Settings.path -> Settings
            Chat.path -> Chat
            else -> null
        }
    }
}