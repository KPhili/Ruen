package com.example.domain.usecases

import com.example.domain.models.Card
import com.example.domain.models.TranslatedWord
import com.example.domain.repositories.ICardRepository
import com.example.domain.repositories.IImageRepository
import com.example.domain.repositories.ITranslatedWordRepository

class SaveCardWithTranslatedWordUseCase(
    private val cardRepository: ICardRepository,
    private val translatedWordRepository: ITranslatedWordRepository,
    private val imageRepository: IImageRepository,
) {
    suspend operator fun invoke(
        card: Card,
        translatedWords: List<TranslatedWord>,
        imageUrl: String? = null
    ): Long {
        val cardId = cardRepository.saveCard(card)
        val translationWordsList = translatedWords.map { it.copy(cardId = cardId) }
        translatedWordRepository.insert(translationWordsList)
        imageUrl?.let{
            val uri = imageRepository.downloadImage(imageUrl)
            cardRepository.updateImage(cardId, uri)
        }
        return cardId
    }
}