package com.example.ruen.providers

import android.content.Context
import com.example.ruen.R
import com.example.ruen.providers.IResourceProvider.STRINGS.*

class ResourceProvider(
    private val appContext: Context
) : IResourceProvider {

    override fun getString(resource: IResourceProvider.STRINGS): String = when (resource) {
        SAVE_CARD_SUCCESS -> appContext.resources.getString(R.string.save_card_success)
        FIELD_WORD_IS_EMPTY -> appContext.resources.getString(R.string.no_translations)
        LIST_OF_TRANSLATION_IS_EMPTY -> appContext.resources.getString(R.string.no_translations)
    }
}