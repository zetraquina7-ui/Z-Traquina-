package com.example.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MemoryCard(
    val id: Int,
    val icon: String, // Emoji representation
    val label: String,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
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
            cards.add(MemoryCard(id = cardId++, icon = emoji, label = emoji))
            cards.add(MemoryCard(id = cardId++, icon = emoji, label = emoji))
        }
        _memoryCards.value = cards.shuffled()
        _matchedPairs.value = 0
        firstFlippedIndex = null
        isBusyFlipping = false
    }

    fun flipMemoryCard(index: Int, onPairMatched: () -> Unit) {
        if (isBusyFlipping) return
        val currentList = _memoryCards.value.toMutableList()
        val card = currentList[index]

        if (card.isFlipped || card.isMatched) return

        card.isFlipped = true
        _memoryCards.value = currentList

        if (firstFlippedIndex == null) {
            firstFlippedIndex = index
        } else {
            val prevIndex = firstFlippedIndex!!
            val prevCard = currentList[prevIndex]

            if (prevCard.icon == card.icon) {
                // Match!
                prevCard.isMatched = true
                card.isMatched = true
                _matchedPairs.value = _matchedPairs.value + 1
                firstFlippedIndex = null
                _memoryCards.value = currentList
                onPairMatched()
            } else {
                // No match, hide back after delay
                isBusyFlipping = true
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    prevCard.isFlipped = false
                    card.isFlipped = false
                    firstFlippedIndex = null
                    isBusyFlipping = false
                    _memoryCards.value = currentList.toList()
                }, 900)
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
