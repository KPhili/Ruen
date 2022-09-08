package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.db.dao.CardDao
import com.example.data.db.dao.TranslatedWordDao
import com.example.data.db.entities.CardRoom
import com.example.data.db.entities.TranslatedWordRoom

@Database(
    entities = [CardRoom::class, TranslatedWordRoom::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun translatedWordDao(): TranslatedWordDao
}