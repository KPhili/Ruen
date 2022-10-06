package com.example.ruen.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.domain.models.TranslatedWord
import com.example.ruen.R
import com.example.ruen.databinding.FragmentNewCardBinding
import com.example.ruen.viewmodels.TranslatorUIState
import com.example.ruen.viewmodels.NewCardViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NewCardFragment :
    BottomSheetDialogFragment() {

    private val viewModel: NewCardViewModel by viewModel { parametersOf(groupId) }
    private var _binding: FragmentNewCardBinding? = null
    private val binding get() = _binding!!
    private val args: NewCardFragmentArgs by navArgs()
    private val groupId by lazy { args.groupId }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.NewCardBottomSheetModal)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUpdatesUI()
        setClickListeners(view)
        setChangeListeners()
    }

    private fun setClickListeners(view: View) {
        with(binding) {
            saveView.setOnClickListener {
                val word = wordView.text.toString()
                val translations = translationView.text.split("\n").toMutableList()
                val chipIds = translationContainerView.checkedChipIds
                if (chipIds.isNotEmpty()) {
                    chipIds.forEach {
                        val translate = view.findViewById<Chip>(it).text.toString()
                        translations.add(translate)
                    }
                }
                viewModel.saveCard(word, translations.toTypedArray())
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
            is TranslatorUIState.TranslationsLoaded -> setSuccessUiState(uiState)
            is TranslatorUIState.ClearUIState -> clearViews()
            is TranslatorUIState.SavedSuccess -> dismiss()
            is TranslatorUIState.Error -> uiState.throwable.message?.let {
                showNotification(it)
                Log.d(TAG, "updateUI:  ${uiState.throwable.stackTraceToString()}")
            }
            is TranslatorUIState.Notification -> showNotification(uiState.message)
        }
    }

    private fun setSuccessUiState(uiState: TranslatorUIState.TranslationsLoaded) {
        with(binding) {
            translationContainerView.removeAllViews()
            uiState.translations?.forEach {
                translationContainerView.addView(createTranslateTextView(it))
            }
        }
    }

    private fun clearViews() = with(binding) {
        translationContainerView.removeAllViews()
        translationView.text.clear()
        wordView.text.clear()
    }


    private fun createTranslateTextView(translatedWord: TranslatedWord) =
        Chip(
            requireContext(),
            null,
        ).apply {
            val chipDrawable = ChipDrawable.createFromAttributes(
                requireContext(),
                null, 0,
                R.style.MyChip
            )
            setChipDrawable(chipDrawable)
            text = translatedWord.value
        }

    private fun showNotification(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val TAG = "NewCardFragment"
    }
}