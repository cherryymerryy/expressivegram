package com.expressivegram.messenger.presentation.screens.chatslist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.drinkless.tdlib.TdApi

@Composable
fun FoldersListCell(folders: List<TdApi.ChatFolderInfo?>?, onClick: (Int) -> Unit) {
    if (folders == null) {
        return
    }

    var selectedIndex by remember { mutableIntStateOf(0) }
    val fullFolders = folders.toMutableList()
    fullFolders.add(0, null)

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(fullFolders) { index, info ->
            val isSelected = index == selectedIndex

            AssistChip(
                enabled = !isSelected,
                onClick = {
                    if (isSelected) {
                        return@AssistChip
                    }

                    selectedIndex = index
                    onClick(selectedIndex)
                },
                label = {
                    Text(
                        text = info?.name?.text?.text ?: "All chats",
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}