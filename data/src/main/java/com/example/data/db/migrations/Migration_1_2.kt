package com.example.data.db.migrations

import android.content.ContentValues
import androidx.room.OnConflictStrategy
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration_1_2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.run {
            beginTransaction()
            execSQL(
                "CREATE TABLE groups (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL)"
            )
            val defaultGroup = ContentValues(1).apply {
                put("id", 1)
                put("name", "Общая")
            }

            insert("groups", OnConflictStrategy.IGNORE, defaultGroup)
            execSQL("ALTER TABLE cards ADD COLUMN group_id INTEGER NOT NULL DEFAULT 1;")
            execSQL("ALTER TABLE cards RENAME TO cards_temp;")
            execSQL("CREATE TABLE \"cards\" (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `value` TEXT NOT NULL, `next_repetition` INTEGER NOT NULL, `repeat_number` INTEGER NOT NULL, `group_id` INTEGER NOT NULL);")
            execSQL("INSERT INTO cards SELECT * FROM cards_temp;")
            execSQL("DROP TABLE cards_temp;")
            setTransactionSuccessful()
            endTransaction()
        }
    }
}