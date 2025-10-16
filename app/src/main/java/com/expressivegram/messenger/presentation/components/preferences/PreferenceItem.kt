package com.expressivegram.messenger.presentation.components.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.BuildConfig
import com.expressivegram.messenger.presentation.components.profile.ChatPhotoItem
import org.drinkless.tdlib.TdApi

@Composable
fun PreferenceItem(
    title: String,
    subtitle: String? = null,
    icon: Any,
    onClick: (() -> Unit)? = null,
    iconTint: Color = LocalContentColor.current,
    position: PreferencePosition = PreferencePosition.Single,
    trailingContent: @Composable (() -> Unit)? = null,
    maxLines: Int = Int.MAX_VALUE
) {
    val shape = when (position) {
        PreferencePosition.Single -> MaterialTheme.shapes.large
        PreferencePosition.Top -> MaterialTheme.shapes.large.copy(
            bottomStart = MaterialTheme.shapes.extraSmall.bottomStart,
            bottomEnd = MaterialTheme.shapes.extraSmall.bottomEnd
        )

        PreferencePosition.Bottom -> MaterialTheme.shapes.large.copy(
            topStart = MaterialTheme.shapes.extraSmall.topStart,
            topEnd = MaterialTheme.shapes.extraSmall.topEnd
        )

        PreferencePosition.Middle -> MaterialTheme.shapes.extraSmall
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            ),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = shape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (icon) {
                is ImageVector -> Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )

                is Painter -> Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )

                is TdApi.File -> ChatPhotoItem(
                    name = BuildConfig.APPLICATION_ID,
                    photo = icon,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        maxLines = maxLines,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            trailingContent?.invoke()
        }
    }
}