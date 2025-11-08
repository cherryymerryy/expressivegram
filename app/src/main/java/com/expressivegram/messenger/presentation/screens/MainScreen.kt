package com.expressivegram.messenger.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.expressivegram.messenger.data.ui.FabButton
import com.expressivegram.messenger.presentation.components.FloatingActionMenu
import com.expressivegram.messenger.presentation.navigation.CallsList
import com.expressivegram.messenger.presentation.navigation.Chat
import com.expressivegram.messenger.presentation.navigation.ChatsList
import com.expressivegram.messenger.presentation.navigation.ContactsList
import com.expressivegram.messenger.presentation.navigation.Main
import com.expressivegram.messenger.presentation.navigation.Settings
import com.expressivegram.messenger.presentation.navigation.TopLevelBackStack
import com.expressivegram.messenger.presentation.navigation.components.CustomBottomNav
import com.expressivegram.messenger.presentation.navigation.components.CustomTopBar
import com.expressivegram.messenger.presentation.screens.callslist.CallsListScreen
import com.expressivegram.messenger.presentation.screens.chat.ChatScreen
import com.expressivegram.messenger.presentation.screens.chatslist.ChatListScreen
import com.expressivegram.messenger.presentation.screens.settings.SettingsScreen

@ExperimentalMaterial3ExpressiveApi
@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(mainTopLevelBackStack: TopLevelBackStack<NavKey>) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
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
        floatingActionButton = {
            FloatingActionMenu(
                expandIcon = Icons.Filled.Add,
                closeIcon = Icons.Filled.Close,
                buttons = listOf(
                    FabButton(
                        text = "New group",
                        icon = Icons.Outlined.Group,
                        onClick = { }
                    ),
                    FabButton(
                        text = "New channel",
                        icon = Icons.Outlined.Groups,
                        onClick = { }
                    ),
                    FabButton(
                        text = "Post story",
                        icon = Icons.Outlined.CameraAlt,
                        onClick = { }
                    )
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
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
                entry<ContactsList> {
                    CallsListScreen()
                }
                entry<CallsList> {
                    CallsListScreen()
                }
                entry<ChatsList> {
                    ChatListScreen(
                        onChatClick = { id ->
                            mainTopLevelBackStack.add(Chat(id))
                        }
                    )
                }
                entry<Settings> {
                    SettingsScreen()
                }
                entry<Chat> { args ->
                    ChatScreen(
                        chatId = args.id,
                        onBackClick = mainTopLevelBackStack::removeLast
                    )
                }
            }
        )
    }
}

@ExperimentalMaterial3ExpressiveApi
@ExperimentalMaterial3Api
@Preview
@Composable
fun TestMainScreen() {
    MainScreen(TopLevelBackStack(Main))
}