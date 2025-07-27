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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.expressivegram.messenger.presentation.navigation.Route
import com.expressivegram.messenger.presentation.navigation.components.CustomBottomNav
import com.expressivegram.messenger.presentation.navigation.components.CustomTopBar
import com.expressivegram.messenger.presentation.screens.callslist.CallsList
import com.expressivegram.messenger.presentation.screens.chat.ChatScreen
import com.expressivegram.messenger.presentation.screens.chatslist.ChatList
import com.expressivegram.messenger.presentation.screens.settings.SettingsScreen
import org.drinkless.tdlib.TdApi

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val localNavController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.let { Route.fromPath(it) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopBar(
                currentRoute = currentRoute,
                onNavigateBack = { navController.popBackStack() },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            CustomBottomNav(
                navController = localNavController,
                currentRoute = currentRoute
            )
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
        NavHost(
            navController = localNavController,
            startDestination = Route.ChatsList,
            modifier = Modifier.padding(ip)
        ) {
            composable<Route.CallsList> {
                CallsList()
            }
            composable<Route.ChatsList> {
                ChatList(
                    chatList = TdApi.ChatListMain(),
                    navController = navController
                )
            }
            composable<Route.Settings> {
                SettingsScreen()
            }
        }
    }
}