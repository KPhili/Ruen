package com.example.ruen.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repositories.CardRepository
import com.example.data.repositories.TranslatedWordRepository
import com.example.domain.models.Card
import com.example.domain.models.TranslatedWord
import com.example.domain.providers.IResourceProvider
import com.example.domain.usecases.SaveCardWithTranslatedWordUseCase
import com.example.domain.usecases.UpdateCardWithTranslatedWordUseCase
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
    private val updateCardWithTranslatedWordUseCase: UpdateCardWithTranslatedWordUseCase,
    private val resourceProvider: IResourceProvider,
    private val internetConnectionChecker: InternetConnectionChecker,
    private val groupId: Long,
    private val cardId: Long?
) : ViewModel() {

    private var card: Card? = null
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
                    return@onEach
                }
                try {
                    if (internetConnectionChecker()) {
                        val translations = translatedWordRepository.translate(word)
                        _uiState.update {
                            it.copy(
                                translatedWords = translations.toMutableList()
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

    fun clickTranslatedWord(translatedWord: String, isChecked: Boolean) {
        _uiState.update { cardUIState ->
            val translatedWords = cardUIState.translatedWords ?: mutableListOf()
            val selectedTranslatedWords = cardUIState.selectedTranslatedWords
            if (isChecked) {
                movingTranslatedWord(translatedWords, selectedTranslatedWords, translatedWord)
            } else {
                movingTranslatedWord(selectedTranslatedWords, translatedWords, translatedWord)
            }
            cardUIState.copy(
                selectedTranslatedWords = selectedTranslatedWords,
                translatedWords = translatedWords
            )
        }
    }

    private fun movingTranslatedWord(
        fromList: MutableList<TranslatedWord>,
        toList: MutableList<TranslatedWord>,
        translatedWord: String
    ) {
        val indexTranslatedWord = fromList.indexOfFirst { it.value == translatedWord }
        toList.add(fromList[indexTranslatedWord])
        fromList.removeAt(indexTranslatedWord)
    }

    fun saveCard(word: String, translations: Array<String>) = viewModelScope.launch {
        if (word.isEmpty()) {
            setNotificationWordIsEmpty()
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

        val card = card?.copy(value = word) ?: Card(value = word, groupId = groupId)
        if (card.id != null) {
            updateCardWithTranslatedWordUseCase(card, translatedWordList)
        } else {
            saveCardWithTranslatedWordUseCase(card, translatedWordList)
        }
        _uiState.update { it.copy(isSaved = true) }
    }

    private fun setNotificationWordIsEmpty() {
        _uiState.update {
            val emptyFieldsMessage = resourceProvider.getString(
                IResourceProvider.STRINGS.FIELD_WORD_IS_EMPTY
            )
            it.copy(notificationMessage = emptyFieldsMessage)
        }
    }

    fun errorAccepted() {
        _uiState.update { it.copy(error = null) }
    }

    fun notificationAccepted() {
        _uiState.update { it.copy(error = null) }
    }

    fun cardAccepted() {
        _uiState.update { it.copy(card = null) }
    }

    fun selectImage(word:String) {
        if (word.isEmpty()){
            setNotificationWordIsEmpty()
            return
        }
        _uiState.update { it.copy(selectImage = word) }
    }

    fun imageSelected() {
        _uiState.update { it.copy(selectImage = null) }
    }

    private fun getCard() {
        cardId?.let {
            viewModelScope.launch {
                val pair = cardRepository.getCardWithTranslatedWord(cardId)
                card = pair.first
                _uiState.value =
                    CardUIState(
                        card = pair.first,
                        selectedTranslatedWords = pair.second.toMutableList()
                    )
            }
        }
    }

    companion object {
        const val DEBOUNCE_MILLIS = 1000L
    }
}

data class CardUIState(
    val card: Card? = null,
    val selectedTranslatedWords: MutableList<TranslatedWord> = mutableListOf(),
    val translatedWords: MutableList<TranslatedWord>? = null,
    val isSaved: Boolean = false,
    val selectImage: String? = null,
    val notificationMessage: String? = null,
    val error: Throwable? = null
)

