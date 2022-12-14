package com.example.ruen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Card
import com.example.domain.models.KnowLevel
import com.example.domain.models.TranslatedWord
import com.example.domain.repositories.ICardRepository
import com.example.domain.usecases.FormatRepeatIntervalUseCase
import com.example.domain.usecases.GetIntervalRepeatUseCase
import com.example.domain.usecases.GetNextRepeatNumberUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class CardRepeatViewModel(
    private val cardRepository: ICardRepository,
    private val formatRepeatIntervalUseCase: FormatRepeatIntervalUseCase,
    private val getNextRepeatNumberUseCase: GetNextRepeatNumberUseCase,
    private val getIntervalRepeatUseCase: GetIntervalRepeatUseCase,
    private val groupId: Long?
) : ViewModel() {

    private var isSpecificCard: Boolean = false
    private var currentCard: Card? = null
    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)

    fun getUIState(cardId: Long? = null): StateFlow<UIState> {
        if (cardId == null) {
            startFlow()
        } else {
            isSpecificCard = true
            getCard(cardId)
        }
        return _uiState.asStateFlow()
    }

    fun chooseLevelKnow(knowLevel: KnowLevel) {
        viewModelScope.launch(Dispatchers.Default) {
            currentCard?.let { card ->
                val nextRepeatNumber = getNextRepeatNumberUseCase(card.repeatNumber, knowLevel)
                val interval = getIntervalRepeatUseCase(nextRepeatNumber)
                val nextRepeatDate = LocalDateTime.now().plusMinutes(interval)
                val newCard =
                    card.copy(repeatNumber = nextRepeatNumber, nextRepetition = nextRepeatDate)
                currentCard = null
                cardRepository.update(newCard)
            }
            if (isSpecificCard) {
                startFlow()
                isSpecificCard = false
            }
        }
    }

    private fun getCard(cardId: Long) {
        viewModelScope.launch {
            val pair = cardRepository.getCardWithTranslatedWord(cardId)
            _uiState.value = convertPairToUIState(pair)
            currentCard = pair.first
        }
    }

    private fun convertPairToUIState(cardWithTranslatedWord: Pair<Card, List<TranslatedWord>>): UIState.Card {
        val card = cardWithTranslatedWord.first
        val translations = cardWithTranslatedWord.second
        val listIntervals = KnowLevel.values().map { knowLevel ->
            val repeatNumber =
                getNextRepeatNumberUseCase(card.repeatNumber, knowLevel)
            val interval = getIntervalRepeatUseCase(repeatNumber)
            val intervalString = formatRepeatIntervalUseCase(interval)
            Pair(knowLevel, intervalString)
        }
        return UIState.Card(card, translations, listIntervals)
    }

    private fun startFlow() {
        val flow =
            if (groupId == null) cardRepository.getNextCardForRepeat()
            else cardRepository.getNextCardForRepeatInGroup(groupId)
        flow
            .onEach {
                _uiState.value = it?.let {
                    currentCard = it.first
                    convertPairToUIState(it)
                } ?: UIState.Empty
            }
            .launchIn(viewModelScope)
    }

    sealed class UIState {
        data class Card(
            val card: com.example.domain.models.Card,
            val translations: List<TranslatedWord>? = null,
            val repeatIntervals: List<Pair<KnowLevel, String>>? = null
        ) : UIState()

        object Loading : UIState()
        object Empty : UIState()
    }
}