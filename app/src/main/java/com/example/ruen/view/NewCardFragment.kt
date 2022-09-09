package com.example.ruen.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.domain.models.TranslatedWord
import com.example.ruen.R
import com.example.ruen.databinding.FragmentNewCardBinding
import com.example.ruen.viewmodel.TranslatorUIState
import com.example.ruen.viewmodel.TranslatorViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewCardFragment :
    BaseFragment<FragmentNewCardBinding>(FragmentNewCardBinding::inflate) {

    val viewModel: TranslatorViewModel by viewModel()
    private val imm by lazy { requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUpdatesUI()
        setClickListeners()
        setChangeListeners()
    }

    private fun setClickListeners() {
        with(binding) {
            saveView.setOnClickListener {
                val word = wordView.text.toString()
                if (word.isEmpty())
                    showWarning("Введите слово")
                else
                    viewModel.translate(wordView.text.toString())
            }
        }
    }

    private fun setChangeListeners() = with(binding) {
        wordView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                text?.let {
                    viewModel.translate(text.toString())
                }
            }

        })
    }

    private fun subscribeUpdatesUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    updateUI(it)
                }
            }
        }
    }

    private fun updateUI(uiState: TranslatorUIState) {
        when (uiState) {
            is TranslatorUIState.Success -> {
                setSuccessUiState(uiState)
            }
            is TranslatorUIState.Loading -> showWarning("Загрузка")
            is TranslatorUIState.Error -> uiState.throwable.message?.let {
                showWarning(it)
                Log.d("TAGwww", "updateUI:  ${uiState.throwable.stackTraceToString()}")
            }
        }
    }

    private fun setSuccessUiState(uiState: TranslatorUIState.Success) {
        with(binding) {
            wordView.apply {
                setText(uiState.word)
                setSelection(uiState.word.length)
            }
            translationContainerView.removeAllViews()
            uiState.translations?.forEach {
                for (i in 1..10) {
                    translationContainerView.addView(createTranslateTextView(it))
                }
            }
            hideKeyboard()
        }
    }

    private fun hideKeyboard() = imm.hideSoftInputFromWindow(view?.windowToken, 0)

    private fun createTranslateTextView(translatedWord: TranslatedWord) =
        Chip(
            requireContext(),
            null,
        ).apply {
            val chipDrawable = ChipDrawable.createFromAttributes(
                requireContext(),
                null, 0,
                R.style.Widget_MaterialComponents_Chip_Choice_MyChip
            )
            setChipDrawable(chipDrawable)
            text = translatedWord.value
        }

    private fun showWarning(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}