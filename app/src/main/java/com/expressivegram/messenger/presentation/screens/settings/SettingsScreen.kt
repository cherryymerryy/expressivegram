package com.expressivegram.messenger.presentation.screens.settings

import android.text.TextUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddBusiness
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.CardGiftcard
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.FolderCopy
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Power
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material.icons.rounded.QuestionAnswer
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers.GREEN_DOMINATED_EXAMPLE
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expressivegram.messenger.BuildConfig
import com.expressivegram.messenger.presentation.components.preferences.PreferenceCategory
import com.expressivegram.messenger.presentation.components.preferences.PreferenceItem
import com.expressivegram.messenger.presentation.components.preferences.PreferencePosition
import com.expressivegram.messenger.utils.UserConfig
import com.expressivegram.messenger.viewmodel.settings.SettingsViewModel
import org.drinkless.tdlib.TdApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true, showSystemUi = true, wallpaper = GREEN_DOMINATED_EXAMPLE)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel = viewModel()) {
    val me = UserConfig.getInstance().getCurrentUser()
    val fullInfo = UserConfig.getInstance().getCurrentUserFullInfo()

    if (me == null) {
        viewModel.updateUserConfig()
    }
    if (fullInfo == null) {
        viewModel.updateUserConfig()
    }

    val personalChat by viewModel.personalChat

    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(8.dp),
                shape = MaterialTheme.shapes.extraLargeIncreased
            ) {
                Image(
                    modifier = Modifier
                        .width(86.dp)
                        .height(86.dp)
                        .background(Color.Red)
                        .clip(MaterialTheme.shapes.extraLargeIncreased),
                    contentDescription = null,
                    bitmap = ImageBitmap(64, 64),
                    alignment = Alignment.TopCenter,
                    contentScale = ContentScale.None,
                    alpha = 1f,
                )
            }

            Text(
                text = me?.firstName + (if (!TextUtils.isEmpty(me?.lastName)) " ${me?.lastName}" else ""),
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                textAlign = TextAlign.Center
            )
            if (me?.usernames != null) {
                Text(
                    text = "@${me.usernames?.activeUsernames[0]}",
                    fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                    fontSize = MaterialTheme.typography.labelLarge.fontSize,
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                    fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .clip(
                    MaterialTheme.shapes.large.copy(
                        bottomStart = MaterialTheme.shapes.extraSmall.bottomStart,
                        bottomEnd = MaterialTheme.shapes.extraSmall.bottomEnd
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(2.dp),

            ) {
            if (fullInfo?.personalChatId != 0L) {
                viewModel.updatePersonalChat(fullInfo?.personalChatId ?: 0)

                if (personalChat != null) {
                    item {
                        PreferenceCategory(title = "Personal chat")
                    }

                    item {
                        PreferenceItem(
                            title = personalChat?.title ?: "None",
                            subtitle = (personalChat?.lastMessage?.content as TdApi.MessageText).text.text,
                            icon = Icons.Rounded.Campaign,
                            position = PreferencePosition.Single,
                        )
                    }
                }
            }


            item {
                PreferenceCategory(title = "Info")
            }

            if (me?.phoneNumber != null) {
                item {
                    PreferenceItem(
                        title = me.phoneNumber ?: "None",
                        subtitle = "Phone number",
                        icon = Icons.Rounded.Phone,
                        position = PreferencePosition.Top,
                    )
                }
            }

            if (fullInfo?.bio != null) {
                item {
                    PreferenceItem(
                        title = fullInfo.bio?.text ?: "None",
                        subtitle = "Bio",
                        icon = Icons.Rounded.Description,
                        position = PreferencePosition.Middle,
                    )
                }
            }

            if (fullInfo?.birthdate != null) {
                item {
                    PreferenceItem(
                        title = fullInfo.birthdate.toString(),
                        subtitle = "Date of birth",
                        icon = Icons.Rounded.Cake,
                        position = PreferencePosition.Middle
                    )
                }
            }

            item {
                PreferenceItem(
                    title = me?.id.toString(),
                    subtitle = "ID",
                    icon = Icons.Rounded.Info,
                    position = PreferencePosition.Bottom
                )
            }

            item {
                PreferenceCategory(title = "Settings")
            }

            item {
                PreferenceItem(
                    title = "Chat Settings",
                    icon = Icons.Rounded.ChatBubble,
                    position = PreferencePosition.Top
                )
            }

            item {
                PreferenceItem(
                    title = "Privacy and Security",
                    icon = Icons.Rounded.Lock,
                    position = PreferencePosition.Middle
                )
            }

            item {
                PreferenceItem(
                    title = "Notifications and Sounds",
                    icon = Icons.Rounded.Notifications,
                    position = PreferencePosition.Middle
                )
            }

            item {
                PreferenceItem(
                    title = "Data and Storage",
                    icon = Icons.Rounded.Storage,
                    position = PreferencePosition.Middle
                )
            }

            item {
                PreferenceItem(
                    title = "Power Saving",
                    icon = Icons.Rounded.Power,
                    position = PreferencePosition.Middle
                )
            }

            item {
                PreferenceItem(
                    title = "Chat Folders",
                    icon = Icons.Rounded.FolderCopy,
                    position = PreferencePosition.Middle
                )
            }

            item {
                PreferenceItem(
                    title = "Devices",
                    icon = Icons.Rounded.Devices,
                    position = PreferencePosition.Middle
                )
            }

            item {
                PreferenceItem(
                    title = "Language",
                    icon = Icons.Rounded.Language,
                    position = PreferencePosition.Bottom
                )
            }

            item {
                PreferenceCategory(title = "Donat")
            }

            item {
                PreferenceItem(
                    title = "Telegram Premium",
                    icon = Icons.Rounded.WorkspacePremium,
                    position = PreferencePosition.Top
                )
            }

            item {
                PreferenceItem(
                    title = "My Stars",
                    icon = Icons.Rounded.Star,
                    position = PreferencePosition.Middle
                )
            }

            item {
                PreferenceItem(
                    title = "Telegram Business",
                    icon = Icons.Rounded.AddBusiness,
                    position = PreferencePosition.Middle
                )
            }

            item {
                PreferenceItem(
                    title = "Send a Gift",
                    icon = Icons.Rounded.CardGiftcard,
                    position = PreferencePosition.Bottom
                )
            }

            item {
                PreferenceCategory(title = "Help")
            }

            item {
                PreferenceItem(
                    title = "Ask a Question",
                    icon = Icons.Rounded.QuestionAnswer,
                    position = PreferencePosition.Top
                )
            }

            item {
                PreferenceItem(
                    title = "Privacy Policy",
                    icon = Icons.Rounded.PrivacyTip,
                    position = PreferencePosition.Bottom
                )
            }

            item {
                PreferenceCategory(title = "App info")
            }

            item {
                PreferenceItem(
                    title = "expressivegram",
                    subtitle = "${BuildConfig.VERSION_NAME}-${BuildConfig.BUILD_TYPE}",
                    icon = Icons.Rounded.QuestionAnswer,
                    position = PreferencePosition.Single
                )
            }

            item {
                Box(modifier = Modifier.height(10.dp))
            }
        }
    }
}