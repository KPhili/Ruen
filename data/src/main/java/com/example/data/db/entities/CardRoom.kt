package com.example.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val value: String,
)