package com.example.ruen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Group
import com.example.domain.repositories.IGroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewGroupViewModel(
    private val groupRepository: IGroupRepository
) : ViewModel() {
    private val _notificationState = MutableStateFlow(UIState())
    val notificationState = _notificationState.asStateFlow()

    fun save(groupName: String) {
        viewModelScope.launch {
            _notificationState.update {
                if (groupName.isEmpty()) {
                    it.copy(notificationMessage = "Название не может быть пустым")
                } else {
                    groupRepository.save(Group(name = groupName))
                    it.copy(saveStatus = true)
                }
            }
        }
    }

    fun notificationShown() = _notificationState.update { it.copy(notificationMessage = null) }

    data class UIState(val notificationMessage: String? = null, val saveStatus: Boolean = false)
}