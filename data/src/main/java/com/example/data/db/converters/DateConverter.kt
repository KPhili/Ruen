package com.example.data.db.converters

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? = value?.let {
        LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
    }

    @TypeConverter
    fun dateToTimestamp(dateTime: LocalDateTime?): Long? =
        dateTime?.let {
            dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }
}