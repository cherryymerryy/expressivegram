package com.expressivegram.messenger.presentation.screens.chat.components.forum

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GeneratingTokens
import androidx.compose.material.icons.filled.Tab
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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

    if (isVerticalMode) {
        AssistChip(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClick(topic.lastMessage?.id ?: 0) },
            label = {
                Column {
                    icon()
                    topicName()
                }
            },
            enabled = isSelected
        )
    } else {
        AssistChip(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClick(if (topic.info.isGeneral) topic.info.messageThreadId else topic.lastMessage?.id ?: 0) },
            leadingIcon = {
                Row {
                    if (isPinned) {
                        Icon(
                            imageVector = Icons.Outlined.PushPin,
                            contentDescription = "forum_topic_tab_pinned_${topic.info.messageThreadId}"
                        )
                    }

                    icon()
                }
            },
            label = { topicName() },
            enabled = isSelected
        )
    }
}