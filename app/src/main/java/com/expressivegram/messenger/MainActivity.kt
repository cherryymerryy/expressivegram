package com.expressivegram.messenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expressivegram.messenger.presentation.screens.ApplicationNavigation
import com.expressivegram.messenger.presentation.screens.auth.LoginScreen
import com.expressivegram.messenger.presentation.screens.loading.LoadingScreen
import com.expressivegram.messenger.presentation.theme.ExpressivegramTheme
import com.expressivegram.messenger.viewmodel.AppState
import com.expressivegram.messenger.viewmodel.AppViewModel

@ExperimentalMaterial3ExpressiveApi
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        setContent {
            ExpressivegramTheme {
                AppContent()
            }
        }
    }
}

@ExperimentalMaterial3ExpressiveApi
@ExperimentalMaterial3Api
@Composable
fun AppContent(appViewModel: AppViewModel = viewModel()) {
    val appState by appViewModel.appState

    Crossfade(targetState = appState, label = "AppState Animation") { state ->
        when (state) {
            is AppState.Loading -> LoadingScreen()
            is AppState.NeedsAuth -> LoginScreen()
            is AppState.LoggedIn -> ApplicationNavigation()
        }
    }
}