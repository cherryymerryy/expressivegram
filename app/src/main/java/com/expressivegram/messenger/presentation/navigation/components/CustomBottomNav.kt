package com.expressivegram.messenger.presentation.navigation.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.expressivegram.messenger.presentation.navigation.CallsList
import com.expressivegram.messenger.presentation.navigation.ChatsList
import com.expressivegram.messenger.presentation.navigation.ContactsList
import com.expressivegram.messenger.presentation.navigation.Settings
import com.expressivegram.messenger.presentation.navigation.TopLevelBackStack

@Composable
fun CustomBottomNav(
    topLevelBackStack: TopLevelBackStack<NavKey>
) {
    val navigationItems = listOf(ContactsList, CallsList, ChatsList, Settings)

    NavigationBar(modifier = Modifier.height(84.dp)) {
        navigationItems.forEach { item ->
            val isSelected = topLevelBackStack.topLevelKey == item

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    topLevelBackStack.switchTopLevel(item)
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.icon,
                        contentDescription = item.title,
                    )
                }
            )
        }
    }
}