package com.example.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MemoryCard(
    val id: Int,
    val icon: String, // Emoji representation
    val label: String,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)

data class DrawingStroke(
    val path: Path = Path(),
    val color: Color = Color.Red,
    val strokeWidth: Float = 12f
)

data class ShadowMatchItem(
    val id: String,
    val name: String,
    val emoji: String,
    var isMatched: Boolean = false
)

class GamesViewModel : ViewModel() {
    // --- Memory Game State ---
    private val memoryEmojis = listOf("🐶", "🐱", "🦁", "🐸", "🍎", "🚀", "🎨", "⭐")
    private val _memoryCards = MutableStateFlow<List<MemoryCard>>(emptyList())
    val memoryCards: StateFlow<List<MemoryCard>> = _memoryCards.asStateFlow()

    private val _matchedPairs = MutableStateFlow(0)
    val matchedPairs: StateFlow<Int> = _matchedPairs.asStateFlow()

    private var firstFlippedIndex: Int? = null
    private var isBusyFlipping = false

    // --- Drawing Canvas State ---
    private val _strokes = MutableStateFlow<List<DrawingStroke>>(emptyList())
    val strokes: StateFlow<List<DrawingStroke>> = _strokes.asStateFlow()

    private val _selectedColor = MutableStateFlow(Color(0xFFFF4081)) // Pink
    val selectedColor: StateFlow<Color> = _selectedColor.asStateFlow()

    private val _selectedBrushWidth = MutableStateFlow(16f)
    val selectedBrushWidth: StateFlow<Float> = _selectedBrushWidth.asStateFlow()

    // --- Star Counter Game State ---
    private val _starTargetCount = MutableStateFlow(4)
    val starTargetCount: StateFlow<Int> = _starTargetCount.asStateFlow()

    private val _userTappedStars = MutableStateFlow(0)
    val userTappedStars: StateFlow<Int> = _userTappedStars.asStateFlow()

    init {
        startNewMemoryGame(pairCount = 4)
    }

    // --- Memory Game Logic ---
    fun startNewMemoryGame(pairCount: Int = 4) {
        val selected = memoryEmojis.shuffled().take(pairCount)
        val cards = mutableListOf<MemoryCard>()
        var cardId = 0
        selected.forEach { emoji ->
            val emojiLabel = when (emoji) {
                "🐶" -> "Cãozinho"
                "🐱" -> "Gatinho"
                "🦁" -> "Leão"
                "🐸" -> "Sapo"
                "🍎" -> "Maçã"
                "🚀" -> "Foguetão"
                "🎨" -> "Tintas"
                "⭐" -> "Estrela"
                else -> emoji
            }
            cards.add(MemoryCard(id = cardId++, icon = emoji, label = emojiLabel))
            cards.add(MemoryCard(id = cardId++, icon = emoji, label = emojiLabel))
        }
        _memoryCards.value = cards.shuffled()
        _matchedPairs.value = 0
        firstFlippedIndex = null
        isBusyFlipping = false
    }

    fun flipMemoryCard(index: Int, onPairMatched: () -> Unit) {
        if (isBusyFlipping) return
        val currentList = _memoryCards.value
        if (index !in currentList.indices) return
        val card = currentList[index]

        if (card.isFlipped || card.isMatched) return

        val flippedCard = card.copy(isFlipped = true)
        val listWithFlipped = currentList.toMutableList().apply {
            set(index, flippedCard)
        }

        if (firstFlippedIndex == null) {
            firstFlippedIndex = index
            _memoryCards.value = listWithFlipped
        } else {
            val prevIndex = firstFlippedIndex!!
            val prevCard = currentList[prevIndex]

            if (prevCard.icon == card.icon) {
                // Match!
                val matchedPrev = prevCard.copy(isMatched = true, isFlipped = true)
                val matchedCurr = card.copy(isMatched = true, isFlipped = true)
                val matchedList = listWithFlipped.toMutableList().apply {
                    set(prevIndex, matchedPrev)
                    set(index, matchedCurr)
                }
                _memoryCards.value = matchedList
                _matchedPairs.value = _matchedPairs.value + 1
                firstFlippedIndex = null
                onPairMatched()
            } else {
                // No match, show both flipped temporarily then unflip
                _memoryCards.value = listWithFlipped
                isBusyFlipping = true
                firstFlippedIndex = null

                viewModelScope.launch {
                    delay(900)
                    val latestList = _memoryCards.value.toMutableList()
                    if (prevIndex in latestList.indices && index in latestList.indices) {
                        latestList[prevIndex] = latestList[prevIndex].copy(isFlipped = false)
                        latestList[index] = latestList[index].copy(isFlipped = false)
                        _memoryCards.value = latestList
                    }
                    isBusyFlipping = false
                }
            }
        }
    }

    // --- Drawing Canvas Logic ---
    fun addStroke(stroke: DrawingStroke) {
        _strokes.value = _strokes.value + stroke
    }

    fun clearDrawing() {
        _strokes.value = emptyList()
    }

    fun setColor(color: Color) {
        _selectedColor.value = color
    }

    fun setBrushWidth(width: Float) {
        _selectedBrushWidth.value = width
    }

    // --- Star Counter Logic ---
    fun tapStar(onSuccess: () -> Unit) {
        val next = _userTappedStars.value + 1
        _userTappedStars.value = next
        if (next == _starTargetCount.value) {
            onSuccess()
            // Reset for next round
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                _starTargetCount.value = (2..6).random()
                _userTappedStars.value = 0
            }, 1200)
        }
    }
}
