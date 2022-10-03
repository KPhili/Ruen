package com.example.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val value: String,
    @ColumnInfo(name = "next_repetition")
    val nextRepetition: LocalDateTime,
    @ColumnInfo(name = "repeat_number")
    val repeatNumber: Int,
//    @ColumnInfo(name = "group_id")
//    val groupId: Long
)