package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM user_progress WHERE id = 1")
    fun getUserProgress(): Flow<UserProgress?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProgress(progress: UserProgress)

    @Query("SELECT * FROM completed_items ORDER BY timestamp DESC")
    fun getCompletedItems(): Flow<List<CompletedItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletedItem(item: CompletedItem)

    @Query("SELECT COUNT(*) FROM completed_items")
    fun getCompletedCount(): Flow<Int>
}
