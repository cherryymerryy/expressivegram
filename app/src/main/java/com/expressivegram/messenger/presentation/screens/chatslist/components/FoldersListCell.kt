package com.expressivegram.messenger.presentation.screens.chatslist.components

import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import org.drinkless.tdlib.TdApi

@Composable
fun FoldersListCell(folders: List<TdApi.ChatFolderInfo?>?, onClick: (Int) -> Unit) {
    if (folders == null) {
        return
    }

    var selectedIndex by remember { mutableIntStateOf(0) }
    val fullFolders = folders.toMutableList()
    fullFolders.add(0, null)

    PrimaryTabRow(
        selectedTabIndex = selectedIndex
    ) {
        fullFolders.forEachIndexed { index, info ->
            val isSelected = index == selectedIndex
            Tab(
                onClick = {
                    if (isSelected) {
                        return@Tab
                    }

                    selectedIndex = index
                    onClick(selectedIndex)
                },
                selected = isSelected,
                text = {
                    Text(
                        text = if (info == null) "All chats" else info.name.text.text,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}