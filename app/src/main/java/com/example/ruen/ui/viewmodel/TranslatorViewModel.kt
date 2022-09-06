package com.example.ruen.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TranslatorViewModel : ViewModel() {
    private val _viewState: MutableStateFlow<TranslatorUIState> by lazy {
        MutableStateFlow(
            TranslatorUIState.Success()
        )
    }
    val viewState: StateFlow<TranslatorUIState> get() = _viewState.asStateFlow()

    fun translate(word: String) {
        viewModelScope.launch {
            _viewState.update { TranslatorUIState.Loading }
            delay(3000)
            _viewState.update {
                TranslatorUIState.Success(word, arrayOf("Слово", "Дать слово"))
            }
        }
    }
}

sealed class TranslatorUIState {
    data class Success(val word: String = "", val translations: Array<String>? = null) :
        TranslatorUIState()

    data class Error(val throwable: Throwable) : TranslatorUIState()
    object Loading : TranslatorUIState()
}

