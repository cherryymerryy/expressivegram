package com.expressivegram.messenger.presentation.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.expressivegram.messenger.presentation.navigation.Route
import com.expressivegram.messenger.presentation.screens.chat.ChatScreen

@Composable
fun ApplicationNavigation(startDestination: String) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Route.Main.path) {
            MainScreen(navController)
        }
        composable(
            route = Route.Chat.path,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val chatId: Long? = backStackEntry.arguments?.getLong("id")
            if (chatId != null) {
                ChatScreen(chatId = chatId)
            }
        }
    }
}