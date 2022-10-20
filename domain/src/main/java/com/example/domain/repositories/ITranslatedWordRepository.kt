package com.example.domain.repositories

import com.example.domain.models.Card
import com.example.domain.models.TranslatedWord

interface ITranslatedWordRepository {
    suspend fun translate(word: String): List<TranslatedWord>

    suspend fun insert(translatedWord: TranslatedWord): Long

    suspend fun insert(translatedWords: List<TranslatedWord>)

    suspend fun deleteAllBelongsCard(cardId: Long)
}