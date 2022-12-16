package com.example.domain.repositories

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.domain.models.Card
import com.example.domain.models.TranslatedWord
import kotlinx.coroutines.flow.Flow

interface ICardRepository {
    suspend fun saveCard(card: Card): Long

    fun getAll(): Flow<PagingData<Card>>

    fun getAllFromGroup(groupId: Long): Flow<PagingData<Card>>

    suspend fun getCardWithTranslatedWord(cardId: Long): Pair<Card, List<TranslatedWord>>

    fun getNextCardForRepeat(): Flow<Pair<Card, List<TranslatedWord>>?>

    fun getNextCardForRepeatInGroup(groupId: Long): Flow<Pair<Card, List<TranslatedWord>>?>

    suspend fun isExistForRepeat(): Boolean

    suspend fun update(card: Card)

    suspend fun delete(card: Card)

    suspend fun updateImage(cardId: Long, uri: String?)

    suspend fun getImageFileName(cardId: Long): String?
    fun getCount(groupId: Long): Flow<Int>
}