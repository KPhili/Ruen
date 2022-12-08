package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.db.converters.DateConverter
import com.example.data.db.dao.CardDao
import com.example.data.db.dao.GroupDao
import com.example.data.db.dao.TranslatedWordDao
import com.example.data.db.entities.CardEntity
import com.example.data.db.entities.GroupEntity
import com.example.data.db.entities.TranslatedWordEntity

@Database(
    entities = [CardEntity::class, TranslatedWordEntity::class, GroupEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun cardDao(): CardDao
    abstract fun translatedWordDao(): TranslatedWordDao
}