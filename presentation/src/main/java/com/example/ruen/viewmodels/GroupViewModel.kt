package com.example.ruen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Group
import com.example.domain.repositories.IGroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GroupViewModel(
    private val groupRepository: IGroupRepository,
    private val groupId: Long?
) : ViewModel() {
    private val _notificationState = MutableStateFlow(UIState())
    val notificationState = _notificationState.asStateFlow()

    fun getGroupName() {
        val groupId = this.groupId ?: return
        viewModelScope.launch {
            val groupName = groupRepository.get(groupId).name
            _notificationState.update {
                it.copy(groupName = groupName)
            }
        }
    }

    fun save(groupName: String) {
        viewModelScope.launch {
            if (groupName.isEmpty()) {
                _notificationState.update {
                    it.copy(notificationMessage = "Название не может быть пустым")
                }
            } else {
                val groupId = this@GroupViewModel.groupId
                if (groupId == null) {
                    groupRepository.save(Group(name = groupName))
                } else {
                    groupRepository.update(Group(name = groupName, id = groupId))
                }
                _notificationState.update {
                    it.copy(saveStatus = true)
                }
            }
        }
    }

    fun notificationShown() = _notificationState.update { it.copy(notificationMessage = null) }

    data class UIState(
        val notificationMessage: String? = null,
        val saveStatus: Boolean = false,
        val groupName: String = ""
    )
}