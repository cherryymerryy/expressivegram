package com.expressivegram.messenger.presentation.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes.Companion.Cookie12Sided
import androidx.compose.material3.MaterialShapes.Companion.Pill
import androidx.compose.material3.MaterialShapes.Companion.Slanted
import androidx.compose.material3.MaterialShapes.Companion.Square
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.expressivegram.messenger.data.chat.ChatType
import com.expressivegram.messenger.utils.getInitials
import org.drinkless.tdlib.TdApi
import java.io.File

@ExperimentalMaterial3ExpressiveApi
@Composable
fun ChatPhotoItem(
    name: String,
    photo: TdApi.File?,
    chatType: ChatType,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = MaterialTheme.colorScheme.secondary
    val textColor = MaterialTheme.colorScheme.onSecondary
    val size = 54.dp

    val shape = when (chatType) {
        ChatType.Channel -> Slanted.toShape()
        ChatType.Forum -> Square.toShape()
        ChatType.Group -> Pill.toShape()
        ChatType.Secret -> Cookie12Sided.toShape()
        else -> CircleShape
    }

    val path = remember { photo?.local?.path }
    val initials = remember(name) { getInitials(name) }

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (!path.isNullOrEmpty() && File(path).exists()) {
            AsyncImage(
                model = path,
                contentDescription = name
            )
        } else {
            Text(
                text = initials,
                fontWeight = FontWeight.Bold,
                color = textColor,
                fontSize = 20.sp
            )
        }
    }
}