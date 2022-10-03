package com.example.ruen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.domain.repositories.IGroupRepository

class GroupsViewModel(
    private val groupRepository: IGroupRepository
) : ViewModel() {
    val groupsFlow = groupRepository.getAll().cachedIn(viewModelScope)
}