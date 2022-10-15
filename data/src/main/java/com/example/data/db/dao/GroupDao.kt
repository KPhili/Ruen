package com.example.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.data.db.entities.GroupEntity

@Dao
interface GroupDao {
    @Query("SELECT * FROM groups")
    fun getAll(): PagingSource<Int, GroupEntity>

    @Query("SELECT * FROM groups WHERE id=:id")
    suspend fun get(id: Long): GroupEntity

    @Insert
    suspend fun insert(group: GroupEntity): Long

    @Update
    suspend fun update(group: GroupEntity)

    @Delete
    suspend fun delete(group: GroupEntity)
}