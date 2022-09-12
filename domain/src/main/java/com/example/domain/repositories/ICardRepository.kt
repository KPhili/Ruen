package com.example.domain.repositories

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.domain.models.Card
import com.example.domain.models.TranslatedWord
import kotlinx.coroutines.flow.Flow

interface ICardRepository {
    suspend fun saveCard(card: Card): Long

    fun getAll(): Flow<PagingData<Card>>

    suspend fun getNextCardForRepeat(): Pair<Card, List<TranslatedWord>>
}