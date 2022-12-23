package com.example.domain.usecases

import com.example.domain.models.KnowLevel
import kotlin.math.max
import kotlin.math.min

class GetNextRepeatNumberUseCase {
    operator fun invoke(
        repeatNumber: Int,
        knowLevel: KnowLevel
    ) =
        when (knowLevel) {
            KnowLevel.DONT_KNOW -> min(0, repeatNumber)
            KnowLevel.BAD_KNOW -> max(1, repeatNumber - 1)
            else -> repeatNumber + 1
        }
}