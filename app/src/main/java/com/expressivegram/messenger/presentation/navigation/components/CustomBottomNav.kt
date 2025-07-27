package com.expressivegram.messenger.presentation.navigation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.expressivegram.messenger.presentation.navigation.NavConstants
import com.expressivegram.messenger.presentation.navigation.Route
import com.expressivegram.messenger.presentation.navigation.navigationItems

@Composable
fun CustomBottomNav(
    navController: NavHostController,
    currentRoute: Route?
) {
    NavigationBar {
        navigationItems.forEach { item ->
            val isSelected = currentRoute?.path == item.route.path

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (isSelected) {
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set(NavConstants.KEY_FOCUS_SEARCH_BAR, true)
                        }
                    } else {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.label),
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.label),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }
}