package com.example.data.db.entities

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.time.LocalDateTime

@Entity(
    tableName = "cards",
    foreignKeys = [ForeignKey(
        entity = GroupEntity::class,
        parentColumns = ["id"],
        childColumns = ["group_id"],
        onDelete = CASCADE
    ),],
    indices = [Index(value = ["group_id"])]
)
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val value: String,
    @ColumnInfo(name = "next_repetition")
    val nextRepetition: LocalDateTime,
    @ColumnInfo(name = "repeat_number")
    val repeatNumber: Int,
    @ColumnInfo(name = "group_id")
    val groupId: Long,
    @ColumnInfo(name = "image_file_name")
    val imageFileName: String? = null,
)