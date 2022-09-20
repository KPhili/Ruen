package com.example.domain.usecases

import com.example.domain.models.KnowLevel
import kotlin.math.max

class GetNextRepeatNumberUseCase {
    operator fun invoke(
        repeatNumber: Int,
        knowLevel: KnowLevel
    ) =
        when (knowLevel) {
            KnowLevel.DONT_KNOW -> max(0, repeatNumber - 1)
            KnowLevel.BAD_KNOW -> repeatNumber
            else -> repeatNumber + 1
        }
}