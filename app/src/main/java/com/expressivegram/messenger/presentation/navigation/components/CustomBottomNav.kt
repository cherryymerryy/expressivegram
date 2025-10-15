package com.expressivegram.messenger.presentation.navigation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import com.expressivegram.messenger.presentation.navigation.CallsList
import com.expressivegram.messenger.presentation.navigation.ChatsList
import com.expressivegram.messenger.presentation.navigation.Settings
import com.expressivegram.messenger.presentation.navigation.TopLevelBackStack

@Composable
fun CustomBottomNav(
    topLevelBackStack: TopLevelBackStack<NavKey>
) {
    val navigationItems = listOf(CallsList, ChatsList, Settings)

    NavigationBar {
        navigationItems.forEach { item ->
            val isSelected = topLevelBackStack.topLevelKey == item

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    topLevelBackStack.switchTopLevel(item)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }
}