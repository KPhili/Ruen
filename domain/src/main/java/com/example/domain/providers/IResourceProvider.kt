package com.example.domain.providers

interface IResourceProvider {
    fun getString(resource: STRINGS): String

    enum class STRINGS {
        SAVE_CARD_SUCCESS,
        FIELD_WORD_IS_EMPTY,
        LIST_OF_TRANSLATION_IS_EMPTY,
        // сокращения для интервалов
        DAYS,
        HOURS,
        MINUTES
    }
}