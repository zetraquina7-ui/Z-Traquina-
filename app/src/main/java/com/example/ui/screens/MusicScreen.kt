package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.AudioSynthesizer
import com.example.ui.components.YouTubePlayerComposable
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunshineYellow
import com.example.viewmodel.MainViewModel

data class MusicVideo(
    val id: String,
    val title: String,
    val duration: String,
    val youtubeId: String,
    val emoji: String,
    val category: String
)

@Composable
fun MusicScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    // Stop any active melody synthesized sound
    LaunchedEffect(Unit) {
        AudioSynthesizer.stopMelody()
    }

    DisposableEffect(Unit) {
        onDispose {
            AudioSynthesizer.stopMelody()
        }
    }

    var selectedCategory by remember { mutableStateOf("Músicas") }
    val musicVideos = remember {
        listOf(
            MusicVideo("m1", "Canção Oficial do Zé Traquina", "3:15", "jYYvwC3L2kI", "🎵", "Músicas"),
            MusicVideo("m2", "O ABC do Zé Traquina", "2:45", "dQw4w9WgXcQ", "🔤", "Músicas"),
            MusicVideo("m3", "Dança das Cores Mágicas", "3:00", "jYYvwC3L2kI", "🎨", "Músicas"),
            MusicVideo("m4", "Sons Alegre da Fazenda", "2:50", "dQw4w9WgXcQ", "🐮", "Músicas"),
            MusicVideo("m5", "Estrelinha Guia (Canção de Ninar)", "3:30", "jYYvwC3L2kI", "⭐", "Ninar"),
            MusicVideo("m6", "Soninho Bom no Universo", "4:00", "dQw4w9WgXcQ", "🌙", "Ninar"),
            MusicVideo("m7", "Ginástica Animada do Zé", "3:10", "jYYvwC3L2kI", "🤸‍♂️", "Danças"),
            MusicVideo("m8", "Festa dos Amiguinhos", "3:20", "dQw4w9WgXcQ", "🎉", "Danças")
        )
    }

    var currentPlayingVideoId by remember { mutableStateOf("jYYvwC3L2kI") }
    var currentPlayingTitle by remember { mutableStateOf("Canção Oficial do Zé Traquina") }

    val filteredVideos = remember(selectedCategory) {
        musicVideos.filter { it.category == selectedCategory }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .testTag("music_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Músicas & Playloads 🎵📺",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Televisão em cima e lista de músicas em baixo!",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        // --- 1. TV EM CIMA (Embedded YouTube Player) ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .testTag("tv_player_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                YouTubePlayerComposable(
                    youtubeId = currentPlayingVideoId,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Currently Playing Banner
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            shape = RoundedCornerShape(12.dp),
            color = SkyBluePrimary.copy(alpha = 0.15f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = SkyBluePrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "A reproduzir: $currentPlayingTitle",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Category Filter Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Músicas", "Ninar", "Danças").forEach { cat ->
                val isSelected = selectedCategory == cat
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { selectedCategory = cat },
                    color = if (isSelected) SkyBluePrimary else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cat,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // --- 2. LISTA DE VIDEOS EM BAIXO ---
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredVideos) { video ->
                val isPlaying = video.youtubeId == currentPlayingVideoId && video.title == currentPlayingTitle
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            currentPlayingVideoId = video.youtubeId
                            currentPlayingTitle = video.title
                            mainViewModel.addStars(1)
                            mainViewModel.speak("A reproduzir ${video.title}")
                        }
                        .testTag("music_item_${video.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isPlaying) SkyBluePrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Emoji Thumbnail
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isPlaying) SkyBluePrimary else SunshineYellow.copy(alpha = 0.3f),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = video.emoji,
                                    fontSize = 24.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = video.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "⏱️ ${video.duration} • Zé Traquina",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isPlaying) SkyBluePrimary else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Tocar",
                                tint = if (isPlaying) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
