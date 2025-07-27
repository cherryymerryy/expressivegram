package com.expressivegram.messenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.expressivegram.messenger.presentation.navigation.Route
import com.expressivegram.messenger.presentation.screens.ApplicationNavigation
import com.expressivegram.messenger.presentation.screens.MainScreen
import com.expressivegram.messenger.presentation.screens.auth.LoginScreen
import com.expressivegram.messenger.presentation.screens.loading.LoadingScreen
import com.expressivegram.messenger.presentation.theme.QDownloaderTheme
import com.expressivegram.messenger.viewmodel.AppState
import com.expressivegram.messenger.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            QDownloaderTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent(appViewModel: AppViewModel = viewModel()) {
    val appState by appViewModel.appState

    Crossfade(targetState = appState, label = "AppState Animation") { state ->
        when (state) {
            is AppState.Loading -> LoadingScreen()
            is AppState.NeedsAuth -> LoginScreen()
            is AppState.LoggedIn -> ApplicationNavigation(Route.Main.path)
        }
    }
}