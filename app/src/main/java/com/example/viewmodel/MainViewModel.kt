package com.example.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.MiniMaxStorytellingService
import com.example.audio.StoryPlaybackState
import com.example.audio.TTSManager
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.data.UserProgress
import com.example.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository(AppDatabase.getDatabase(application).progressDao())
    val ttsManager by lazy { TTSManager(application) }
    val storytellingService by lazy { MiniMaxStorytellingService(application) }

    var currentScreen by mutableStateOf<Screen>(Screen.Home)
        private set

    fun navigateTo(screen: Screen) {
        currentScreen = screen
    }

    val storyPlaybackState: StateFlow<StoryPlaybackState> = storytellingService.playbackState

    private val _userProgress = MutableStateFlow(UserProgress())
    val userProgress: StateFlow<UserProgress> = _userProgress.asStateFlow()

    private val _isTimeLimitReached = MutableStateFlow(false)
    val isTimeLimitReached: StateFlow<Boolean> = _isTimeLimitReached.asStateFlow()

    init {
        viewModelScope.launch {
            repository.userProgress.collectLatest { progress ->
                if (progress != null) {
                    _userProgress.value = progress
                } else {
                    // Initialize default progress
                    val defaultProg = UserProgress()
                    repository.updateProgress(defaultProg)
                    _userProgress.value = defaultProg
                }
            }
        }
    }

    fun addStars(amount: Int) {
        viewModelScope.launch {
            val current = _userProgress.value
            val updated = current.copy(starsCount = current.starsCount + amount)
            _userProgress.value = updated
            repository.updateProgress(updated)
        }
    }

    fun recordGamePlayed() {
        viewModelScope.launch {
            val current = _userProgress.value
            val updated = current.copy(
                totalGamesPlayed = current.totalGamesPlayed + 1,
                starsCount = current.starsCount + 5
            )
            _userProgress.value = updated
            repository.updateProgress(updated)
        }
    }

    fun updateParentSettings(childName: String, ageGroup: String, timeLimitMinutes: Int, soundEnabled: Boolean) {
        viewModelScope.launch {
            val updated = _userProgress.value.copy(
                childName = childName,
                ageGroup = ageGroup,
                timeLimitMinutes = timeLimitMinutes,
                soundEnabled = soundEnabled
            )
            _userProgress.value = updated
            repository.updateProgress(updated)
        }
    }

    fun speak(text: String) {
        if (_userProgress.value.soundEnabled) {
            ttsManager.speak(text)
        }
    }

    fun tellStory(title: String, storyContent: String) {
        if (!_userProgress.value.soundEnabled) return

        val prefs = getApplication<Application>().getSharedPreferences("ze_traquina_prefs", android.content.Context.MODE_PRIVATE)
        val apiKey = prefs.getString("minimax_api_key", "") ?: ""
        val groupId = prefs.getString("minimax_group_id", "") ?: ""
        val voiceId = prefs.getString("minimax_voice_id", "") ?: ""

        storytellingService.tellStory(
            title = title,
            storyContent = storyContent,
            apiKey = apiKey,
            groupId = groupId,
            voiceId = voiceId,
            onFallbackNativeTTS = { text ->
                ttsManager.speak(text)
            }
        )
    }

    fun stopStory() {
        storytellingService.stopStory()
        ttsManager.stop()
    }

    override fun onCleared() {
        super.onCleared()
        storytellingService.stopStory()
        ttsManager.shutdown()
    }
}
