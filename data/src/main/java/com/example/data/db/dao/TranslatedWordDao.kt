package com.example.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.data.db.entities.TranslatedWordRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslatedWordDao {
    @Query("SELECT * FROM translated_words")
    fun getAll(): Flow<List<TranslatedWordRoom>>

    @Query("SELECT * FROM translated_words WHERE id=:id")
    suspend fun get(id: Long): TranslatedWordRoom

    @Insert
    suspend fun insert(translated_word: TranslatedWordRoom): Long

    @Delete
    suspend fun delete(translated_word: TranslatedWordRoom)
}