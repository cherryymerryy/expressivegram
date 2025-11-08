package com.expressivegram.messenger.presentation.screens.chat.components.bars

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ElevatedToggleButton
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarColors
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.expressivegram.messenger.extensions.canInteractWithMessages
import com.expressivegram.messenger.extensions.canSendAnyFiles
import org.drinkless.tdlib.TdApi

@ExperimentalMaterial3ExpressiveApi
@Composable
fun ChannelBottomBar(
    modifier: Modifier = Modifier,
    memberStatus: TdApi.ChatMemberStatus?,
    permissions: TdApi.ChatPermissions?
) {
    var expanded by rememberSaveable { mutableStateOf(true) }
    var checked by rememberSaveable { mutableStateOf(true) }
    val textFieldState = rememberTextFieldState()

    val permissions = when (memberStatus) {
        is TdApi.ChatMemberStatusRestricted -> memberStatus.permissions
        else -> permissions
    }

    val administratorRights = when (memberStatus) {
        is TdApi.ChatMemberStatusAdministrator -> memberStatus.rights
        else -> null
    }

    val isCreator = memberStatus?.constructor == TdApi.ChatMemberStatusCreator.CONSTRUCTOR
    val isAdministrator = memberStatus?.constructor == TdApi.ChatMemberStatusAdministrator.CONSTRUCTOR && administratorRights != null
    val isTextFieldAvailable = isCreator || (isAdministrator && administratorRights.canInteractWithMessages())
    val canPostMessages = isCreator || (isAdministrator && administratorRights.canPostMessages)

    HorizontalFloatingToolbar(
        modifier = modifier
            .offset(y = -ScreenOffset)
            .zIndex(1f)
            .padding(horizontal = 14.dp),
        expanded = expanded,
        colors = FloatingToolbarColors(
            toolbarContainerColor = if (isTextFieldAvailable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer,
            toolbarContentColor = if (isTextFieldAvailable) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            fabContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            fabContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        leadingContent = {
            if (!isTextFieldAvailable) {
                IconButton(
                    onClick = {  }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search"
                    )
                }
            }
        },
        trailingContent = {
            if (!isTextFieldAvailable) {
                IconButton(
                    onClick = {  }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CardGiftcard,
                        contentDescription = "Gift"
                    )
                }
            }
        }
    ) {
        if (isCreator || (isAdministrator && administratorRights.canInteractWithMessages())) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { /*TODO*/ },
                enabled = permissions?.canSendAnyFiles() == true || canPostMessages
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
            if (textFieldState.text.isEmpty()) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = { /*TODO*/ },
                    enabled = permissions?.canSendPhotos == true || canPostMessages
                ) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = "Camera"
                    )
                }
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = { /*TODO*/ },
                    enabled = permissions?.canSendVoiceNotes == true || canPostMessages
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardVoice,
                        contentDescription = "Voice message"
                    )
                }
            }
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                enabled = permissions?.canSendBasicMessages == true || canPostMessages,
                state = textFieldState,
                shape = CircleShape,
                placeholder = {
                    Text(
                        text = "Write a message...",
                        overflow = TextOverflow.Ellipsis
                    )
                },
                trailingIcon = {
                    if (textFieldState.text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                textFieldState.edit {
                                    delete(0, textFieldState.text.length)
                                }
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { /*TODO*/ },
                            enabled = permissions?.canSendOtherMessages == true || canPostMessages
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.EmojiEmotions,
                                contentDescription = "Emoji"
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                )
            )
        } else {
            ElevatedToggleButton(
                checked = checked,
                onCheckedChange = { checked = it },
            ) {
                Text(if (checked) "Mute" else "Unmute")
            }
        }
    }
}