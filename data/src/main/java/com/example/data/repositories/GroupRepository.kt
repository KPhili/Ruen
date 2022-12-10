package com.example.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.example.data.db.dao.CardDao
import com.example.data.db.dao.GroupDao
import com.example.data.mappers.toGroup
import com.example.data.mappers.toGroupEntity
import com.example.domain.models.Group
import com.example.domain.repositories.IGroupRepository
import com.example.domain.repositories.IImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class GroupRepository(
    private val groupDao: GroupDao,
    private val cardDao: CardDao,
    private val imageRepository: IImageRepository
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

    override suspend fun get(id: Long) = withContext(Dispatchers.IO) {
        groupDao.get(id).toGroup()
    }

    override suspend fun update(group: Group) = withContext(Dispatchers.IO) {
        groupDao.update(group.toGroupEntity())
    }

    override suspend fun delete(group: Group) = withContext(Dispatchers.IO) {
        val groupId = group.id ?: throw Exception("Can't delete a group without an id")
        // удаление всех изображений из локального хранилища прежде чем удалить карточки
        cardDao.getAllImageFileNamesByGroup(groupId).forEach { imageFileName ->
            imageRepository.deleteImage(imageFileName)
        }
        groupDao.delete(group.toGroupEntity())
    }

    companion object {
        private const val MAX_SIZE = 120
        private const val PAGE_SIZE = 30
    }
}