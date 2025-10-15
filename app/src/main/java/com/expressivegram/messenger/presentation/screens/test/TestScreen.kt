package com.expressivegram.messenger.presentation.screens.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TestScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
            .padding(vertical = 30.dp, horizontal = 8.dp)
            .clip(
                MaterialTheme.shapes.large.copy(
                    bottomStart = MaterialTheme.shapes.medium.bottomStart,
                    bottomEnd = MaterialTheme.shapes.medium.bottomEnd,
                    topStart = MaterialTheme.shapes.medium.topStart,
                    topEnd = MaterialTheme.shapes.medium.topEnd
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)
        )
    }
}