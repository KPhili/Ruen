package com.example.domain.usecases

import com.example.domain.models.Card
import com.example.domain.models.TranslatedWord
import com.example.domain.repositories.ICardRepository
import com.example.domain.repositories.IImageRepository
import com.example.domain.repositories.ITranslatedWordRepository

class SaveCardWithTranslatedWordUseCase(
    private val cardRepository: ICardRepository,
    private val translatedWordRepository: ITranslatedWordRepository,
) {
    suspend operator fun invoke(
        card: Card,
        translatedWords: List<TranslatedWord>,
    ): Long {
        val cardId = cardRepository.saveCard(card)
        val translationWordsList = translatedWords.map { it.copy(cardId = cardId) }
        translatedWordRepository.insert(translationWordsList)
        return cardId
    }
}