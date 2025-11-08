package com.expressivegram.messenger.presentation.screens.chat.components.bars

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.data.chat.ChatState
import com.expressivegram.messenger.data.chat.ChatType
import com.expressivegram.messenger.extensions.toText
import com.expressivegram.messenger.presentation.components.profile.ChatPhotoItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatTopBar(
    chat: ChatState,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        navigationIcon = {
            Row {
                IconButton(
                    onClick = { onBackPressed() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null
                    )
                }
                ChatPhotoItem(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    name = chat.title,
                    photo = chat.photo,
                    chatType = chat.type
                )
            }
        },
        title = {
            Text(
                text = chat.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        subtitle = {
            Text(
                text = if (chat.actions.isEmpty()) {
                    when (chat.type) {
                        ChatType.Private -> chat.userStatus?.toText() ?: "Unknown"
                        ChatType.Secret -> chat.userStatus?.toText() ?: "Unknown"
                        ChatType.Unknown -> "Unknown"
                        else -> "${chat.membersCount} members" + (if (chat.onlineMembersCount != 0) ", ${chat.onlineMembersCount} online" else "")
                    }
                } else {
                    "${chat.actions.size} action(-s)"
                }
            )
        },
        actions = {
            IconButton(
                onClick = {  }
            ) {
                Icon(
                    imageVector = Icons.Rounded.MoreHoriz,
                    contentDescription = null
                )
            }
        }
    )
}