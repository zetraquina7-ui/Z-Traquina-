package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.sin

object AudioSynthesizer {
    private const val SAMPLE_RATE = 22050
    private var playbackJob: Job? = null
    private var sharedAudioTrack: AudioTrack? = null

    @Synchronized
    private fun getOrCreateAudioTrack(): AudioTrack? {
        try {
            if (sharedAudioTrack == null || sharedAudioTrack?.state != AudioTrack.STATE_INITIALIZED) {
                val minBufferSize = AudioTrack.getMinBufferSize(
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                sharedAudioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(SAMPLE_RATE)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(Math.max(minBufferSize, 8192))
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()
                sharedAudioTrack?.play()
            }
            return sharedAudioTrack
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    @Synchronized
    fun releaseAudioTrack() {
        try {
            sharedAudioTrack?.stop()
            sharedAudioTrack?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            sharedAudioTrack = null
        }
    }

    // Frequencies for C4 octave
    val NOTE_FREQUENCIES = mapOf(
        "Do" to 261.63,  // C4
        "Re" to 293.66,  // D4
        "Mi" to 329.63,  // E4
        "Fa" to 349.23,  // F4
        "Sol" to 392.00, // G4
        "La" to 440.00,  // A4
        "Si" to 493.88,  // B4
        "Do2" to 523.25  // C5
    )

    suspend fun playNoteSync(freqHz: Double, durationMs: Int = 350) = withContext(Dispatchers.IO) {
        try {
            val track = getOrCreateAudioTrack() ?: return@withContext
            val numSamples = (durationMs * SAMPLE_RATE) / 1000
            val sample = DoubleArray(numSamples)
            val generatedSnd = ByteArray(2 * numSamples)

            // Warm musical tone generation with harmonic + decay
            for (i in 0 until numSamples) {
                val time = i.toDouble() / SAMPLE_RATE
                val envelope = Math.exp(-time * 3.5)
                val signal = sin(2.0 * Math.PI * freqHz * time) * 0.7 + sin(4.0 * Math.PI * freqHz * time) * 0.3
                sample[i] = signal * envelope
            }

            // Convert to 16-bit PCM
            var idx = 0
            for (dVal in sample) {
                val valInt = (dVal * 32767).toInt().coerceIn(-32768, 32767)
                generatedSnd[idx++] = (valInt and 0x00ff).toByte()
                generatedSnd[idx++] = (valInt and 0xff00 shr 8).toByte()
            }

            track.write(generatedSnd, 0, generatedSnd.size)
            delay((durationMs * 0.85).toLong())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playNote(freqHz: Double, durationMs: Int = 350) {
        CoroutineScope(Dispatchers.IO).launch {
            playNoteSync(freqHz, durationMs)
        }
    }

    fun stopMelody() {
        playbackJob?.cancel()
        playbackJob = null
        releaseAudioTrack()
    }

    fun playSongMelody(songIndex: Int, loop: Boolean = false) {
        stopMelody()
        playbackJob = CoroutineScope(Dispatchers.IO).launch {
            val sequence = when (songIndex % 10) {
                0 -> listOf( // 1. Férias de verão
                    "Do" to 250, "Mi" to 250, "Sol" to 250, "Do2" to 400,
                    "Sol" to 250, "Mi" to 250, "Do" to 500,
                    "Fa" to 250, "La" to 250, "Do2" to 250, "Sol" to 500
                )
                1 -> listOf( // 2. Marcha dos santos populares
                    "Sol" to 200, "Sol" to 200, "La" to 300, "Sol" to 300,
                    "Do2" to 400, "Si" to 400, "La" to 300, "Sol" to 500
                )
                2 -> listOf( // 3. Dia da criança
                    "Do" to 200, "Re" to 200, "Mi" to 200, "Fa" to 200,
                    "Sol" to 400, "Sol" to 400, "La" to 200, "La" to 200,
                    "Sol" to 600
                )
                3 -> listOf( // 4. Dia da família
                    "Mi" to 300, "Sol" to 300, "Do2" to 400, "Si" to 300,
                    "La" to 300, "Sol" to 500, "Fa" to 300, "Mi" to 500
                )
                4 -> listOf( // 5. Animais do ABC (ABC Song)
                    "Do" to 300, "Do" to 300, "Sol" to 300, "Sol" to 300,
                    "La" to 300, "La" to 300, "Sol" to 600,
                    "Fa" to 300, "Fa" to 300, "Mi" to 300, "Mi" to 300,
                    "Re" to 300, "Re" to 300, "Do" to 600
                )
                5 -> listOf( // 6. Um coração para ti
                    "Mi" to 250, "Fa" to 250, "Sol" to 400, "Do2" to 400,
                    "Si" to 300, "La" to 300, "Sol" to 500
                )
                6 -> listOf( // 7. Juntos somos o mundo
                    "Sol" to 300, "Do2" to 300, "Do2" to 300, "Si" to 300,
                    "La" to 300, "Sol" to 500, "Fa" to 300, "Mi" to 300, "Re" to 500
                )
                7 -> listOf( // 8. A luz de Jesus venceu
                    "Do" to 250, "Mi" to 250, "Sol" to 300, "Sol" to 300,
                    "La" to 300, "Sol" to 300, "Do2" to 600
                )
                8 -> listOf( // 9. Festa de carnaval
                    "Do" to 180, "Re" to 180, "Mi" to 180, "Fa" to 180, "Sol" to 180,
                    "La" to 180, "Si" to 180, "Do2" to 350, "Sol" to 350, "Do2" to 500
                )
                else -> listOf( // 10. É Natal (Jingle Bells)
                    "Mi" to 250, "Mi" to 250, "Mi" to 500,
                    "Mi" to 250, "Mi" to 250, "Mi" to 500,
                    "Mi" to 250, "Sol" to 250, "Do" to 350, "Re" to 250, "Mi" to 700
                )
            }
            do {
                for ((note, dur) in sequence) {
                    val freq = NOTE_FREQUENCIES[note] ?: 261.63
                    playNoteSync(freq, dur)
                    delay(30)
                }
                if (loop) delay(400)
            } while (loop)
        }
    }

    fun playKidsSongMelody() {
        playSongMelody(4, false)
    }
}
