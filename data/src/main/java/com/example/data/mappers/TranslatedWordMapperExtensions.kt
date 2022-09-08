package com.example.data.mappers

import com.example.data.models.LibreTranslatedWord
import com.example.domain.models.TranslatedWord

fun LibreTranslatedWord.toTranslatedWord() = TranslatedWord(this.translatedText)