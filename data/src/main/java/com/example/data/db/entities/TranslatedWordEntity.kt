package com.example.data.db.entities

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "translated_words",
    foreignKeys = [ForeignKey(
        entity = CardEntity::class,
        parentColumns = ["id"],
        childColumns = ["card_id"],
        onDelete = CASCADE
    )],
    indices = [Index(value = ["card_id"])]
)
data class TranslatedWordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val value: String,

    @ColumnInfo(name = "card_id")
    val cardId: Long?
)
