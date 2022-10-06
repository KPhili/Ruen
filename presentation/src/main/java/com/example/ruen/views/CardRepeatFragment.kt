package com.example.ruen.views

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.domain.models.KnowLevel
import com.example.ruen.R
import com.example.ruen.databinding.FragmentCardRepeatBinding
import com.example.ruen.viewmodels.CardRepeatViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CardRepeatFragment :
    BaseFragment<FragmentCardRepeatBinding>(FragmentCardRepeatBinding::inflate),
    View.OnClickListener {

    private val viewModel: CardRepeatViewModel by viewModel { parametersOf(groupId) }
    private val args: CardsFragmentArgs by navArgs()
    private val groupId: Long by lazy { args.groupId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToUIState()
        setViewsProperty()
        setClickListeners()
    }

    private fun subscribeToUIState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState ->
                    updateUIState(uiState)
                }
            }
        }
    }

    private fun setViewsProperty() = with(binding) {
        wordView.movementMethod = ScrollingMovementMethod()
        translationsView.movementMethod = ScrollingMovementMethod()
    }


    private fun updateUIState(uiState: CardRepeatViewModel.UIState) = with(binding) {
        when (uiState) {
            is CardRepeatViewModel.UIState.Card -> setCardUIState(uiState)
            is CardRepeatViewModel.UIState.Empty -> setNoMoreWords()
        }

    }

    private fun setCardUIState(uiState: CardRepeatViewModel.UIState.Card) = with(binding) {
        wordView.text = uiState.card.value
        translationsView.text = uiState.translations?.map { it.value }?.joinToString(", ")
        uiState.repeatIntervals?.forEach {
            when (it.first) {
                KnowLevel.DONT_KNOW -> dontKnowView.text =
                    resources.getString(
                        R.string.dont_know,
                        it.second
                    )
                KnowLevel.BAD_KNOW -> badKnowView.text =
                    resources.getString(
                        R.string.bad_know,
                        it.second
                    )
                KnowLevel.GOOD_KNOW -> goodKnowView.text =
                    resources.getString(
                        R.string.good_know,
                        it.second
                    )
            }
        }
        noCardView.visibility = View.GONE
    }

    private fun setNoMoreWords() = with(binding) {
        wordView.text = ""
        translationsView.text = ""
        noCardView.visibility = View.VISIBLE
    }

    private fun setClickListeners() = with(binding) {
        arrayOf(dontKnowView, badKnowView, goodKnowView).forEach {
            it.setOnClickListener(this@CardRepeatFragment)
        }
        // видимость поля основной и обратной сторон
        arrayOf(wordView, translationsView).forEach {
            it.setOnClickListener {
                val wordViewVisibility = getReverseVisibility(wordView.visibility)
                val translationsViewVisibility = wordView.visibility
                wordViewVisibility.let {
                    wordView.visibility = it
                    mainSide.visibility = it
                }
                translationsViewVisibility.let {
                    translationsView.visibility = it
                    otherSide.visibility = it
                }
            }
        }
    }

    private fun getReverseVisibility(visibility: Int) = when (visibility) {
        View.GONE, View.INVISIBLE -> View.VISIBLE
        View.VISIBLE -> View.GONE
        else -> throw IllegalArgumentException("Visibility must be View.[GONE,VISIBLE, INVISIBLE]")
    }

    override fun onClick(view: View) {
        val answer = when (view.id) {
            R.id.dont_know_view -> KnowLevel.DONT_KNOW
            R.id.bad_know_view -> KnowLevel.BAD_KNOW
            R.id.good_know_view -> KnowLevel.GOOD_KNOW
            else -> throw IllegalArgumentException("Button not supported")
        }
        viewModel.chooseLevelKnow(answer)
    }
}