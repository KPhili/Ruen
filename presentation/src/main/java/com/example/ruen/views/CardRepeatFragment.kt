package com.example.ruen.views

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.View
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
    private val args: CardRepeatFragmentArgs by navArgs()
    private val groupId: Long? by lazy { args.groupId.takeIf { it != -1L } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToUIState()
        setViewsProperty()
        setClickListeners()
    }

    private fun subscribeToUIState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val cardId = args.cardId.takeIf { it != -1L }
                viewModel.getUIState(cardId).collectLatest { uiState ->
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
            is CardRepeatViewModel.UIState.Empty -> setNoMoreWords(true)
            CardRepeatViewModel.UIState.Loading -> setLoading(true)
        }
    }

    private fun setLoading(visible: Boolean) = with(binding) {
        val visibility = if (visible) View.VISIBLE else View.GONE
        progressBar.visibility = visibility
    }

    private fun setCardUIState(uiState: CardRepeatViewModel.UIState.Card) = with(binding) {
        setLoading(false)
        wordView.text = uiState.card.value
        translationsView.text = uiState.translations?.joinToString(", ") { it.value }
        val imageFileName = uiState.card.imageFileName
        // ???????????????? ???????????? "???????????????? ??????????????????????" ?? ?????????????????? ???????? ??????????????????????, ???????? ???????? imageFileName
        if (imageFileName != null) {
            showImage.visibility = View.VISIBLE
            val image = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                ?.listFiles { _, fileName -> fileName == imageFileName }
                ?.takeIf { it.isNotEmpty() }
                ?.first()
            image?.let {
                val bitmapImage = BitmapFactory.decodeFile(image.absolutePath)
                imageView.setImageBitmap(bitmapImage)
            }
        } else {
            showImage.visibility = View.GONE
        }
        if (wordView.visibility == View.GONE) flipCard()
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
        setNoMoreWords(false)
    }

    // ?????????????????? ?????????????? ?????? ???????????? ???????? ?????? ????????????????????
    private fun setNoMoreWords(visible: Boolean) = with(binding) {
        setLoading(false)
        noCardView.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setClickListeners() = with(binding) {
        arrayOf(dontKnowView, badKnowView, goodKnowView).forEach {
            it.setOnClickListener(this@CardRepeatFragment)
        }
        // ?????????????????? ???????? ???????????????? ?? ???????????????? ????????????
        arrayOf(wordView, translationsView).forEach {
            it.setOnClickListener {
                flipCard()
            }
        }
        //???????????????? ??????????????????????
        showImage.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    imageView.visibility = View.VISIBLE
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    imageView.visibility = View.GONE
                    return@setOnTouchListener true
                }
            }

            return@setOnTouchListener view.performClick()
        }
    }

    // ?????????????????? ??????????
    private fun flipCard() = with(binding) {
        val wordViewVisibility = getReverseVisibility(wordView.visibility)
        val translationsViewVisibility = wordView.visibility
        wordViewVisibility.let {
            wordView.visibility = it
            mainSideView.visibility = it
        }
        translationsViewVisibility.let {
            translationsView.visibility = it
            otherSideView.visibility = it
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

    companion object {
        private const val TAG = "CardRepeatFragment"
    }
}