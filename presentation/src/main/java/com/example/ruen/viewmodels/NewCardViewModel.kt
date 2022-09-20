package com.example.ruen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repositories.TranslatedWordRepository
import com.example.domain.models.Card
import com.example.domain.models.TranslatedWord
import com.example.domain.providers.IResourceProvider
import com.example.domain.usecases.SaveCardWithTranslatedWordUseCase
import com.example.ruen.utils.InternetConnectionChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.ConnectException


class TranslatorViewModel(
    private val repository: TranslatedWordRepository,
    private val saveCardWithTranslatedWordUseCase: SaveCardWithTranslatedWordUseCase,
    private val resourceProvider: IResourceProvider,
    private val internetConnectionChecker: InternetConnectionChecker
) : ViewModel() {

    private val _uiState: MutableStateFlow<TranslatorUIState> = MutableStateFlow(
        TranslatorUIState.TranslationsLoaded()
    )
    val viewState: StateFlow<TranslatorUIState> get() = _uiState

    private val wordFlow = MutableStateFlow("")

    init {
        wordFlow
            .debounce(DEBOUNCE_MILLIS)
            .onEach { word ->
                if (word.isEmpty()) {
                    _uiState.value = TranslatorUIState.ClearUIState
                    return@onEach
                }
                _uiState.value = TranslatorUIState.Loading
                try {
                    if (internetConnectionChecker()) {
                        val translations = repository.translate(word)
                        _uiState.value = TranslatorUIState.TranslationsLoaded(translations)
                    } else {
                        _uiState.value =
                            TranslatorUIState.Error(
                                ConnectException(
                                    resourceProvider.getString(
                                        IResourceProvider.STRINGS.NO_INTERNET_CONNECTION
                                    )
                                )
                            )
                    }

                } catch (e: Throwable) {
                    TranslatorUIState.Error(e)
                }
            }

            .launchIn(viewModelScope)
    }

    fun translate(word: String) {
        viewModelScope.launch {
            wordFlow.emit(word)
        }
    }

    fun newCard(word: String, translations: Array<String>) = viewModelScope.launch {
        if (word.isEmpty()) {
            _uiState.value = TranslatorUIState.Notification(
                resourceProvider.getString(
                    IResourceProvider.STRINGS.FIELD_WORD_IS_EMPTY
                )
            )
            return@launch
        }
        val translatedWordList = translations
            .filter { it.isNotEmpty() }
            .map { TranslatedWord(value = it) }
        if (translatedWordList.isEmpty()) {
            _uiState.value = TranslatorUIState.Notification(
                resourceProvider.getString(
                    IResourceProvider.STRINGS.LIST_OF_TRANSLATION_IS_EMPTY
                )
            )
            return@launch
        }
        val card = Card(value = word)
        saveCardWithTranslatedWordUseCase(card, translatedWordList)
        _uiState.value = TranslatorUIState.ClearUIState
    }

    companion object {
        const val DEBOUNCE_MILLIS = 1000L
    }
}

sealed class TranslatorUIState {
    data class TranslationsLoaded(
        val translations: List<TranslatedWord>? = null
    ) : TranslatorUIState()
    object ClearUIState : TranslatorUIState()
    data class Notification(val message: String) : TranslatorUIState()
    data class Error(val throwable: Throwable) : TranslatorUIState()
    object Loading : TranslatorUIState()
}

