package com.example.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.example.data.db.dao.GroupDao
import com.example.data.mappers.toGroup
import com.example.data.mappers.toGroupEntity
import com.example.domain.models.Group
import com.example.domain.repositories.IGroupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class GroupRepository(
    private val groupDao: GroupDao
) : IGroupRepository {
    override suspend fun save(group: Group): Long = withContext(Dispatchers.IO) {
        groupDao.insert(group.toGroupEntity())
    }

    override fun getAll() =
        Pager(PagingConfig(pageSize = PAGE_SIZE, maxSize = MAX_SIZE)) { groupDao.getAll() }
            .flow
            .map { pagingData ->
                pagingData.map { groupEntity ->
                    groupEntity.toGroup()
                }
            }

    override suspend fun update(group: Group) {
        groupDao.update(group.toGroupEntity())
    }

    companion object {
        private const val MAX_SIZE = 120
        private const val PAGE_SIZE = 30
    }
}