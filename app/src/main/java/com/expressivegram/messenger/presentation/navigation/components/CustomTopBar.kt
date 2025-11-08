package com.expressivegram.messenger.presentation.navigation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.data.chat.ChatType
import com.expressivegram.messenger.presentation.components.profile.ChatPhotoItem
import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.utils.UserConfig
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.drinkless.tdlib.TdApi

@ExperimentalMaterial3Api
@ExperimentalMaterial3ExpressiveApi
@Composable
fun CustomTopBar() {
    val scope = rememberCoroutineScope()
    var searchPlaceholder by remember { mutableStateOf("Search...") }

    val instance = TdUtility.getInstance()

    LaunchedEffect(searchPlaceholder) {
        instance.updates
            .filterIsInstance<TdApi.UpdateConnectionState>()
            .onEach { updateConnectionState ->
                searchPlaceholder = when (updateConnectionState.state.constructor) {
                    TdApi.ConnectionStateUpdating.CONSTRUCTOR -> "Updating..."
                    TdApi.ConnectionStateConnecting.CONSTRUCTOR -> "Connecting..."
                    TdApi.ConnectionStateWaitingForNetwork.CONSTRUCTOR -> "Waiting for Network..."
                    TdApi.ConnectionStateConnectingToProxy.CONSTRUCTOR -> "Connecting to Proxy..."
                    else -> "Search..."
                }
            }
            .launchIn(scope)
    }

    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()

    val currentUser = UserConfig.getInstance().getCurrentUser()

    AppBarWithSearch(
        modifier = Modifier.padding(bottom = 16.dp),
        colors = SearchBarDefaults.appBarWithSearchColors(
            appBarContainerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        state = rememberSearchBarState(),
        inputField = {
            SearchBarDefaults.InputField(
                searchBarState = searchBarState,
                textFieldState = textFieldState,
                colors = inputFieldColors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                ),
                onSearch = {
                },
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = searchPlaceholder,
                        textAlign = TextAlign.Center
                    )
                }
            )
        },
        actions = {
            if (currentUser != null) {
                ChatPhotoItem(
                    name = currentUser.firstName + currentUser.lastName,
                    photo = currentUser.profilePhoto?.small,
                    chatType = ChatType.Private
                )
            }
        }
    )
}