package com.example.domain.repositories

import com.example.domain.models.Card

interface ICardRepository{
    suspend fun saveCard(card: Card): Long
}