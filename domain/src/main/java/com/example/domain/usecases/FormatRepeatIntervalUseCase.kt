package com.example.domain.usecases

import com.example.domain.providers.IResourceProvider
import java.lang.StringBuilder

class FormatRepeatIntervalUseCase(
    private val resources: IResourceProvider
) {
    operator fun invoke(minutes: Long): String {
        var restMinutes = minutes
        val result = StringBuilder()
        val days = restMinutes / (24 * 60)
        restMinutes -= days * 24 * 60
        val hours = restMinutes / 60
        restMinutes -= hours * 60
        if (days > 0) result.append(days.toString() + resources.getString(IResourceProvider.STRINGS.DAYS))
        if (hours > 0) result.append(hours.toString() + resources.getString(IResourceProvider.STRINGS.HOURS))
        if (restMinutes > 0 || result.isEmpty()) result.append(
            restMinutes.toString() + resources.getString(
                IResourceProvider.STRINGS.MINUTES
            )
        )
        return result.toString()
    }
}