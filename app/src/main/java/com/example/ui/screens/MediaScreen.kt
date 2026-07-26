package com.example.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.MediaViewModel

@Composable
fun MediaScreen(
    mainViewModel: MainViewModel,
    mediaViewModel: MediaViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    VideosScreen(
        mainViewModel = mainViewModel,
        modifier = modifier
    )
}
