package com.expressivegram.messenger.presentation.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.expressivegram.messenger.presentation.navigation.Chat
import com.expressivegram.messenger.presentation.navigation.Main
import com.expressivegram.messenger.presentation.navigation.TopLevelBackStack
import com.expressivegram.messenger.presentation.screens.chat.ChatScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationNavigation() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val topLevelBackStack = remember { TopLevelBackStack<NavKey>(Main) }

    NavDisplay(
        backStack = topLevelBackStack.backStack,
        onBack = { topLevelBackStack.removeLast() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Main> {
                MainScreen(topLevelBackStack)
            }
            entry<Chat> { args ->
                ChatScreen(
                    chatId = args.id,
                    onBackClick = { topLevelBackStack.removeLast() }
                )
            }
        }
    )
}