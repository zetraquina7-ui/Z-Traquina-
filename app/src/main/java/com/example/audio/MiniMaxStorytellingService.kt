package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

sealed class StoryPlaybackState {
    object Idle : StoryPlaybackState()
    data class FetchingVoice(val storyTitle: String) : StoryPlaybackState()
    data class Speaking(val storyTitle: String, val textProgress: String) : StoryPlaybackState()
    data class Error(val message: String) : StoryPlaybackState()
}

/**
 * Service Layer to integrate MiniMax API to fetch & stream Zé Traquina's character voice
 * for dynamic storytelling, educational narratives, and interactive speech.
 */
class MiniMaxStorytellingService(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    private val _playbackState = MutableStateFlow<StoryPlaybackState>(StoryPlaybackState.Idle)
    val playbackState: StateFlow<StoryPlaybackState> = _playbackState.asStateFlow()

    /**
     * Synthesize and stream story audio using MiniMax API T2A (speech-01-hd model).
     * Automatically falls back to native TTS if credentials are empty or API fails.
     */
    fun tellStory(
        title: String,
        storyContent: String,
        apiKey: String,
        groupId: String,
        voiceId: String,
        onFallbackNativeTTS: (String) -> Unit
    ) {
        if (apiKey.isBlank() || voiceId.isBlank()) {
            _playbackState.value = StoryPlaybackState.Speaking(title, storyContent)
            onFallbackNativeTTS(storyContent)
            return
        }

        _playbackState.value = StoryPlaybackState.FetchingVoice(title)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val urlString = if (groupId.isNotBlank()) {
                    "https://api.minimaxi.chat/v1/t2a_v2?GroupId=$groupId"
                } else {
                    "https://api.minimaxi.chat/v1/t2a_v2"
                }

                val url = URL(urlString)
                val conn = url.openConnection() as HttpURLConnection
                try {
                    conn.requestMethod = "POST"
                    conn.setRequestProperty("Content-Type", "application/json")
                    conn.setRequestProperty("Authorization", "Bearer $apiKey")
                    conn.connectTimeout = 10000
                    conn.readTimeout = 15000
                    conn.doOutput = true

                    val payload = JSONObject().apply {
                        put("model", "speech-01-hd")
                        put("text", storyContent)
                        put("stream", true) // Request chunked streaming where supported
                        put("voice_setting", JSONObject().apply {
                            put("voice_id", voiceId)
                            put("speed", 1.0)
                            put("vol", 1.0)
                            put("pitch", 0)
                        })
                        put("audio_setting", JSONObject().apply {
                            put("sample_rate", 32000)
                            put("bitrate", 128000)
                            put("format", "mp3")
                        })
                    }

                    conn.outputStream.use { os ->
                        os.write(payload.toString().toByteArray(Charsets.UTF_8))
                    }

                    val responseCode = conn.responseCode
                    if (responseCode == 200) {
                        val tempAudioFile = File(context.cacheDir, "ze_traquina_story_${System.currentTimeMillis()}.mp3")
                        val inputStream: InputStream = conn.inputStream
                        
                        var totalRead = 0
                        FileOutputStream(tempAudioFile).use { outputStream ->
                            val buffer = ByteArray(4096)
                            var bytesRead: Int

                            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                                outputStream.write(buffer, 0, bytesRead)
                                totalRead += bytesRead
                            }
                            outputStream.flush()
                        }

                        if (totalRead > 200 && tempAudioFile.exists()) {
                            withContext(Dispatchers.Main) {
                                _playbackState.value = StoryPlaybackState.Speaking(title, storyContent)
                                playStoryAudio(tempAudioFile.absolutePath) {
                                    _playbackState.value = StoryPlaybackState.Idle
                                }
                            }
                            return@launch
                        }
                    }

                    Log.w("MiniMaxStorytelling", "API returned status $responseCode, falling back to native voice")
                    withContext(Dispatchers.Main) {
                        _playbackState.value = StoryPlaybackState.Speaking(title, storyContent)
                        onFallbackNativeTTS(storyContent)
                    }
                } finally {
                    conn.disconnect()
                }

            } catch (e: Exception) {
                Log.e("MiniMaxStorytelling", "Error fetching story voice from MiniMax", e)
                withContext(Dispatchers.Main) {
                    _playbackState.value = StoryPlaybackState.Error("Falha ao ligar ao MiniMax: ${e.localizedMessage}")
                    onFallbackNativeTTS(storyContent)
                }
            }
        }
    }

    private fun playStoryAudio(filePath: String, onComplete: () -> Unit) {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                )
                setDataSource(filePath)
                setOnCompletionListener {
                    onComplete()
                }
                setOnErrorListener { _, _, _ ->
                    onComplete()
                    true
                }
                setOnPreparedListener { 
                    start() 
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e("MiniMaxStorytelling", "Error playing story audio file", e)
            onComplete()
        }
    }

    fun stopStory() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            _playbackState.value = StoryPlaybackState.Idle
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
