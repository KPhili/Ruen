package com.example.data.repositories

import androidx.paging.*
import com.example.data.db.dao.CardDao
import com.example.data.db.entities.CardEntity
import com.example.data.mappers.toCard
import com.example.data.mappers.toCardRoom
import com.example.domain.models.Card
import com.example.domain.repositories.ICardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class CardRepository(
    private val cardDao: CardDao
) : ICardRepository {
    override suspend fun saveCard(card: Card): Long = withContext(Dispatchers.IO) {
        cardDao.insert(card.toCardRoom())
    }

    override fun getAll(): Flow<PagingData<Card>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE, maxSize = MAX_SIZE)) { cardDao.getAll() }
            .flow
            .map { pagingData ->
                pagingData.map { cardEntity ->
                    cardEntity.toCard()
                }
            }
    }

    companion object{
        private const val MAX_SIZE = 120
        private const val PAGE_SIZE = 30
    }
}