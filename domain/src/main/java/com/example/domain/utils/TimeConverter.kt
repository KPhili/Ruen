package com.example.domain.utils

import java.util.concurrent.TimeUnit

object TimeConverter {
    fun convertToMinutes(value: Long, type: TimeUnit) = when (type) {
        TimeUnit.MINUTES -> value
        TimeUnit.HOURS -> value * 60
        TimeUnit.DAYS -> value * 24
        else -> throw IllegalArgumentException()
    }
}