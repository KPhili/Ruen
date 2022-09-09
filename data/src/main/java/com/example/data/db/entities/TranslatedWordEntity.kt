package com.example.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translated_words")
data class TranslatedWordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val value: String,
    @ColumnInfo(name = "card_id")
    val cardId: Long?
)
