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
    private val getIntervalRepeatUseCase: GetIntervalRepeatUseCase
) : ViewModel() {

    private var currentCard: Card? = null

    val uiState: StateFlow<UIState>

    init {
        uiState = cardRepository.getNextCardForRepeat()
            .map {
                it?.let {
                    val card = it.first
                    val translations = it.second
                    val listIntervals = KnowLevel.values().map { knowLevel ->
                        val repeatNumber =
                            getNextRepeatNumberUseCase(card.repeatNumber, knowLevel)
                        val interval = getIntervalRepeatUseCase(repeatNumber)
                        val intervalString = formatRepeatIntervalUseCase(interval)
                        Pair(knowLevel, intervalString)
                    }
                    UIState.Card(card, translations, listIntervals)
                } ?: UIState.Empty
            }
            .onEach { currentCard = if (it is UIState.Card) it.card else null }
            .flowOn(Dispatchers.Default)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                UIState.Card(Card(value = "", groupId = 0))
            )

    }

    fun chooseLevelKnow(knowLevel: KnowLevel) {
        viewModelScope.launch(Dispatchers.Default) {
            currentCard?.let { card ->
                val nextRepeatNumber = getNextRepeatNumberUseCase(card.repeatNumber, knowLevel)
                val interval = getIntervalRepeatUseCase(nextRepeatNumber)
                val nextRepeatDate = LocalDateTime.now().plusMinutes(interval)
                val newCard =
                    card.copy(repeatNumber = nextRepeatNumber, nextRepetition = nextRepeatDate)
                cardRepository.update(newCard)
                currentCard = null
            }
        }
    }

    sealed class UIState {
        data class Card(
            val card: com.example.domain.models.Card,
            val translations: List<TranslatedWord>? = null,
            val repeatIntervals: List<Pair<KnowLevel, String>>? = null
        ) : UIState()

        object Empty : UIState()
    }
}