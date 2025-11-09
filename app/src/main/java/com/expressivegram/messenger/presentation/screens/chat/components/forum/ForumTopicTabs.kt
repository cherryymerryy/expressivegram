package com.expressivegram.messenger.presentation.screens.chat.components.forum

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.RotateLeft
import androidx.compose.material3.ElevatedToggleButton
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.drinkless.tdlib.TdApi

@ExperimentalMaterial3ExpressiveApi
@Composable
fun ForumTopicTabs(
    topics: TdApi.ForumTopics?,
    hasForumTabs: Boolean,
    onTabClick: (Long) -> Unit,
    content: @Composable () -> Unit
) {
    if (topics == null) {
        return
    }

    var isVerticalMode by rememberSaveable { mutableStateOf(false) }

    val toggleButton = remember {
        @Composable {
            ElevatedToggleButton(
                checked = isVerticalMode,
                onCheckedChange = { isVerticalMode = it }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.RotateLeft,
                    contentDescription = "toggle_vertical_mode"
                )
            }
        }
    }

    if (isVerticalMode && hasForumTabs) {
        Row {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(0.15f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    toggleButton()
                }

                itemsIndexed(
                    items = topics.topics,
                    key = { _, topic -> topic.info.messageThreadId }
                ) { index, topic ->
                    ForumTopicTab(
                        topic = topic,
                        onClick = onTabClick,
                        isVerticalMode = isVerticalMode,
                        isSelected = topics.topics[index].info.messageThreadId == topic.info.messageThreadId,
                        isPinned = topic.isPinned
                    )
                }
            }
            content()
        }
    } else {
        Column {
            LazyRow(
                modifier = Modifier.fillMaxWidth().height(52.dp).padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (hasForumTabs) {
                    item {
                        toggleButton()
                    }
                }

                itemsIndexed(
                    items = topics.topics,
                    key = { _, topic -> topic.info.messageThreadId }
                ) { index, topic ->
                    ForumTopicTab(
                        topic = topic,
                        onClick = onTabClick,
                        isVerticalMode = isVerticalMode,
                        isSelected = topics.topics[index].info.messageThreadId == topic.info.messageThreadId,
                        isPinned = topic.isPinned
                    )
                }
            }
            content()
        }
    }
}