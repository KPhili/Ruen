package com.example.ruen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repositories.TranslatedWordRepository
import com.example.domain.models.Card
import com.example.domain.models.TranslatedWord
import com.example.domain.usecases.SaveCardWithTranslatedWordUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TranslatorViewModel(
    private val repository: TranslatedWordRepository,
    private val saveCardWithTranslatedWordUseCase: SaveCardWithTranslatedWordUseCase
) : ViewModel() {

    private val _viewState: MutableStateFlow<TranslatorUIState> = MutableStateFlow(
        TranslatorUIState.Success()
    )
    val viewState: StateFlow<TranslatorUIState> get() = _viewState.asStateFlow()

    private val wordFlow = MutableStateFlow("")

    init {
        viewModelScope.launch(Dispatchers.Main) {
            wordFlow.debounce(DEBOUNCE_MILLIS)
                .distinctUntilChanged()
                .filter { it.isNotEmpty() }
                .catch {
                    TranslatorUIState.Error(it)
                }
                .collect { word ->
                    _viewState.update { TranslatorUIState.Loading }
                    _viewState.update {
                        val translations = repository.translate(word)
                        TranslatorUIState.Success(word, translations)
                    }
                }
        }
    }

    fun translate(word: String) {
        if (word.isEmpty()) return
        viewModelScope.launch {
            wordFlow.emit(word)
        }
    }

    fun newCard(word: String, translations: Array<String>) = viewModelScope.launch {
        val card = Card(value = word)
        val translatedWordList = translations.map { TranslatedWord(value = it) }
        saveCardWithTranslatedWordUseCase(card, translatedWordList)
        _viewState.update { TranslatorUIState.Success() }
    }

    companion object {
        const val DEBOUNCE_MILLIS = 1000L
    }
}

sealed class TranslatorUIState {
    data class Success(val word: String = "", val translations: List<TranslatedWord>? = null) :
        TranslatorUIState()

    data class Error(val throwable: Throwable) : TranslatorUIState()
    object Loading : TranslatorUIState()
}

