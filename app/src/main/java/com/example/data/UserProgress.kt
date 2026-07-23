package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey val id: Int = 1,
    val starsCount: Int = 25,
    val totalGamesPlayed: Int = 0,
    val streakDays: Int = 1,
    val timeLimitMinutes: Int = 30, // 0 = unlimited, 15, 30, 45, 60
    val ageGroup: String = "4-5", // "2-3", "4-5", "6+"
    val soundEnabled: Boolean = false,
    val childName: String = "Amiguinhos"
)

@Entity(tableName = "completed_items")
data class CompletedItem(
    @PrimaryKey val id: String, // e.g. "alphabet_A", "game_memory_easy"
    val category: String,
    val title: String,
    val timestamp: Long = System.currentTimeMillis()
)
