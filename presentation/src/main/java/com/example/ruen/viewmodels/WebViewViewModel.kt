package com.example.ruen.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WebViewViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<String?>(null)
    val uiState
        get() = _uiState.asStateFlow()

    fun findImages(word: String) {
        if (word.isNotEmpty()) {
            _uiState.value = IMAGE_SEARCH_URL + Uri.encode(word)
        }
    }

    companion object {
        private const val IMAGE_SEARCH_URL = "https://yandex.ru/images/search?text="
    }
}