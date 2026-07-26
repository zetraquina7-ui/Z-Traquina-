package com.example.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.regex.Pattern

data class YouTubeVideo(val id: String, val title: String = "Novo Vídeo")

class MediaViewModel : ViewModel() {
    private val _videos = MutableStateFlow<List<YouTubeVideo>>(
        listOf(
            YouTubeVideo("dQw4w9WgXcQ", "Zé Traquina - Aventura"),
            YouTubeVideo("3JZ_D3ELwOQ", "Música Animada")
        )
    )
    val videos: StateFlow<List<YouTubeVideo>> = _videos.asStateFlow()

    fun addYouTubeVideo(url: String) {
        val videoId = extractYouTubeId(url)
        if (videoId != null) {
            val newList = _videos.value.toMutableList()
            newList.add(YouTubeVideo(id = videoId))
            _videos.value = newList
        }
    }
    
    fun removeVideo(video: YouTubeVideo) {
        val newList = _videos.value.toMutableList()
        newList.remove(video)
        _videos.value = newList
    }

    private fun extractYouTubeId(url: String): String? {
        val pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(url)
        if (matcher.find()) {
            return matcher.group()
        }
        return null
    }
}
