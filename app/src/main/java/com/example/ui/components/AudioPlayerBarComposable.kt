package com.example.ui.components

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.AudioSynthesizer
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunshineYellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AudioPlayerBarComposable(
    isPlayingState: Boolean = false,
    onPlayStateChanged: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPlaying by remember(isPlayingState) { mutableStateOf(isPlayingState) }
    var currentProgress by remember { mutableFloatStateOf(0f) }
    var totalDuration by remember { mutableFloatStateOf(100f) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Prepare MediaPlayer asynchronously on IO dispatcher
    LaunchedEffect(context) {
        withContext(Dispatchers.IO) {
            try {
                val mp = MediaPlayer.create(context, R.raw.ze_traquina_e_fixe)
                if (mp != null) {
                    mp.isLooping = true
                    withContext(Dispatchers.Main) {
                        totalDuration = mp.duration.toFloat().coerceAtLeast(1f)
                        mediaPlayer = mp
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    DisposableEffect(mediaPlayer) {
        onDispose {
            val player = mediaPlayer
            AudioSynthesizer.stopMelody()
            if (player != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        if (player.isPlaying) {
                            player.stop()
                        }
                        player.release()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    LaunchedEffect(isPlaying, mediaPlayer) {
        val mp = mediaPlayer
        if (isPlaying) {
            if (mp != null) {
                try {
                    if (!mp.isPlaying) {
                        mp.start()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                AudioSynthesizer.playSongMelody(0, true)
            }
        } else {
            if (mp != null) {
                try {
                    if (mp.isPlaying) {
                        mp.pause()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            AudioSynthesizer.stopMelody()
        }
    }

    LaunchedEffect(isPlaying, mediaPlayer) {
        while (isPlaying) {
            val mp = mediaPlayer
            if (mp != null && mp.isPlaying) {
                currentProgress = mp.currentPosition.toFloat()
                totalDuration = mp.duration.toFloat().coerceAtLeast(1f)
            }
            delay(250L)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .testTag("audio_player_bar_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2FE)), // Soft blue
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Icon & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = CircleShape,
                    color = SkyBluePrimary,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Zé Traquina é fixe 🎵",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        maxLines = 1
                    )
                    
                    Spacer(modifier = Modifier.height(3.dp))
                    val progressFraction = (currentProgress / totalDuration).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = SkyBluePrimary,
                        trackColor = SkyBluePrimary.copy(alpha = 0.2f),
                    )
                }
            }

            // Play / Pause Button (Colorful circular)
            Surface(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .clickable {
                        val newState = !isPlaying
                        isPlaying = newState
                        onPlayStateChanged(newState)
                    },
                shape = CircleShape,
                color = if (isPlaying) SunshineYellow else SkyBluePrimary
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pausar" else "Tocar",
                        tint = if (isPlaying) Color.Black else Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
