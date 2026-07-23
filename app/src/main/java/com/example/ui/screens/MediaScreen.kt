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
import com.example.ui.theme.SkyBluePrimary
import com.example.viewmodel.MainViewModel

@Composable
fun MediaScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Músicas, 1: Educativo, 2: Brincadeiras

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .testTag("videos_parent_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Vídeos 🎬",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SkyBluePrimary
                )
                Text(
                    text = "Músicas, vídeos educativos e brincadeiras divertidas!",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        // Custom 3-Tab Switcher (Músicas, Educativo, Brincadeiras)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 2.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                // Tab 0: Músicas
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (selectedTab == 0) SkyBluePrimary else Color.Transparent)
                        .clickable {
                            selectedTab = 0
                        }
                        .padding(vertical = 10.dp)
                        .testTag("videos_tab_musicas"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Músicas",
                            tint = if (selectedTab == 0) Color.White else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Músicas 🎵",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (selectedTab == 0) Color.White else Color.Gray
                        )
                    }
                }

                // Tab 1: Educativo
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (selectedTab == 1) SkyBluePrimary else Color.Transparent)
                        .clickable {
                            selectedTab = 1
                        }
                        .padding(vertical = 10.dp)
                        .testTag("videos_tab_educativo"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Educativo",
                            tint = if (selectedTab == 1) Color.White else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Educativo 📚",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (selectedTab == 1) Color.White else Color.Gray
                        )
                    }
                }

                // Tab 2: Brincadeiras
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (selectedTab == 2) SkyBluePrimary else Color.Transparent)
                        .clickable {
                            selectedTab = 2
                        }
                        .padding(vertical = 10.dp)
                        .testTag("videos_tab_brincadeiras"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEmotions,
                            contentDescription = "Brincadeiras",
                            tint = if (selectedTab == 2) Color.White else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Brincadeiras 🎈",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (selectedTab == 2) Color.White else Color.Gray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Submenu Content View
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> MusicScreen(mainViewModel = mainViewModel)
                1 -> VideosScreen(mainViewModel = mainViewModel, categoryFilter = "educativo")
                2 -> VideosScreen(mainViewModel = mainViewModel, categoryFilter = "brincadeiras")
            }
        }
    }
}
