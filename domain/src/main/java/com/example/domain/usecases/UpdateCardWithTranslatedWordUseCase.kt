package com.example.domain.usecases

import com.example.domain.models.Card
import com.example.domain.models.TranslatedWord
import com.example.domain.repositories.ICardRepository
import com.example.domain.repositories.ITranslatedWordRepository

class UpdateCardWithTranslatedWordUseCase(
    private val cardRepository: ICardRepository,
    private val translatedWordRepository: ITranslatedWordRepository
) {
    suspend operator fun invoke(card: Card, translatedWords: List<TranslatedWord>): Long {
        card.id ?: throw NullPointerException("card.id can't equals null for update")
        cardRepository.update(card)
        translatedWordRepository.deleteAllBelongsCard(card.id)
        val translationWordsList = translatedWords.map { it.copy(cardId = card.id) }
        translatedWordRepository.insert(translationWordsList)
        return card.id
    }
}