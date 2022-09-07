package com.example.data.datasource

import com.example.data.apiservices.libre.LibreTranslateQueryBody
import com.example.data.apiservices.libre.LibreTranslateService

class LibreWordTranslationRemoteSource(private val translateService: LibreTranslateService) {
    suspend fun translate(
        word: String,
        sourceLang: String = "en",
        targetLang: String = "ru"
    ) = translateService.translate(
        LibreTranslateQueryBody(word, targetLang, sourceLang)
    )
}