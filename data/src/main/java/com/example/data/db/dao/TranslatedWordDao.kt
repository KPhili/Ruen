package com.example.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.data.db.entities.TranslatedWordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslatedWordDao {
    @Query("SELECT * FROM translated_words")
    fun getAll(): Flow<List<TranslatedWordEntity>>

    @Query("SELECT * FROM translated_words WHERE id=:id")
    suspend fun get(id: Long): TranslatedWordEntity

    @Insert
    suspend fun insert(translatedWord: TranslatedWordEntity): Long

    @Insert
    suspend fun insert(translatedWords: List<TranslatedWordEntity>)

    @Delete
    suspend fun delete(translated_word: TranslatedWordEntity)

    @Query("DELETE FROM translated_words WHERE card_id=:cardId")
    suspend fun deleteAllBelongsCard(cardId: Long)
}