package com.expressivegram.messenger.presentation.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.expressivegram.messenger.viewmodel.chat.ChatViewModel
import org.drinkless.tdlib.TdApi
import java.io.File

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatPhotoItem(
    picture: TdApi.ChatPhotoInfo?,
    chatTitle: String,
    viewModel: ChatViewModel = viewModel()
) {
    var imagePath by remember { mutableStateOf(picture?.small?.local?.path) }

    LaunchedEffect(key1 = picture?.small?.id) {
        val smallFile = picture?.small
        if (smallFile != null) {
            if (!File(smallFile.local.path).exists()) {
                val downloadedPath = viewModel.downloadFileIfNeeded(smallFile)
                imagePath = downloadedPath
            }
        }
    }

    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        if (imagePath != null && File(imagePath!!).exists()) {
            AsyncImage(
                model = imagePath,
                contentDescription = chatTitle,
                modifier = Modifier.fillMaxSize()
            )
        } else if (picture != null) {
            LoadingIndicator(modifier = Modifier.size(24.dp))
        } else {
            val initials = chatTitle.take(1).uppercase()
            Text(text = initials, fontSize = 24.sp, color = Color.White)
        }
    }
}