package com.example.data.mappers

import com.example.data.db.entities.CardEntity
import com.example.domain.models.Card

internal fun Card.toCardRoom() = CardEntity(
    this.id,
    this.value,
    this.nextRepetition,
    this.repeatNumber,

)

internal fun CardEntity.toCard() = Card(
    this.id,
    this.value,
    this.nextRepetition,
    this.repeatNumber
)