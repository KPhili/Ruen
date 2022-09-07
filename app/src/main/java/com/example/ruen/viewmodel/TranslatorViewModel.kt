package com.example.ruen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repositories.WordTranslationRepository
import com.example.domain.models.TranslatedWord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException


class TranslatorViewModel(
    private val repository: WordTranslationRepository
) : ViewModel() {

    private val _viewState: MutableStateFlow<TranslatorUIState> by lazy {
        MutableStateFlow(
            TranslatorUIState.Success()
        )
    }
    val viewState: StateFlow<TranslatorUIState> get() = _viewState.asStateFlow()

    fun translate(word: String) {
        viewModelScope.launch {
            _viewState.update { TranslatorUIState.Loading }
            _viewState.update {
                try {
                    val translations = repository.translate(word)
                    TranslatorUIState.Success(word, translations)
                } catch (e: HttpException) {
                    TranslatorUIState.Error(e)
                }
            }
        }
    }
}

sealed class TranslatorUIState {
    data class Success(val word: String = "", val translations: List<TranslatedWord>? = null) :
        TranslatorUIState()

    data class Error(val throwable: Throwable) : TranslatorUIState()
    object Loading : TranslatorUIState()
}

