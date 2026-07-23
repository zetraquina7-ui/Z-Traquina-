package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val dao: ProgressDao) {
    val userProgress: Flow<UserProgress?> = dao.getUserProgress()
    val completedItems: Flow<List<CompletedItem>> = dao.getCompletedItems()

    suspend fun updateProgress(progress: UserProgress) {
        dao.saveUserProgress(progress)
    }

    suspend fun addStars(amount: Int) {
        // Will be called when completing games or lessons
    }

    suspend fun recordCompletion(id: String, category: String, title: String) {
        dao.insertCompletedItem(CompletedItem(id = id, category = category, title = title))
    }
}
