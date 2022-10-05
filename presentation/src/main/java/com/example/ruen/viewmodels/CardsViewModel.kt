package com.example.ruen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.domain.repositories.ICardRepository

class CardsViewModel(private val cardsRepository: ICardRepository) : ViewModel() {

    fun getCardsFlow(groupId: Long) =
        cardsRepository.getAllFromGroup(groupId).cachedIn(viewModelScope)
}