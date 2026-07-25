package com.example.audio

import android.content.Context
import android.content.SharedPreferences
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class TTSManager(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isReady = false
    private val prefs: SharedPreferences = context.getSharedPreferences("ze_traquina_prefs", Context.MODE_PRIVATE)

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Throwable) {
            Log.e("TTSManager", "Error initializing TextToSpeech", e)
        }
    }

    override fun onInit(status: Int) {
        try {
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.forLanguageTag("pt-PT"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.language = Locale.forLanguageTag("pt-PT")
                }
                tts?.setPitch(1.25f)
                tts?.setSpeechRate(0.95f)
                isReady = true
            } else {
                Log.e("TTSManager", "Native TTS Initialization failed with status: $status")
            }
        } catch (e: Throwable) {
            Log.e("TTSManager", "Error in onInit", e)
        }
    }

    fun speak(text: String) {
        val apiKey = prefs.getString("minimax_api_key", "") ?: ""
        val groupId = prefs.getString("minimax_group_id", "") ?: ""
        val voiceId = prefs.getString("minimax_voice_id", "") ?: ""

        if (apiKey.isNotBlank() && voiceId.isNotBlank()) {
            MiniMaxTTSService.speakMiniMax(
                context = context,
                text = text,
                apiKey = apiKey,
                groupId = groupId,
                voiceId = voiceId,
                onFallback = {
                    speakNative(text)
                }
            )
        } else {
            speakNative(text)
        }
    }

    private fun speakNative(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (isReady && tts != null) {
                    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ze_traquina_tts")
                }
            } catch (e: Throwable) {
                Log.e("TTSManager", "Error during speakNative", e)
            }
        }
    }

    fun stop() {
        try {
            MiniMaxTTSService.stop()
            tts?.stop()
        } catch (e: Throwable) {
            Log.e("TTSManager", "Error stopping TTS", e)
        }
    }

    fun shutdown() {
        try {
            MiniMaxTTSService.stop()
            tts?.stop()
            tts?.shutdown()
        } catch (e: Throwable) {
            Log.e("TTSManager", "Error shutting down TTS", e)
        }
    }
}

