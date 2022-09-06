package com.example.ruen.ui.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ruen.R
import com.example.ruen.databinding.FragmentTranslatorBinding
import com.example.ruen.ui.viewmodel.TranslatorUIState
import com.example.ruen.ui.viewmodel.TranslatorViewModel
import kotlinx.coroutines.launch

class TranslatorFragment :
    BaseFragment<FragmentTranslatorBinding>(FragmentTranslatorBinding::inflate) {

    private val viewModel: TranslatorViewModel by viewModels()
    private val imm by lazy { requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUpdatesUI()
        setClickListeners()
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
            else -> {}
        }
    }

    private fun setSuccessUiState(uiState: TranslatorUIState.Success) {
        with(binding) {
            wordView.apply {
                setText(uiState.word)
                setSelection(uiState.word.length)
            }
            translationContainter.apply {
                removeAllViews()
            }
            uiState.translations?.forEach {
                translationContainter.addView(createTranslateTextView(it))
            }
            hideKeyboard()
        }
    }

    private fun hideKeyboard() = imm.hideSoftInputFromWindow(view?.windowToken, 0)

    private fun createTranslateTextView(text: String) =
        TextView(
            requireContext(),
            null,
            0,
            R.style.Widget_AppCompat_TextView_TranslateTextView
        ).apply {
            setText(text)
        }

    private fun showWarning(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}