package com.example.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.data.db.entities.CardEntity
import com.example.data.db.entities.CardWithTranslatedWordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM cards ORDER BY next_repetition")
    fun getAll(): PagingSource<Int, CardEntity>

    @Query("SELECT * FROM cards WHERE group_id=:groupId ORDER BY next_repetition")
    fun getAllFromGroup(groupId: Long): PagingSource<Int, CardEntity>

    @Query("SELECT * FROM cards WHERE id=:id")
    suspend fun get(id: Long): CardEntity

    @Insert
    suspend fun insert(card: CardEntity): Long

    @Update
    suspend fun update(card: CardEntity)

    @Delete
    suspend fun delete(card: CardEntity)

    @Transaction
    @Query("SELECT * FROM cards WHERE id=:id")
    suspend fun getCardsWithTranslatedWords(id: Long): CardWithTranslatedWordEntity

    @Transaction
    @Query("SELECT * FROM cards WHERE next_repetition/1000 <= CAST(strftime('%s', CURRENT_TIMESTAMP)  AS  integer) ORDER BY next_repetition LIMIT 1")
    fun getNextCardForRepeat(): Flow<CardWithTranslatedWordEntity?>

    @Transaction
    @Query("SELECT * FROM cards WHERE group_id=:groupId AND next_repetition/1000 <= CAST(strftime('%s', CURRENT_TIMESTAMP)  AS  integer) ORDER BY next_repetition LIMIT 1")
    fun getNextCardForRepeatInGroup(groupId: Long): Flow<CardWithTranslatedWordEntity?>

    @Query("SELECT COUNT(*) FROM cards WHERE next_repetition/1000 <= CAST(strftime('%s', CURRENT_TIMESTAMP)  AS  integer)")
    suspend fun getCountForRepeat(): Int
}