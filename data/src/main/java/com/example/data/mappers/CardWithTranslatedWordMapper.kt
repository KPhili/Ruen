package com.example.data.mappers

import com.example.data.db.entities.CardWithTranslatedWordEntity

internal fun CardWithTranslatedWordEntity.toDomainPair() =
    Pair(this.cardRoom.toCard(), this.translatedWords.map { it.toTranslatedWord() })