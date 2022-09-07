package com.example.data.repositories

import com.example.data.datasource.LibreWordTranslationRemoteSource
import com.example.data.models.LibreTranslatedWord
import com.example.domain.models.TranslatedWord

class WordTranslationRepository(private val remoteSource: LibreWordTranslationRemoteSource) {
    suspend fun translate(word: String): List<TranslatedWord> {
        val translateWord = remoteSource.translate(word).toTranslatedWord()
        return listOf(translateWord)
    }
}

fun LibreTranslatedWord.toTranslatedWord() = TranslatedWord(this.translatedText)

