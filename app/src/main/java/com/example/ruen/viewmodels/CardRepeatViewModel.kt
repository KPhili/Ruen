package com.example.ruen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Card
import com.example.domain.models.TranslatedWord
import com.example.domain.repositories.ICardRepository
import com.example.domain.usecases.FormatRepeatIntervalUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class CardRepeatViewModel(
    private val cardRepository: ICardRepository,
    private val formatRepeatIntervalUseCase: FormatRepeatIntervalUseCase
) : ViewModel() {

    private var currentCard: Card? = null

    private val _uiState: MutableStateFlow<UIState> =
        MutableStateFlow(UIState.Card(Card(value = "")))
    val uiState = _uiState
        .onEach { currentCard = if (it is UIState.Card) it.card else null }

    init {
        loadNextCard()
    }

    fun chooseLevelKnow(knowLevel: KnowLevel) {
        viewModelScope.launch(Dispatchers.Default) {
            currentCard?.let { card ->
                val nextRepeatNumber = getNextRepeatNumber(card.repeatNumber, knowLevel)
                val interval = getIntervalRepeat(nextRepeatNumber)
                val nextRepeatDate = LocalDateTime.now().plusMinutes(interval)
                val newCard =
                    card.copy(repeatNumber = nextRepeatNumber, nextRepetition = nextRepeatDate)
                cardRepository.update(newCard)
                currentCard = null
                loadNextCard()
            }
        }
    }

    fun loadNextCard() {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.value = cardRepository.getNextCardForRepeat()?.run {
                val card = first
                val translations = second
                val listIntervals = KnowLevel.values().map {
                    val nextRepeatNumber = getNextRepeatNumber(card.repeatNumber, it)
                    val interval = getIntervalRepeat(nextRepeatNumber)
                    val intervalString = formatRepeatIntervalUseCase(interval)
                    Pair(it, intervalString)
                }
                UIState.Card(card, translations, listIntervals)
            } ?: UIState.Empty
        }
    }

    private fun getNextRepeatNumber(currentNumber: Int, knowLevel: KnowLevel) = when {
        knowLevel == KnowLevel.DONT_KNOW ||
                (currentNumber in arrayOf(0, 1) &&
                        knowLevel == KnowLevel.BAD_KNOW) -> 0
        knowLevel == KnowLevel.GOOD_KNOW -> currentNumber
        else -> currentNumber + 1
    }

    private fun getIntervalRepeat(repeatNumber: Int) = when (repeatNumber) {
        0 -> FIRST_REPEAT
        1 -> SECOND_REPEAT
        else -> convertToMinutes(2 * (repeatNumber.toLong() - 2) + 1, TimeUnit.DAYS)
    }


    companion object {
        // interval in minutes
        private const val FIRST_REPEAT = 3L
        private val SECOND_REPEAT = convertToMinutes(4, TimeUnit.HOURS)

        private fun convertToMinutes(value: Long, type: TimeUnit) = when (type) {
            TimeUnit.MINUTES -> value
            TimeUnit.HOURS -> value * 60
            TimeUnit.DAYS -> value * 24
            else -> throw IllegalArgumentException()
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

    enum class KnowLevel {
        DONT_KNOW, BAD_KNOW, GOOD_KNOW, EXCELLENT_KNOW
    }
}