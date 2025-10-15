package com.expressivegram.messenger.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.expressivegram.messenger.presentation.navigation.CallsList
import com.expressivegram.messenger.presentation.navigation.Chat
import com.expressivegram.messenger.presentation.navigation.ChatsList
import com.expressivegram.messenger.presentation.navigation.Settings
import com.expressivegram.messenger.presentation.navigation.TopLevelBackStack
import com.expressivegram.messenger.presentation.navigation.components.CustomBottomNav
import com.expressivegram.messenger.presentation.navigation.components.CustomTopBar
import com.expressivegram.messenger.presentation.screens.callslist.CallsListScreen
import com.expressivegram.messenger.presentation.screens.chat.ChatScreen
import com.expressivegram.messenger.presentation.screens.chatslist.ChatListScreen
import com.expressivegram.messenger.presentation.screens.settings.SettingsScreen
import org.drinkless.tdlib.TdApi

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val topLevelBackStack = remember { TopLevelBackStack<NavKey>(ChatsList) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopBar()
        },
        bottomBar = {
            CustomBottomNav(topLevelBackStack)
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier,
                snackbar = {
                    Snackbar(
                        snackbarData = it,
                        modifier = Modifier,
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { ip ->
        NavDisplay(
            modifier = Modifier.padding(ip),
            backStack = topLevelBackStack.backStack,
            onBack = { topLevelBackStack.removeLast() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<CallsList> {
                    CallsListScreen()
                }
                entry<ChatsList> {
                    ChatListScreen(
                        chatList = TdApi.ChatListMain(),
                        onChatClick = { id ->
                            topLevelBackStack.add(Chat(id))
                        }
                    )
                }
                entry<Settings> {
                    SettingsScreen()
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
}