package com.example.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
@Entity(
    tableName = "translated_words",
    foreignKeys = [ForeignKey(entity = CardEntity::class, parentColumns = ["id"], childColumns = ["card_id"], onDelete = CASCADE)]
)
data class TranslatedWordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val value: String,

    @ColumnInfo(name = "card_id")
    val cardId: Long?
)
