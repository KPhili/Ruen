package com.example.data.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CardWithTranslatedWordEntity(
    @Embedded
    val cardRoom: CardEntity,
    @Relation(parentColumn = "id", entityColumn = "card_id")
    val translatedWords: List<TranslatedWordEntity>
)