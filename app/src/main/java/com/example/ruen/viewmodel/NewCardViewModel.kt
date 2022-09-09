package com.example.ruen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repositories.WordTranslationRepository
import com.example.domain.models.TranslatedWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.Locale.filter


class TranslatorViewModel(
    private val repository: WordTranslationRepository
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
                        val translations =
                            withContext(Dispatchers.IO) {
                                repository.translate(word)
                            }
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

