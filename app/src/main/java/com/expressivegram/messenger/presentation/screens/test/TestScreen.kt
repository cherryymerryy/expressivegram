package com.expressivegram.messenger.presentation.screens.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers.RED_DOMINATED_EXAMPLE
import androidx.compose.ui.unit.dp
import com.expressivegram.messenger.presentation.navigation.components.CustomTopBar
import com.expressivegram.messenger.presentation.screens.chat.components.ChatBottomBar
import com.expressivegram.messenger.presentation.theme.ExpressivegramTheme

@ExperimentalMaterial3ExpressiveApi
@ExperimentalMaterial3Api
@Preview(
    showSystemUi = true,
    showBackground = true,
    wallpaper = RED_DOMINATED_EXAMPLE
)
@Composable
fun TestScreen() {
    ExpressivegramTheme {
        Scaffold(
            topBar = {
                CustomTopBar()
            },
            modifier = Modifier.fillMaxSize()
        ) { ip ->
            Box(Modifier.padding(ip).fillMaxSize()) {
                Column {

                    ChatBottomBar { }

                    LazyColumn(
//                    modifier =
//                        Modifier.floatingToolbarVerticalNestedScroll(
//                            expanded = expanded,
//                            onExpand = { expanded = true },
//                            onCollapse = { expanded = false },
//                        ),
                        state = rememberLazyListState(),
                        contentPadding = ip,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        val list = (0..75).map { it.toString() }
                        items(count = list.size) {
                            Text(
                                text = list[it],
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
