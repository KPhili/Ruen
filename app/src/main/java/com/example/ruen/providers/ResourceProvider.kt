package com.example.ruen.providers

import android.content.Context
import com.example.ruen.R

class ResourceProvider(
    private val appContext: Context
) : IResourceProvider {

    override fun getString(resource: IResourceProvider.STRINGS): String = when (resource) {
        IResourceProvider.STRINGS.SAVE_CARD_SUCCESS -> appContext.resources.getString(R.string.save_card_success)
        IResourceProvider.STRINGS.FIELD_WORD_IS_EMPTY -> appContext.resources.getString(R.string.field_word_is_empty)
        IResourceProvider.STRINGS.LIST_OF_TRANSLATION_IS_EMPTY -> appContext.resources.getString(R.string.field_word_is_empty)
    }
}