package com.example.ruen.providers

import android.content.Context
import com.example.domain.providers.IResourceProvider
import com.example.ruen.R
import com.example.domain.providers.IResourceProvider.STRINGS.*

class ResourceProvider(
    private val appContext: Context
) : IResourceProvider {

    override fun getString(resource: IResourceProvider.STRINGS): String =
        appContext.resources.getString(
            when (resource) {
                SAVE_CARD_SUCCESS -> R.string.save_card_success
                FIELD_WORD_IS_EMPTY -> R.string.field_word_is_empty
                LIST_OF_TRANSLATION_IS_EMPTY -> R.string.no_translations
                NO_INTERNET_CONNECTION -> R.string.no_internet_connection
                DAYS -> R.string.days
                HOURS -> R.string.hours
                MINUTES -> R.string.minutes
            }
        )
}