package com.example.data.mappers

import com.example.data.db.entities.GroupEntity
import com.example.domain.models.Group

internal fun Group.toGroupEntity() = GroupEntity(
    this.id,
    this.name
)

internal fun GroupEntity.toGroup() = Group(
    this.id,
    this.name
)