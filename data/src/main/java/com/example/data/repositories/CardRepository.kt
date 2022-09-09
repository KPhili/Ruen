package com.example.data.repositories

import com.example.data.db.dao.CardDao
import com.example.data.mappers.toCardRoom
import com.example.domain.models.Card
import com.example.domain.repositories.ICardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CardRepository(
    private val roomCardDao: CardDao
) : ICardRepository {
    override suspend fun saveCard(card: Card): Long  = withContext(Dispatchers.IO){
        roomCardDao.insert(card.toCardRoom())
    }
}