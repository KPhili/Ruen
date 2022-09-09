package com.example.data.db.dao

import androidx.room.*
import com.example.data.db.entities.CardEntity
import com.example.data.db.entities.CardWithTranslatedWordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM cards")
    fun getAll(): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE id=:id")
    suspend fun get(id: Long): CardEntity

    @Insert
    suspend fun insert(card: CardEntity): Long

    @Delete
    suspend fun delete(card: CardEntity)

    @Transaction
    @Query("SELECT * FROM cards")
    fun getCardsWithTranslatedWords(): Flow<List<CardWithTranslatedWordEntity>>

    @Transaction
    @Query("SELECT * FROM cards WHERE id=:id")
    fun getCardsWithTranslatedWords(id: Long): Flow<List<CardWithTranslatedWordEntity>>
}