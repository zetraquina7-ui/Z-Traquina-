package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("inicio", "Início", Icons.Default.Home)
    object Learn : Screen("aprender", "Aprender", Icons.Default.AutoAwesome)
    object Games : Screen("jogos", "Jogos", Icons.Default.SportsEsports)
    object Media : Screen("videos", "Vídeos", Icons.Default.OndemandVideo)
    object Chat : Screen("chat", "Zé Traquina", Icons.Default.Face)
    object More : Screen("mais", "Mais", Icons.Default.MoreHoriz)
}

val mainNavScreens = listOf(
    Screen.Home,
    Screen.Learn,
    Screen.Games,
    Screen.Media,
    Screen.Chat,
    Screen.More
)
