package com.example.data.mappers

import com.example.data.db.entities.TranslatedWordEntity
import com.example.data.models.LibreTranslatedWord
import com.example.domain.models.TranslatedWord

fun LibreTranslatedWord.toTranslatedWord() = TranslatedWord(null, this.translatedText)

internal fun TranslatedWord.toTranslatedWordRoom() = TranslatedWordEntity(
    this.id,
    this.value,
    this.cardId,
)

internal fun TranslatedWordEntity.toTranslatedWord() = TranslatedWord(
    this.id,
    this.value,
    this.cardId,
)