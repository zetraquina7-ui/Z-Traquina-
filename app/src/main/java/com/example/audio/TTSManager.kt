package com.example.audio

import android.content.Context
import android.content.SharedPreferences
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TTSManager(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isReady = false
    private val prefs: SharedPreferences = context.getSharedPreferences("ze_traquina_prefs", Context.MODE_PRIVATE)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("pt", "PT"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to default PT locale
                tts?.language = Locale("pt", "PT")
            }
            tts?.setPitch(1.25f) // Friendly, higher pitch child voice for Zé Traquina
            tts?.setSpeechRate(0.95f) // Clear pace for kids
            isReady = true
        } else {
            Log.e("TTSManager", "Native TTS Initialization failed")
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
        if (isReady && tts != null) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ze_traquina_tts")
        }
    }

    fun stop() {
        MiniMaxTTSService.stop()
        tts?.stop()
    }

    fun shutdown() {
        MiniMaxTTSService.stop()
        tts?.stop()
        tts?.shutdown()
    }
}

