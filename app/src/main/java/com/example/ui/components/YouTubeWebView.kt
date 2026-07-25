package com.example.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun YouTubeWebView(
    playlistId: String = "PLHz1Xt0IaQWM",
    modifier: Modifier = Modifier
) {
    YouTubePlaylistPlayerComposable(
        playlistId = playlistId,
        modifier = modifier
    )
}
