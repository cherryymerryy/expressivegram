package com.expressivegram.messenger.presentation.screens.chat.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import org.drinkless.tdlib.TdApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatTopBar(
    chat: TdApi.Chat?,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { onBackPressed }
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = null
                )
            }
        },
        title = { Text(chat?.title ?: "Unknown chat") },
        subtitle = { Text(chat?.id.toString()) },
        actions = {
            IconButton(
                onClick = {  }
            ) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = null
                )
            }
        }
    )
}