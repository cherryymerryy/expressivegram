package com.expressivegram.messenger.presentation.components

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.expressivegram.messenger.data.FabButton

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FloatingActionMenu(
    expandIcon: ImageVector,
    closeIcon: ImageVector,
    buttons: List<FabButton>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    FloatingActionButtonMenu(
        modifier = modifier,
        expanded = expanded,
        button = {
            ToggleFloatingActionButton(
                checked = expanded,
                onCheckedChange = { expanded = it }
            ) {
                Icon(
                    imageVector = if (expanded) closeIcon else expandIcon,
                    contentDescription = null,
                    tint = if (expanded) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
    ) {
        buttons.forEach { item ->
            FloatingActionButtonMenuItem(
                onClick = {
                    item.onClick()
                    expanded = false
                },
                text = {
                    Text(text = item.text)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null
                    )
                }
            )
        }
    }
}