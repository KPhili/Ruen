package com.example.ruen.views

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.domain.models.TranslatedWord
import com.example.ruen.R
import com.example.ruen.databinding.FragmentCardBinding
import com.example.ruen.viewmodels.CardUIState
import com.example.ruen.viewmodels.CardViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CardFragment :
    BottomSheetDialogFragment() {

    private val viewModel: CardViewModel by viewModel { parametersOf(groupId, cardId) }
    private var _binding: FragmentCardBinding? = null
    private val binding get() = _binding!!
    private val args: CardFragmentArgs by navArgs()
    private val groupId by lazy { args.groupId }
    private val cardId by lazy { args.cardId.takeIf { it != -1L } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.behavior.skipCollapsed = true
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        return dialog
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

    private fun updateUI(uiState: CardUIState) {
        when {
            uiState.isSaved -> dismiss()
            uiState.error != null -> {
                uiState.error.message?.let {
                    showNotification(it)
                    Log.e(TAG, "updateUI:  ${uiState.error.stackTraceToString()}")
                    viewModel.errorAccepted()
                }
            }
            uiState.notificationMessage != null -> {
                showNotification(uiState.notificationMessage)
                viewModel.notificationAccepted()
            }
            uiState.card != null || uiState.translatedWords != null -> {
                uiState.card?.let {
                    binding.wordView.apply {
                        setText(it.value)
                        setSelection(it.value.length)
                    }
                    viewModel.cardAccepted()
                }
                setChipsTranslatedWords(uiState.selectedTranslatedWords, uiState.translatedWords)
            }
        }
    }

    private fun setChipsTranslatedWords(
        selectedTranslatedWord: List<TranslatedWord>,
        translatedWords: MutableList<TranslatedWord>?
    ) {
        with(binding) {
            translationContainerView.removeAllViews()
            selectedTranslatedWord.forEach {
                translationContainerView.addView(createTranslatedWordChip(it, true))
            }
            translatedWords?.forEach {
                translationContainerView.addView(createTranslatedWordChip(it))
            }
        }
    }

    private fun createTranslatedWordChip(
        translatedWord: TranslatedWord,
        selected: Boolean = false
    ) =
        Chip(requireContext(), null).apply {
            val chipDrawable = ChipDrawable.createFromAttributes(
                requireContext(),
                null, 0,
                R.style.MyChip
            )
            setChipDrawable(chipDrawable)
            text = translatedWord.value
            isChecked = selected
            setOnCheckedChangeListener { compoundButton, checked ->
                viewModel.clickTranslatedWord(compoundButton.text.toString(), checked)
            }
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