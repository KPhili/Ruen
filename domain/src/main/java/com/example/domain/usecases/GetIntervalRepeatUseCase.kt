package com.example.domain.usecases

import com.example.domain.utils.TimeConverter
import java.util.concurrent.TimeUnit

class GetIntervalRepeatUseCase {
    operator fun invoke(repeatNumber: Int): Long {
        return when (repeatNumber) {
            0 -> FIRST_REPEAT
            1 -> SECOND_REPEAT
            2 -> THIRD_REPEAT
            else -> TimeConverter.convertToMinutes(
                2 * (repeatNumber.toLong() - 2) + 1,
                TimeUnit.DAYS
            )
        }
    }

    companion object {
        // interval in minutes
        private const val FIRST_REPEAT = 1L
        private const val SECOND_REPEAT = 5L
        private val THIRD_REPEAT = TimeConverter.convertToMinutes(4, TimeUnit.HOURS)
    }
}