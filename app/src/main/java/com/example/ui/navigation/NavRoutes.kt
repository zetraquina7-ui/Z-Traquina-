package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("inicio", "Início", Icons.Default.Home)
    object Learn : Screen("aprender", "Aprender", Icons.Default.School)
    object Music : Screen("musicas", "Músicas", Icons.Default.Star)
    object Games : Screen("jogos", "Jogos", Icons.Default.SportsEsports)
    object Media : Screen("videos", "Vídeos", Icons.Default.OndemandVideo)
    object Chat : Screen("chat", "ZéAI", Icons.Default.Face)
    object Parents : Screen("pais", "Pais", Icons.Default.SupervisorAccount)
    object More : Screen("mais", "Mais", Icons.Default.Info)
}

val mainNavScreens = listOf(
    Screen.Home,
    Screen.Learn,
    Screen.Music,
    Screen.Games,
    Screen.Media,
    Screen.Chat,
    Screen.Parents,
    Screen.More
)
