package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.ZeTraquinaChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class ChatViewModel : ViewModel() {
    private val repository by lazy { ZeTraquinaChatRepository() }

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "Olá! 🌟 Eu sou o Zé Traquina! Que bom falar contigo, amiguinho! O que queres perguntar-me hoje?",
                isFromUser = false
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(userText: String, onResponseReceived: (String) -> Unit) {
        if (userText.isBlank()) return

        val userMessage = ChatMessage(text = userText, isFromUser = true)
        _messages.value = _messages.value + userMessage
        _isLoading.value = true

        viewModelScope.launch {
            val history = _messages.value
                .drop(1) // exclude initial greeting
                .chunked(2)
                .mapNotNull { pair ->
                    if (pair.size == 2 && pair[0].isFromUser && !pair[1].isFromUser) {
                        Pair(pair[0].text, pair[1].text)
                    } else null
                }

            val responseText = repository.sendMessage(userText, history)
            val mascotMessage = ChatMessage(text = responseText, isFromUser = false)
            _messages.value = _messages.value + mascotMessage
            _isLoading.value = false
            onResponseReceived(responseText)
        }
    }
}
