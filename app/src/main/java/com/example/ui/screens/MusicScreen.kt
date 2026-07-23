package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.audio.AudioSynthesizer
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.LaunchedEffect
import com.example.ui.theme.KidStarOrange
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunshineYellow
import com.example.viewmodel.MainViewModel


data class SongItem(
    val id: String,
    val title: String,
    val youtubeId: String,
    val youtubeUrl: String,
    val videoUrl: String,
    val description: String
)

@Composable
fun InAppNativeVideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    var lastUrl by remember { mutableStateOf("") }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            android.widget.VideoView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setOnPreparedListener { mp ->
                    mp.isLooping = true
                    mp.start()
                }
                setOnErrorListener { _, _, _ ->
                    true
                }
            }
        },
        update = { videoView ->
            if (lastUrl != videoUrl) {
                lastUrl = videoUrl
                videoView.setVideoPath(videoUrl)
                videoView.requestFocus()
                videoView.start()
            }
        },
        onRelease = { videoView ->
            try {
                videoView.stopPlayback()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    )
}

@Composable
fun MusicScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val songs = remember {
        listOf(
            SongItem(
                id = "s0",
                title = "1. Zé Traquina é fixe 🧢🎵",
                youtubeId = "jYYvwC3L2kI",
                youtubeUrl = "https://youtu.be/jYYvwC3L2kI?feature=shared",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                description = "A música oficial do Zé Traquina! Cantada com toda a alegria e energia do teu melhor amigo!"
            ),
            SongItem(
                id = "s1",
                title = "2. Férias de verão ☀️🏖️",
                youtubeId = "jYYvwC3L2kI",
                youtubeUrl = "https://youtu.be/jYYvwC3L2kI?feature=shared",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                description = "Canção alegre para celebrar os dias de sol e praia!"
            ),
            SongItem(
                id = "s2",
                title = "2. Marcha dos santos populares 🎈🎉",
                youtubeId = "GXDSVN0nfJo",
                youtubeUrl = "https://youtu.be/GXDSVN0nfJo?feature=shared",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoylikes.mp4",
                description = "Marcha festiva cheia de alegria, arraial e tradição!"
            ),
            SongItem(
                id = "s3",
                title = "3. Dia da criança - Magia no ar ✨🎈",
                youtubeId = "Xkbtbam05_w",
                youtubeUrl = "https://youtu.be/Xkbtbam05_w?feature=shared",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                description = "Um dia repleto de magia, sorrisos e brincadeiras!"
            ),
            SongItem(
                id = "s4",
                title = "4. Dia da família 👨‍👩‍👧‍👦❤️",
                youtubeId = "DhvP0v6uZEI",
                youtubeUrl = "https://youtu.be/DhvP0v6uZEI?feature=shared",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                description = "Canção carinhosa sobre o amor e união em família."
            ),
            SongItem(
                id = "s5",
                title = "5. Animais do ABC 🦁🔤",
                youtubeId = "pmMVHEF0zQg",
                youtubeUrl = "https://youtu.be/pmMVHEF0zQg?feature=shared",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                description = "Aprende o alfabeto com os bichinhos mais simpáticos!"
            ),
            SongItem(
                id = "s6",
                title = "6. Um coração para ti 💖🎶",
                youtubeId = "VbUX6EODgc0",
                youtubeUrl = "https://youtu.be/VbUX6EODgc0?feature=shared",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                description = "Melodia doce cheia de afeto e amizade."
            ),
            SongItem(
                id = "s7",
                title = "7. Juntos somos o mundo 🌍🤝",
                youtubeId = "Ce6QVBTkSUI",
                youtubeUrl = "https://youtu.be/Ce6QVBTkSUI?feature=shared",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                description = "Música inspiradora sobre a paz e o planeta."
            ),
            SongItem(
                id = "s8",
                title = "8. A luz de Jesus venceu ✝️✨",
                youtubeId = "npjny0rOVok",
                youtubeUrl = "https://youtu.be/npjny0rOVok?feature=shared",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackBranding.mp4",
                description = "Canção alegre de esperança e luz."
            ),
            SongItem(
                id = "s9",
                title = "9. Festa de carnaval 🎭🥳",
                youtubeId = "hrpeW39DYTM",
                youtubeUrl = "https://youtu.be/hrpeW39DYTM?feature=shared",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
                description = "Música contagiante para mascarar e dançar!"
            ),
            SongItem(
                id = "s10",
                title = "10. É Natal 🎄🎁",
                youtubeId = "Cdys2zuYpVs",
                youtubeUrl = "https://youtu.be/Cdys2zuYpVs?feature=shared",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4",
                description = "Canção festiva com o espírito do Natal!"
            ),
            SongItem(
                id = "s11",
                title = "11. É halloween, que divertido 🎃👻",
                youtubeId = "JT5dhPkaXLI",
                youtubeUrl = "https://youtu.be/JT5dhPkaXLI?feature=shared",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                description = "Brincadeira alegre de Halloween com o Zé Traquina!"
            )
        )
    }

    var selectedSongIndex by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosSec by remember { mutableStateOf(0) }
    val totalPosSec = 90 // 1 min 30s duration

    val currentSong = songs.getOrNull(selectedSongIndex) ?: songs[0]

    // Animated Equalizer Bars
    val infiniteTransition = rememberInfiniteTransition(label = "eq_anim")
    val b1 by infiniteTransition.animateFloat(0.3f, 0.95f, infiniteRepeatable(tween(320), RepeatMode.Reverse), label = "b1")
    val b2 by infiniteTransition.animateFloat(0.2f, 1.0f, infiniteRepeatable(tween(480), RepeatMode.Reverse), label = "b2")
    val b3 by infiniteTransition.animateFloat(0.4f, 0.85f, infiniteRepeatable(tween(380), RepeatMode.Reverse), label = "b3")
    val b4 by infiniteTransition.animateFloat(0.15f, 0.9f, infiniteRepeatable(tween(520), RepeatMode.Reverse), label = "b4")
    val b5 by infiniteTransition.animateFloat(0.35f, 0.8f, infiniteRepeatable(tween(410), RepeatMode.Reverse), label = "b5")

    // Automatic playback progress effect (No synth key sound during video playback)
    LaunchedEffect(isPlaying, selectedSongIndex) {
        AudioSynthesizer.stopMelody()
        if (isPlaying) {
            currentPosSec = 0
            while (isPlaying && currentPosSec < totalPosSec) {
                kotlinx.coroutines.delay(1000L)
                currentPosSec++
            }
            if (currentPosSec >= totalPosSec) {
                selectedSongIndex = (selectedSongIndex + 1) % songs.size
            }
        } else {
            currentPosSec = 0
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            AudioSynthesizer.stopMelody()
        }
    }

    val xylophoneKeys = listOf(
        Pair("Do", Color(0xFFFF5252)),
        Pair("Re", Color(0xFFFF9800)),
        Pair("Mi", Color(0xFFFFEB3B)),
        Pair("Fa", Color(0xFF4CAF50)),
        Pair("Sol", Color(0xFF00BCD4)),
        Pair("La", Color(0xFF2196F3)),
        Pair("Si", Color(0xFF9C27B0)),
        Pair("Do2", Color(0xFFE91E63))
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .testTag("music_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // --- TOP NATIVE MUSIC PLAYER SCREEN (Ecrã do Leitor Direto em Cima) ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("top_music_player_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Top Player Visual Canvas (In-App Player Canvas)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .background(Color.Black)
                    ) {
                        if (isPlaying) {
                            InAppNativeVideoPlayer(
                                videoUrl = currentSong.videoUrl,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            // Album Art Cover Background
                            AsyncImage(
                                model = "https://img.youtube.com/vi/${currentSong.youtubeId}/hqdefault.jpg",
                                contentDescription = currentSong.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Dark Gradient Overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.45f))
                            )

                            // Center Big Play Button
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .clickable {
                                        isPlaying = true
                                        mainViewModel.addStars(2)
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    modifier = Modifier.size(68.dp),
                                    shape = CircleShape,
                                    color = SkyBluePrimary,
                                    shadowElevation = 8.dp
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Tocar na APK",
                                            tint = Color.White,
                                            modifier = Modifier.size(46.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color.Black.copy(alpha = 0.85f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = SunshineYellow,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Toque para Assistir na APK! 🎬",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Player Controls & Progress Bar Section Below Screen
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        // Title & Track Count Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isPlaying) SunshineYellow else SkyBluePrimary
                            ) {
                                Text(
                                    text = if (isPlaying) "A TOCAR NA APK 🎬" else "DISPONÍVEL 🎵",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isPlaying) Color.Black else Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Text(
                                text = "Música ${selectedSongIndex + 1} de ${songs.size}",
                                fontSize = 12.sp,
                                color = Color.LightGray,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = currentSong.title,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = currentSong.description,
                            color = Color.Gray,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Control Buttons Row (Previous, Play/Pause inside APK, Next)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Previous Track
                            IconButton(
                                onClick = {
                                    selectedSongIndex = if (selectedSongIndex > 0) selectedSongIndex - 1 else songs.size - 1
                                    isPlaying = true
                                    mainViewModel.addStars(1)
                                },
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = "Música Anterior",
                                    tint = Color.White
                                )
                            }

                            // In-App Play / Pause Button
                            Button(
                                onClick = {
                                    isPlaying = !isPlaying
                                    if (isPlaying) mainViewModel.addStars(2)
                                },
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isPlaying) Color(0xFFEF4444) else SkyBluePrimary
                                ),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Pausar" else "Tocar na APK",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isPlaying) "Pausar ⏸" else "Tocar na APK ▶",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }

                            // Next Track
                            IconButton(
                                onClick = {
                                    selectedSongIndex = (selectedSongIndex + 1) % songs.size
                                    isPlaying = true
                                    mainViewModel.addStars(1)
                                },
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "Próxima Música",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- BOTTOM SONG LIST HEADER ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lista de Músicas (${songs.size}) 🎵",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Toque para carregar",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        // --- SONGS LIST BELOW PLAYER ---
        itemsIndexed(songs) { index, song ->
            val isSelected = selectedSongIndex == index
            val thumbnailUrl = "https://img.youtube.com/vi/${song.youtubeId}/hqdefault.jpg"

            val isCurrentPlaying = isSelected && isPlaying

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 5.dp)
                    .clickable {
                        selectedSongIndex = index
                        isPlaying = true
                        mainViewModel.addStars(1)
                    }
                    .testTag("song_item_${song.id}"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) SunshineYellow.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Track Thumbnail Box
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = thumbnailUrl,
                            contentDescription = song.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (isCurrentPlaying) KidStarOrange else SkyBluePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isCurrentPlaying) Icons.Default.VolumeUp else Icons.Default.PlayArrow,
                                contentDescription = "Tocar na APK",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isSelected) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SkyBluePrimary,
                                    modifier = Modifier.padding(end = 6.dp)
                                ) {
                                    Text(
                                        text = if (isPlaying) "A TOCAR NA APK" else "SELECIONADA",
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = song.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = song.description,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray
                        )
                    }

                    // Direct Play button on right
                    IconButton(
                        onClick = {
                            selectedSongIndex = index
                            isPlaying = true
                            mainViewModel.addStars(1)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isCurrentPlaying) Icons.Default.VolumeUp else Icons.Default.PlayArrow,
                            contentDescription = "Tocar na APK",
                            tint = if (isSelected) SkyBluePrimary else Color.Gray
                        )
                    }
                }
            }
        }

        // --- Xylophone Section at Bottom ---
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .testTag("xylophone_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎹 Xilofone do Zé Traquina",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Toque nas teclas para tocares as tuas próprias notas!",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Keys Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        xylophoneKeys.forEachIndexed { index, (note, color) ->
                            val heightFraction = 1f - (index * 0.04f)
                            var isKeyTapped by remember { mutableStateOf(false) }
                            val scale by animateFloatAsState(targetValue = if (isKeyTapped) 0.92f else 1.0f)

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(heightFraction)
                                    .scale(scale)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(color)
                                    .clickable {
                                        isKeyTapped = true
                                        val freq = AudioSynthesizer.NOTE_FREQUENCIES[note] ?: 261.63
                                        AudioSynthesizer.playNote(freq)
                                        mainViewModel.addStars(1)
                                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({ isKeyTapped = false }, 180)
                                    },
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Text(
                                    text = note,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

