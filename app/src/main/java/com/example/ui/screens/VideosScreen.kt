package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.components.YouTubePlayerComposable
import com.example.ui.theme.KidStarOrange
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunshineYellow
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val YOUTUBE_API_KEY = "AIzaSyBP6gYBEy9W2p0FIVObmRKScBkIJRqgTUE"

data class YouTubeVideoTrack(
    val id: String,
    val title: String,
    val videoId: String? = null,
    val thumbnailUrl: String? = null,
    val duration: String = "Vídeo do YouTube",
    val emoji: String = "🎵"
)

data class YouTubePlaylistData(
    val key: String,
    val name: String,
    val playlistId: String,
    val playlistUrl: String,
    val emoji: String,
    val accentColor: Color
)

suspend fun fetchYouTubePlaylistItems(playlistId: String, apiKey: String): List<YouTubeVideoTrack> = withContext(Dispatchers.IO) {
    val resultList = mutableListOf<YouTubeVideoTrack>()
    try {
        val urlString = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=$playlistId&key=$apiKey"
        val url = java.net.URL(urlString)
        val connection = url.openConnection() as java.net.HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 10000
        connection.readTimeout = 10000

        if (connection.responseCode == 200) {
            val stream = connection.inputStream
            val jsonString = stream.bufferedReader().use { it.readText() }
            val root = org.json.JSONObject(jsonString)
            val items = root.optJSONArray("items") ?: org.json.JSONArray()

            for (i in 0 until items.length()) {
                val item = items.optJSONObject(i) ?: continue
                val itemId = item.optString("id", "item_$i")
                val snippet = item.optJSONObject("snippet") ?: continue

                val title = snippet.optString("title", "Sem Título")
                if (title == "Private video" || title == "Deleted video") continue

                val resourceId = snippet.optJSONObject("resourceId")
                val videoId = resourceId?.optString("videoId")

                val thumbnails = snippet.optJSONObject("thumbnails")
                val thumbnailUrl = thumbnails?.optJSONObject("medium")?.optString("url")
                    ?: thumbnails?.optJSONObject("high")?.optString("url")
                    ?: thumbnails?.optJSONObject("standard")?.optString("url")
                    ?: thumbnails?.optJSONObject("default")?.optString("url")
                    ?: (if (!videoId.isNullOrBlank()) "https://img.youtube.com/vi/$videoId/hqdefault.jpg" else null)

                if (!videoId.isNullOrBlank()) {
                    resultList.add(
                        YouTubeVideoTrack(
                            id = itemId,
                            title = title,
                            videoId = videoId,
                            thumbnailUrl = thumbnailUrl,
                            duration = "Vídeo do YouTube",
                            emoji = "🎬"
                        )
                    )
                }
            }
        } else {
            Log.e("VideosScreen", "YouTube API HTTP response code: ${connection.responseCode}")
        }
    } catch (e: Exception) {
        Log.e("VideosScreen", "Error fetching YouTube playlist items", e)
    }
    resultList
}

@Composable
fun VideosScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Real YouTube Playlists without hardcoded mock video arrays
    val playlists = remember {
        listOf(
            YouTubePlaylistData(
                key = "musicas",
                name = "Músicas",
                playlistId = "PLHz1Xt0IaQWM",
                playlistUrl = "https://youtube.com/playlist?list=PLHz1Xt0IaQWM",
                emoji = "🎵",
                accentColor = SkyBluePrimary
            ),
            YouTubePlaylistData(
                key = "educativo",
                name = "Educativo",
                playlistId = "PLT7ZV5QsDKA4",
                playlistUrl = "https://youtube.com/playlist?list=PLT7ZV5QsDKA4",
                emoji = "📚",
                accentColor = KidStarOrange
            ),
            YouTubePlaylistData(
                key = "diversao",
                name = "Diversão",
                playlistId = "PLHXyMYX6Yxxc",
                playlistUrl = "https://youtube.com/playlist?list=PLHXyMYX6Yxxc",
                emoji = "🎉",
                accentColor = Color(0xFF7C4DFF)
            )
        )
    }

    var selectedPlaylistIndex by remember { mutableStateOf(0) }
    val currentPlaylist = playlists[selectedPlaylistIndex]

    // Cache of tracks fetched dynamically from YouTube Data API v3
    var playlistTracksMap by remember { mutableStateOf<Map<String, List<YouTubeVideoTrack>>>(emptyMap()) }
    var isLoadingTracks by remember { mutableStateOf(false) }

    // Selected specific video in playlist (null = playing full playlist)
    var selectedVideoId by remember { mutableStateOf<String?>(null) }

    // Dropdown menu state for header navbar
    var dropdownExpanded by remember { mutableStateOf(false) }

    // Dialog for custom URL
    var showAddDialog by remember { mutableStateOf(false) }
    var inputTitle by remember { mutableStateOf("") }
    var inputUrl by remember { mutableStateOf("") }
    val customVideos = remember { mutableStateListOf<YouTubeVideoTrack>() }

    // Fetch real playlist items from YouTube Data API
    LaunchedEffect(currentPlaylist.playlistId) {
        if (!playlistTracksMap.containsKey(currentPlaylist.playlistId)) {
            isLoadingTracks = true
            val fetchedTracks = fetchYouTubePlaylistItems(currentPlaylist.playlistId, YOUTUBE_API_KEY)
            playlistTracksMap = playlistTracksMap + (currentPlaylist.playlistId to fetchedTracks)
            isLoadingTracks = false
        }
    }

    val activeTracks = playlistTracksMap[currentPlaylist.playlistId] ?: emptyList()

    // Determine what to play in the player iframe at the top
    val activeVideoId = selectedVideoId
    val activePlaylistId = if (selectedVideoId == null) currentPlaylist.playlistId else null

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F9))
    ) {
        // --- 1. Header / Navbar (#1e293b) ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF1E293B),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Nav Dropdown "Vídeos ▼"
                Box {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF334155),
                        modifier = Modifier
                            .clickable { dropdownExpanded = true }
                            .testTag("nav_link_videos_dropdown")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Vídeos",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Submenu Vídeos",
                                tint = Color.White
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier
                            .background(Color.White)
                            .width(180.dp)
                    ) {
                        playlists.forEachIndexed { index, playlist ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(playlist.emoji, fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = playlist.name,
                                            fontWeight = if (selectedPlaylistIndex == index) FontWeight.Bold else FontWeight.Medium,
                                            color = if (selectedPlaylistIndex == index) playlist.accentColor else Color(0xFF333333)
                                        )
                                    }
                                },
                                onClick = {
                                    selectedPlaylistIndex = index
                                    selectedVideoId = null
                                    dropdownExpanded = false
                                },
                                modifier = Modifier.testTag("dropdown_item_${playlist.key}")
                            )
                        }
                    }
                }

                // Playlist Quick Selector Pills in Navbar
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    playlists.forEachIndexed { index, playlist ->
                        val isSelected = (index == selectedPlaylistIndex)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) playlist.accentColor else Color(0xFF334155),
                            modifier = Modifier.clickable {
                                selectedPlaylistIndex = index
                                selectedVideoId = null
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(playlist.emoji, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = playlist.name,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = { showAddDialog = !showAddDialog },
                        modifier = Modifier
                            .size(34.dp)
                            .background(SunshineYellow, CircleShape)
                    ) {
                        Icon(
                            imageVector = if (showAddDialog) Icons.Default.Close else Icons.Default.Add,
                            contentDescription = "Adicionar Link",
                            tint = Color(0xFF1E293B),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // --- Custom URL Add Box ---
        AnimatedVisibility(visible = showAddDialog) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, SunshineYellow)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "➕ Adicionar Vídeo do YouTube",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = inputTitle,
                        onValueChange = { inputTitle = it },
                        label = { Text("Título do Vídeo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = inputUrl,
                        onValueChange = { inputUrl = it },
                        label = { Text("URL do YouTube") },
                        placeholder = { Text("https://www.youtube.com/watch?v=...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        leadingIcon = {
                            Icon(Icons.Default.Link, contentDescription = null, tint = Color.Gray)
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            if (inputTitle.isNotBlank() && inputUrl.isNotBlank()) {
                                val parsed = parseYouTubeUrl(inputUrl)
                                if (parsed.videoId != null) {
                                    val newTrack = YouTubeVideoTrack(
                                        id = "custom_${System.currentTimeMillis()}",
                                        title = inputTitle.trim(),
                                        videoId = parsed.videoId,
                                        thumbnailUrl = "https://img.youtube.com/vi/${parsed.videoId}/hqdefault.jpg",
                                        duration = "Vídeo Adicionado",
                                        emoji = "🎬"
                                    )
                                    customVideos.add(0, newTrack)
                                    selectedVideoId = parsed.videoId
                                } else if (parsed.playlistId != null) {
                                    selectedVideoId = null
                                }
                                inputTitle = ""
                                inputUrl = ""
                                showAddDialog = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = currentPlaylist.accentColor)
                    ) {
                        Text("Guardar e Tocar ▶", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // --- Main Content Area ---
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 200.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // 2. Playlist Title (`#titulo-playlist`)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currentPlaylist.emoji,
                            fontSize = 28.sp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = currentPlaylist.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            modifier = Modifier.testTag("titulo_playlist")
                        )
                    }

                    // Button to open playlist directly in YouTube app/web
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = currentPlaylist.accentColor.copy(alpha = 0.12f),
                        modifier = Modifier.clickable {
                            openExternalUrl(context, currentPlaylist.playlistUrl)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = "Abrir no YouTube",
                                tint = currentPlaylist.accentColor,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "YouTube ↗",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = currentPlaylist.accentColor
                            )
                        }
                    }
                }
            }

            // 3. Single YouTube iFrame Player at the Top (`.player-wrapper`)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .testTag("youtube_player_wrapper"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    YouTubePlayerComposable(
                        youtubeId = activeVideoId,
                        playlistId = activePlaylistId,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // 4. Section Title (`.tracklist-title`)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Músicas da Playlist",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569)
                        )

                        if (selectedVideoId != null) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = currentPlaylist.accentColor,
                                modifier = Modifier.clickable { selectedVideoId = null }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlaylistPlay,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Tocar Playlist Inteira",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Loading state for YouTube API fetch
            if (isLoadingTracks && activeTracks.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = currentPlaylist.accentColor,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "A carregar vídeos da API do YouTube...",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Custom user videos if added
            if (customVideos.isNotEmpty()) {
                itemsIndexed(customVideos) { index, track ->
                    TrackCardItem(
                        track = track,
                        index = index + 1,
                        isSelected = (selectedVideoId == track.videoId),
                        accentColor = currentPlaylist.accentColor,
                        onClick = {
                            if (track.videoId != null) {
                                selectedVideoId = track.videoId
                            }
                        }
                    )
                }
            }

            // 5. Track Cards Grid populated DYNAMICALLY from YouTube API response
            if (!isLoadingTracks && activeTracks.isEmpty() && customVideos.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = "Toque em 'Tocar Playlist Inteira' ou selecione outra playlist no menu superior.",
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                itemsIndexed(activeTracks) { index, track ->
                    TrackCardItem(
                        track = track,
                        index = index + 1,
                        isSelected = (selectedVideoId == track.videoId),
                        accentColor = currentPlaylist.accentColor,
                        onClick = {
                            if (track.videoId != null) {
                                selectedVideoId = track.videoId
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TrackCardItem(
    track: YouTubeVideoTrack,
    index: Int,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit
) {
    val thumbnailUrl = track.thumbnailUrl ?: if (!track.videoId.isNullOrBlank()) {
        "https://img.youtube.com/vi/${track.videoId}/hqdefault.jpg"
    } else null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("track_card_${track.id}"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 0.dp,
            color = if (isSelected) accentColor else Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Thumbnail Image returned from YouTube API (`snippet.thumbnails.medium.url`)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF0F172A)),
                contentAlignment = Alignment.Center
            ) {
                if (thumbnailUrl != null) {
                    AsyncImage(
                        model = thumbnailUrl,
                        contentDescription = track.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(track.emoji, fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Vídeo $index", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Play Icon Overlay
                Surface(
                    shape = CircleShape,
                    color = if (isSelected) accentColor else Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Reproduzir",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Video Title returned from YouTube API (`snippet.title`)
            Text(
                text = "$index. ${track.title}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) accentColor else Color(0xFF1E293B),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = track.duration,
                fontSize = 11.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}

data class ParsedYouTubeUrl(
    val videoId: String? = null,
    val playlistId: String? = null
)

fun parseYouTubeUrl(rawUrl: String): ParsedYouTubeUrl {
    val cleanUrl = rawUrl.trim()

    var playlistId: String? = null
    if (cleanUrl.contains("list=")) {
        val listPart = cleanUrl.substringAfter("list=")
        playlistId = listPart.substringBefore("&").substringBefore("?")
    }

    var videoId: String? = null
    if (cleanUrl.contains("v=")) {
        val vPart = cleanUrl.substringAfter("v=")
        videoId = vPart.substringBefore("&").substringBefore("?")
    } else if (cleanUrl.contains("youtu.be/")) {
        val bePart = cleanUrl.substringAfter("youtu.be/")
        videoId = bePart.substringBefore("?").substringBefore("&")
    } else if (cleanUrl.contains("embed/")) {
        val embedPart = cleanUrl.substringAfter("embed/")
        val candidate = embedPart.substringBefore("?").substringBefore("&")
        if (candidate != "videoseries") {
            videoId = candidate
        }
    }

    return ParsedYouTubeUrl(videoId = videoId, playlistId = playlistId)
}

fun openExternalUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Log.e("VideosScreen", "Error opening URL: $url", e)
    }
}
