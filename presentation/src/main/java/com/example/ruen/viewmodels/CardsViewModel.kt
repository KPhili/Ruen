package com.example.ruen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.domain.models.Card
import com.example.domain.repositories.ICardRepository
import kotlinx.coroutines.launch

class CardsViewModel(private val cardsRepository: ICardRepository, groupId: Long) : ViewModel() {

    val cardsFlow =
        cardsRepository.getAllFromGroup(groupId).cachedIn(viewModelScope)

    fun deleteCard(card: Card) {
        viewModelScope.launch {
            cardsRepository.delete(card)
        }
    }
}