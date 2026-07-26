package com.example.ui.components

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.KidStarOrange
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunshineYellow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AdvancedMusicPlayerComposable(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var currentProgress by remember { mutableFloatStateOf(0f) }
    var totalDuration by remember { mutableFloatStateOf(100f) }
    var volume by remember { mutableFloatStateOf(1f) }
    var isShuffle by remember { mutableStateOf(false) }
    var isRepeat by remember { mutableStateOf(false) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var currentTrackName by remember { mutableStateOf("Zé Traquina É Fixe") }

    LaunchedEffect(context) {
        withContext(Dispatchers.IO) {
            try {
                val mp = MediaPlayer.create(context, R.raw.ze_traquina_e_fixe)
                if (mp != null) {
                    withContext(Dispatchers.Main) {
                        totalDuration = mp.duration.toFloat().coerceAtLeast(1f)
                        mediaPlayer = mp
                        mp.setVolume(volume, volume)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    DisposableEffect(mediaPlayer) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            mediaPlayer?.start()
        } else {
            mediaPlayer?.pause()
        }
    }
    
    LaunchedEffect(isRepeat) {
        mediaPlayer?.isLooping = isRepeat
    }

    LaunchedEffect(volume) {
        mediaPlayer?.setVolume(volume, volume)
    }

    LaunchedEffect(isPlaying, mediaPlayer) {
        while (isPlaying) {
            val mp = mediaPlayer
            if (mp != null && mp.isPlaying) {
                currentProgress = mp.currentPosition.toFloat()
                totalDuration = mp.duration.toFloat().coerceAtLeast(1f)
            }
            delay(500L)
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2FE)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Album Art placeholder
            Surface(
                shape = CircleShape,
                color = SkyBluePrimary,
                modifier = Modifier.size(120.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(64.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = currentTrackName,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A)
            )
            Text(
                text = "Universo Zé Traquina",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Progress Bar
            Slider(
                value = currentProgress,
                onValueChange = { 
                    currentProgress = it
                    mediaPlayer?.seekTo(it.toInt())
                },
                valueRange = 0f..totalDuration,
                colors = SliderDefaults.colors(
                    thumbColor = KidStarOrange,
                    activeTrackColor = SkyBluePrimary
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatTime(currentProgress.toInt()), fontSize = 12.sp, color = Color.Gray)
                Text(formatTime(totalDuration.toInt()), fontSize = 12.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { isShuffle = !isShuffle }) {
                    Icon(Icons.Default.Shuffle, contentDescription = "Shuffle", tint = if (isShuffle) KidStarOrange else Color.Gray)
                }
                IconButton(onClick = { 
                    mediaPlayer?.seekTo(0)
                    currentProgress = 0f
                }) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", tint = SkyBluePrimary, modifier = Modifier.size(36.dp))
                }
                
                Surface(
                    shape = CircleShape,
                    color = SunshineYellow,
                    modifier = Modifier
                        .size(64.dp)
                        .clickable { isPlaying = !isPlaying }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.Black,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                IconButton(onClick = { 
                    mediaPlayer?.seekTo(0)
                    currentProgress = 0f
                }) {
                    Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = SkyBluePrimary, modifier = Modifier.size(36.dp))
                }
                IconButton(onClick = { isRepeat = !isRepeat }) {
                    Icon(Icons.Default.Repeat, contentDescription = "Repeat", tint = if (isRepeat) KidStarOrange else Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            // Volume
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.VolumeDown, contentDescription = "Volume Down", tint = Color.Gray)
                Slider(
                    value = volume,
                    onValueChange = { volume = it },
                    valueRange = 0f..1f,
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                    colors = SliderDefaults.colors(thumbColor = SkyBluePrimary, activeTrackColor = SkyBluePrimary)
                )
                Icon(Icons.Default.VolumeUp, contentDescription = "Volume Up", tint = Color.Gray)
            }
        }
    }
}

private fun formatTime(ms: Int): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
