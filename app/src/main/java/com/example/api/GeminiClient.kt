package com.example.api

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ZeTraquinaChatRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(25, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .build()

    private val fallbackResponses = listOf(
        "Olá, amiguinhos! 🌟 Hoje é um dia fantástico para aprender e brincar juntos! O que queres fazer agora?",
        "Que fixe falares comigo! 🚀 Sabias que as estrelas brilham no céu porque estão cheias de energia como tu?",
        "Eu adoro cantar e desenhar! 🎨 Qual é o teu animal preferido de todos?",
        "Muito bem! ⭐ És um miúdo super esperto e empenhado! Vamos continuar a explorar o Universo Zé Traquina!",
        "Ah ah ah, és muito divertido! 🎈 Vamos cantar uma canção do ABC?"
    )

    suspend fun sendMessage(userMessage: String, history: List<Pair<String, String>>): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getFallbackResponse(userMessage)
        }

        try {
            val rootJson = JSONObject()
            
            // System instruction
            val systemInstr = JSONObject()
            val sysParts = JSONArray()
            val sysPart = JSONObject().put("text", 
                "Tu és o Zé Traquina, o mascote alegre e brincalhão da aplicação de educação infantil 'Universo Zé Traquina'. " +
                "A tua missão é conversar com crianças de 2 a 6 anos em Português de Portugal (pt-PT) com muito carinho, otimismo e vocabulário simples e correto de Portugal. " +
                "NUNCA uses Português do Brasil. Usa palavras e expressões típicas de Portugal (como 'fixe', 'espetacular', 'miúdo', 'autocarro', 'chávena', 'tu queres', 'falares comigo', etc., sem usar 'você' nem 'legal' nem 'kkkk'). " +
                "Responde com frases curtas (máximo 2 a 3 frases), usando muitos emojis divertidos (⭐, 🎈, 🚀, 🎨, 🦁). " +
                "Ensina coisas giras como cores, números, letras e bons hábitos com entusiasmo!"
            )
            sysParts.put(sysPart)
            systemInstr.put("parts", sysParts)
            rootJson.put("systemInstruction", systemInstr)

            // Contents history + new query
            val contentsArray = JSONArray()
            history.takeLast(4).forEach { (user, model) ->
                val userObj = JSONObject().put("parts", JSONArray().put(JSONObject().put("text", user)))
                val modelObj = JSONObject().put("parts", JSONArray().put(JSONObject().put("text", model)))
                contentsArray.put(userObj)
                contentsArray.put(modelObj)
            }

            val currentObj = JSONObject().put("parts", JSONArray().put(JSONObject().put("text", userMessage)))
            contentsArray.put(currentObj)

            rootJson.put("contents", contentsArray)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = rootJson.toString().toRequestBody(mediaType)

            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext getFallbackResponse(userMessage)
                }
                val bodyStr = response.body?.string() ?: ""
                val resObj = JSONObject(bodyStr)
                val candidates = resObj.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val contentObj = firstCandidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val reply = parts.getJSONObject(0).optString("text")
                        if (reply.isNotBlank()) {
                            return@withContext reply
                        }
                    }
                }
            }
            getFallbackResponse(userMessage)
        } catch (e: Exception) {
            getFallbackResponse(userMessage)
        }
    }

    private fun getFallbackResponse(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("oi") || lower.contains("olá") -> "Olá, amiguinho! 🎈 Que bom ver-te por aqui! Vamos aprender coisas incríveis juntos?"
            lower.contains("piada") || lower.contains("engraçad") -> "Sabes o que o zero disse para o oito? 😃 'Que cinto fixe esse que estás a usar!' Ah ah ah!"
            lower.contains("história") || lower.contains("historinha") -> "Era uma vez uma estrelinha chamada Bibi que adorava pular nas nuvens fofinhas de algodão doce! ⭐ Ela acenou para ti!"
            lower.contains("música") || lower.contains("canta") -> "🎵 Brilha, brilha estrelinha, quero ver-te a brilhar! Lá no alto do céu, como um diamante a piscar! ✨"
            else -> fallbackResponses.random()
        }
    }
}
