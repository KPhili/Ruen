package com.example.ruen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.domain.models.Group
import com.example.domain.repositories.IGroupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupsViewModel(
    private val groupRepository: IGroupRepository
) : ViewModel() {

    val groupsFlow = groupRepository.getAll().cachedIn(viewModelScope)

    fun deleteGroup(group: Group) {
        viewModelScope.launch {
            groupRepository.delete(group)
        }
    }
}