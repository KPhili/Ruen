package com.example.ruen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repositories.TranslatedWordRepository
import com.example.domain.models.Card
import com.example.domain.models.TranslatedWord
import com.example.domain.usecases.SaveCardWithTranslatedWordUseCase
import com.example.ruen.providers.IResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class TranslatorViewModel(
    private val repository: TranslatedWordRepository,
    private val saveCardWithTranslatedWordUseCase: SaveCardWithTranslatedWordUseCase,
    private val resourceProvider: IResourceProvider
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
        _viewState.update {
            if (word.isEmpty()) {
                TranslatorUIState.Notification(
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
                TranslatorUIState.Notification(
                    resourceProvider.getString(
                        IResourceProvider.STRINGS.LIST_OF_TRANSLATION_IS_EMPTY
                    )
                )
                return@launch
            }
            val card = Card(value = word)
            saveCardWithTranslatedWordUseCase(card, translatedWordList)

            TranslatorUIState.Success(
                notificationMessage = resourceProvider.getString(
                    IResourceProvider.STRINGS.SAVE_CARD_SUCCESS
                )
            )
        }
    }

    companion object {
        const val DEBOUNCE_MILLIS = 1000L
    }
}

sealed class TranslatorUIState {
    data class Success(
        val word: String = "",
        val translations: List<TranslatedWord>? = null,
        val notificationMessage: String? = null
    ) : TranslatorUIState()

    data class Notification(val message: String) : TranslatorUIState()
    data class Error(val throwable: Throwable) : TranslatorUIState()
    object Loading : TranslatorUIState()
}

