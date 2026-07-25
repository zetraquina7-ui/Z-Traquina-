package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.ui.components.YouTubePlaylistPlayerComposable
import com.example.ui.theme.KidStarOrange
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunshineYellow
import com.example.viewmodel.MainViewModel

@Composable
fun MediaScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Músicas Infantis, 1: Educativo, 2: Diversão & Mais

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .testTag("videos_parent_screen")
    ) {
        // Top Header with high zIndex
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .zIndex(9999f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Vídeos & Músicas 🎬🎵",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SkyBluePrimary
                )
                Text(
                    text = "Explora as playlists oficiais do Zé Traquina!",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        // Custom 3-Tab Switcher with child-friendly colors and high zIndex
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 2.dp)
                .zIndex(9999f),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                // Tab 1: Músicas Infantis
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (selectedTab == 0) SkyBluePrimary else Color.Transparent)
                        .clickable { selectedTab = 0 }
                        .padding(vertical = 10.dp)
                        .testTag("videos_tab_musicas_infantis"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = if (selectedTab == 0) Color.White else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Músicas 🎵",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (selectedTab == 0) Color.White else Color.Gray
                        )
                    }
                }

                // Tab 2: Educativo
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (selectedTab == 1) SunshineYellow else Color.Transparent)
                        .clickable { selectedTab = 1 }
                        .padding(vertical = 10.dp)
                        .testTag("videos_tab_educativo"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = if (selectedTab == 1) Color.Black else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Educativo 📚",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (selectedTab == 1) Color.Black else Color.Gray
                        )
                    }
                }

                // Tab 3: Diversão & Mais
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (selectedTab == 2) KidStarOrange else Color.Transparent)
                        .clickable { selectedTab = 2 }
                        .padding(vertical = 10.dp)
                        .testTag("videos_tab_diversao"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEmotions,
                            contentDescription = null,
                            tint = if (selectedTab == 2) Color.White else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Diversão ⭐",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (selectedTab == 2) Color.White else Color.Gray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Active Playlist Iframe Player via youtube-nocookie.com
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 8.dp)
                .zIndex(1f)
        ) {
            val currentEmbedUrl = when (selectedTab) {
                0 -> "https://www.youtube-nocookie.com/embed/videoseries?list=PLHz1Xt0IaQWM"
                1 -> "https://www.youtube-nocookie.com/embed/videoseries?list=PLT7ZV5QsDKA4"
                else -> "https://www.youtube-nocookie.com/embed/videoseries?list=PLHXyMYX6Yxxc"
            }

            YouTubePlaylistPlayerComposable(
                embedUrl = currentEmbedUrl,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
