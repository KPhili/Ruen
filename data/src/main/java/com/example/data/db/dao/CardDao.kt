package com.example.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.data.db.entities.CardRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM cards")
    fun getAll(): Flow<List<CardRoom>>

    @Query("SELECT * FROM cards WHERE id=:id")
    suspend fun get(id: Long): CardRoom

    @Insert
    suspend fun insert(card: CardRoom): Long

    @Delete
    suspend fun delete(card: CardRoom)
}