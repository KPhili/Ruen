package com.example.ruen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repositories.CardRepository
import com.example.data.repositories.TranslatedWordRepository
import com.example.domain.models.Card
import com.example.domain.models.TranslatedWord
import com.example.domain.providers.IResourceProvider
import com.example.domain.usecases.SaveCardWithTranslatedWordUseCase
import com.example.ruen.utils.InternetConnectionChecker
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.ConnectException


@OptIn(FlowPreview::class)
class CardViewModel(
    private val translatedWordRepository: TranslatedWordRepository,
    private val cardRepository: CardRepository,
    private val saveCardWithTranslatedWordUseCase: SaveCardWithTranslatedWordUseCase,
    private val resourceProvider: IResourceProvider,
    private val internetConnectionChecker: InternetConnectionChecker,
    private val groupId: Long,
    private val cardId: Long?
) : ViewModel() {

    private val _uiState: MutableStateFlow<CardUIState> = MutableStateFlow(
        CardUIState()
    )
    val viewState: StateFlow<CardUIState> get() = _uiState
    private val wordFlow = MutableStateFlow("")

    init {
        getCard()
        wordFlow
            .debounce(DEBOUNCE_MILLIS)
            .onEach { word ->
                if (word.isEmpty()) {
                    _uiState.value = CardUIState()
                    return@onEach
                }
                try {
                    if (internetConnectionChecker()) {
                        val translations = translatedWordRepository.translate(word)
                        _uiState.update {
                            val card =
                                it.card?.copy(value = word) ?: Card(value = word, groupId = groupId)
                            it.copy(
                                card = card, translatedWords = translations
                            )
                        }
                    } else {
                        val errorMessage = resourceProvider.getString(
                            IResourceProvider.STRINGS.NO_INTERNET_CONNECTION
                        )
                        _uiState.update { it.copy(error = ConnectException(errorMessage)) }
                    }
                } catch (e: Throwable) {
                    _uiState.update { it.copy(error = e) }
                }
            }
            .launchIn(viewModelScope)


    }

    fun translate(word: String) {
        viewModelScope.launch {
            wordFlow.emit(word)
        }
    }

    fun saveCard(word: String, translations: Array<String>) = viewModelScope.launch {
        if (word.isEmpty()) {
            _uiState.update {
                val emptyFieldsMessage = resourceProvider.getString(
                    IResourceProvider.STRINGS.FIELD_WORD_IS_EMPTY
                )
                it.copy(notificationMessage = emptyFieldsMessage)
            }
            return@launch
        }
        val translatedWordList = translations
            .filter { it.isNotEmpty() }
            .map { TranslatedWord(value = it) }
        if (translatedWordList.isEmpty()) {
            _uiState.update {
                val translationsEmptyMessage = resourceProvider.getString(
                    IResourceProvider.STRINGS.LIST_OF_TRANSLATION_IS_EMPTY
                )
                it.copy(notificationMessage = translationsEmptyMessage)
            }
            return@launch
        }
        val card = Card(value = word, groupId = groupId)
        saveCardWithTranslatedWordUseCase(card, translatedWordList)
        _uiState.update { it.copy(isSaved = true) }
    }

    fun errorAccepted() {
        _uiState.update { it.copy(error = null) }
    }

    fun notificationAccepted() {
        _uiState.update { it.copy(error = null) }
    }

    private fun getCard() {
        cardId?.let {
            viewModelScope.launch {
                val pair = cardRepository.getCardWithTranslatedWord(cardId)
                _uiState.value = CardUIState(pair.first, pair.second)
            }
        }
    }

    companion object {
        const val DEBOUNCE_MILLIS = 1000L
    }
}

data class CardUIState(
    val card: Card? = null,
    val translatedWords: List<TranslatedWord>? = null,
    val isSaved: Boolean = false,
    val notificationMessage: String? = null,
    val error: Throwable? = null
)

