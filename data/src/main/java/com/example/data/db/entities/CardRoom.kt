package com.example.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "cards")
data class CardRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val value: String,
    @ColumnInfo(name = "next_repetition")
    val nextRepetition: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "repeat_number")
    val repeatNumber: Int = 0
)