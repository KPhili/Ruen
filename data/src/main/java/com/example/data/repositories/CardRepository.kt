package com.example.data.repositories

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.db.dao.CardDao
import com.example.data.db.entities.CardWithTranslatedWordEntity
import com.example.data.mappers.toCard
import com.example.data.mappers.toCardEntity
import com.example.data.mappers.toDomainPair
import com.example.domain.models.Card
import com.example.domain.models.TranslatedWord
import com.example.domain.repositories.ICardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class CardRepository(
    private val cardDao: CardDao
) : ICardRepository {
    override suspend fun saveCard(card: Card): Long = withContext(Dispatchers.IO) {
        cardDao.insert(card.toCardEntity())
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

    override suspend fun getCardWithTranslatedWord(cardId: Long) = withContext(Dispatchers.IO) {
        cardDao.getCardsWithTranslatedWords(cardId).toDomainPair()
    }

    override fun getAllFromGroup(groupId: Long): Flow<PagingData<Card>> {
        return Pager(
            PagingConfig(
                pageSize = PAGE_SIZE,
                maxSize = MAX_SIZE
            )
        ) { cardDao.getAllFromGroup(groupId) }
            .flow
            .map {
                it.map { cardEntity ->
                    cardEntity.toCard()
                }
            }
    }

    override fun getNextCardForRepeat(): Flow<Pair<Card, List<TranslatedWord>>?> {
        return flowMapToCardsWithTranslatedWord(cardDao.getNextCardForRepeat())
    }

    override fun getNextCardForRepeatInGroup(groupId: Long): Flow<Pair<Card, List<TranslatedWord>>?> {
        return flowMapToCardsWithTranslatedWord(cardDao.getNextCardForRepeatInGroup(groupId))
    }

    private fun flowMapToCardsWithTranslatedWord(flow: Flow<CardWithTranslatedWordEntity?>) =
        flow.map {
            it?.toDomainPair()
        }

    override suspend fun isExistForRepeat() =
        cardDao.getCountForRepeat() > 0


    override suspend fun update(card: Card) {
        cardDao.update(card.toCardEntity())
    }

    override suspend fun delete(card: Card) = withContext(Dispatchers.IO) {
        cardDao.delete(card.toCardEntity())
    }

    override suspend fun updateImage(cardId:Long, uri: String?) = withContext(Dispatchers.IO) {
        cardDao.updateImage(cardId, uri)
    }


        companion object {
        private const val MAX_SIZE = 120
        private const val PAGE_SIZE = 30
    }
}