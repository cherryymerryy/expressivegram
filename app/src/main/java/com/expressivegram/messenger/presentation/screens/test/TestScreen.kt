package com.expressivegram.messenger.presentation.screens.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.expressivegram.messenger.presentation.navigation.components.CustomTopBar

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TestScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CustomTopBar()
        }
    ) { ip ->
        TestScreen_0(
            Modifier
                .padding(ip)
                .background(Color.Red)
                .fillMaxSize()
        )
    }
}

@Composable
fun TestScreen_0(modifier: Modifier) {
    Box(
        modifier = modifier
    )
}