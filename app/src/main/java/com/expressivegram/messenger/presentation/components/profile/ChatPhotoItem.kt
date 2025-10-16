package com.expressivegram.messenger.presentation.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.expressivegram.messenger.BuildConfig
import com.expressivegram.messenger.extensions.execute
import com.expressivegram.messenger.utils.DownloadController
import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.utils.getInitials
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.drinkless.tdlib.TdApi
import java.io.File

@Composable
fun ChatPhotoItem(name: String, photo: TdApi.File?, modifier: Modifier = Modifier) {
    val backgroundColor = MaterialTheme.colorScheme.primary
    val size = 48.dp
    val shape = CircleShape

    val scope = rememberCoroutineScope()
    var url by remember { mutableStateOf("") }

    val instance = TdUtility.getInstance()

    LaunchedEffect(Unit) {
        instance.updates
            .filterIsInstance<TdApi.UpdateFile>()
            .onEach { updateFile ->
                if (updateFile.file.id != photo?.id) {
                    return@onEach
                }

                if (!updateFile.file.local.isDownloadingCompleted) {
                    return@onEach
                }

                url = updateFile.file.local.path
            }
            .launchIn(scope)
    }

    if (photo != null) {
        val local = photo.local

        if (local.canBeDownloaded && !local.isDownloadingCompleted)
            DownloadController.getInstance().downloadFile(photo.id)
        else
            url = local.path
    }

    if (url.isNotEmpty() && File(url).exists()) {
        AsyncImage(
            modifier = modifier
                .size(size)
                .clip(shape),
            model = url,
            contentDescription = name
        )
    } else if (name.isNotEmpty()) {
        val initials = getInitials(name)

        Box(
            modifier = modifier
                .size(size)
                .clip(shape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp
            )
        }
    } else {
        Box(
            modifier = modifier
                .size(size)
                .clip(shape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = BuildConfig.APPLICATION_ID,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp
            )
        }
    }
}