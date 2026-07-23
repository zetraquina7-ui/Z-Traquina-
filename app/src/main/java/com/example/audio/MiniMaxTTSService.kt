package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

object MiniMaxTTSService {
    private var mediaPlayer: MediaPlayer? = null

    fun speakMiniMax(
        context: Context,
        text: String,
        apiKey: String,
        groupId: String,
        voiceId: String,
        onFallback: () -> Unit
    ) {
        if (apiKey.isBlank() || voiceId.isBlank()) {
            onFallback()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val urlString = if (groupId.isNotBlank()) {
                    "https://api.minimaxi.chat/v1/t2a_v2?GroupId=$groupId"
                } else {
                    "https://api.minimaxi.chat/v1/t2a_v2"
                }

                val url = URL(urlString)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Authorization", "Bearer $apiKey")
                conn.connectTimeout = 8000
                conn.readTimeout = 8000
                conn.doOutput = true

                val payload = JSONObject().apply {
                    put("model", "speech-01-hd")
                    put("text", text)
                    put("stream", false)
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
                    val responseBytes = conn.inputStream.readBytes()

                    if (responseBytes.size > 200) {
                        val tempAudioFile = File(context.cacheDir, "minimax_tts_temp.mp3")
                        FileOutputStream(tempAudioFile).use { fos ->
                            fos.write(responseBytes)
                        }

                        withContext(Dispatchers.Main) {
                            playAudioFile(tempAudioFile.absolutePath)
                        }
                        return@launch
                    }
                }

                withContext(Dispatchers.Main) {
                    onFallback()
                }
            } catch (e: Exception) {
                Log.e("MiniMaxTTS", "Failed to synthesize speech via MiniMax.io API", e)
                withContext(Dispatchers.Main) {
                    onFallback()
                }
            }
        }
    }

    private fun playAudioFile(filePath: String) {
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
                prepare()
                start()
            }
        } catch (e: Exception) {
            Log.e("MiniMaxTTS", "Error playing generated MiniMax audio file", e)
        }
    }

    fun stop() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
