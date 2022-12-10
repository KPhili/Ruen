package com.example.domain.usecases

import com.example.domain.utils.TimeConverter
import java.util.concurrent.TimeUnit

class GetIntervalRepeatUseCase {
    operator fun invoke(repeatNumber: Int): Long {
        return when (repeatNumber) {
            0 -> FIRST_REPEAT
            1 -> SECOND_REPEAT
            2 -> THIRD_REPEAT
            3 -> FOUR_REPEAT
            else -> 2 * invoke(repeatNumber - 1) + TimeConverter.convertToMinutes(1, TimeUnit.DAYS)
        }
    }

    companion object {
        // interval in minutes
        private const val FIRST_REPEAT = 1L
        private const val SECOND_REPEAT = 5L
        private val THIRD_REPEAT = TimeConverter.convertToMinutes(4, TimeUnit.HOURS)
        private val FOUR_REPEAT = TimeConverter.convertToMinutes(1, TimeUnit.DAYS)
    }
}