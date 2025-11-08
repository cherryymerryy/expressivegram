package com.expressivegram.messenger.presentation.screens.chat.components.forum

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GeneratingTokens
import androidx.compose.material.icons.filled.Tab
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.drinkless.tdlib.TdApi

@Composable
fun ForumTopicTab(
    topic: TdApi.ForumTopic,
    onClick: (Long) -> Unit,
    isVerticalMode: Boolean,
    isSelected: Boolean,
    isPinned: Boolean
) {
    val topicName = remember {
        @Composable {
            Text(
                text = topic.info.name,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

    val icon = remember {
        @Composable {
            if (topic.info.isGeneral) {
                Icon(
                    imageVector = Icons.Filled.GeneratingTokens,
                    contentDescription = "forum_topic_tab_general_${topic.info.messageThreadId}"
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Tab,
                    contentDescription = "forum_topic_tab_general_${topic.info.messageThreadId}"
                )
            }
        }
    }

    val modifier = Modifier
        .padding(4.dp)
        .background(
            color = when {
                isSelected -> MaterialTheme.colorScheme.surfaceContainer
                isPinned -> MaterialTheme.colorScheme.surfaceContainerHigh
                else -> Color.Transparent
            }
        )
        .clickable(enabled = true) { onClick(topic.lastMessage?.id ?: 0) }

    if (isVerticalMode) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()
            topicName()
        }
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            icon()
            topicName()
        }
    }
}