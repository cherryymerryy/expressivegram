package com.expressivegram.messenger.presentation.screens.chat.components.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.data.message.TdMessageState
import com.expressivegram.messenger.presentation.screens.chat.components.message.MessageCell

@ExperimentalMaterial3ExpressiveApi
@Composable
fun HistoryList(
    messages: List<TdMessageState>
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(index = 0)
        }
    }

//    LaunchedEffect(messages) {
//        val index = messages.indexOfFirst { it.message.id == chatState?.firstUnreadMessageId }
//        if (index != -1) {
//            lazyListState.animateScrollToItem(index)
//        }
//    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .clip(
                shape = MaterialTheme.shapes.small.copy(
                    topStart = MaterialTheme.shapes.extraLargeIncreased.topStart,
                    topEnd = MaterialTheme.shapes.extraLargeIncreased.topEnd,
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp)
                )
            )
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 12.dp),
        state = lazyListState,
        verticalArrangement = Arrangement.Top,
        reverseLayout = true,
        contentPadding = PaddingValues(bottom = 95.dp)
    ) {
        if (messages.isNotEmpty()) {
            items(messages, key = { it.message.id }) { state ->
                MessageCell(state)
            }
        } else {
            item {
                Box(
                    modifier = Modifier.fillParentMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularWavyProgressIndicator()
                }
            }
        }
    }
}