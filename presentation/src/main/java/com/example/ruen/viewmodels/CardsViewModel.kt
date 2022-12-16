package com.example.ruen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.domain.models.Card
import com.example.domain.repositories.ICardRepository
import com.example.domain.repositories.IGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CardsViewModel(
    private val cardsRepository: ICardRepository,
    private val groupRepository: IGroupRepository,
    private val groupId: Long
) : ViewModel() {

    private val groupName = MutableStateFlow("")

    val cardsFlow =
        cardsRepository.getAllFromGroup(groupId).cachedIn(viewModelScope)

    fun getCount(): Flow<Int> = cardsRepository.getCount(groupId)

    fun loadGroupName(): StateFlow<String> {
        viewModelScope.launch {
            groupName.value = groupRepository.getGroupName(groupId)
        }
        return groupName.asStateFlow()
    }

    fun deleteCard(card: Card) {
        viewModelScope.launch {
            cardsRepository.delete(card)
        }
    }
}