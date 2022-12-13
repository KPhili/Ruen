package com.example.domain.repositories

import androidx.paging.PagingData
import com.example.domain.models.Group
import kotlinx.coroutines.flow.Flow

interface IGroupRepository {
    fun getAll(): Flow<PagingData<Group>>
    suspend fun getAllAsList(): List<Group>
    suspend fun save(group: Group): Long
    suspend fun update(group: Group)
    suspend fun delete(group: Group)
    suspend fun get(id: Long): Group
}