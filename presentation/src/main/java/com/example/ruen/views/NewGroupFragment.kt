package com.example.ruen.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ruen.R
import com.example.ruen.databinding.FragmentNewGroupBinding
import com.example.ruen.viewmodels.NewGroupViewModel
import com.example.ruen.viewmodels.NewGroupViewModel.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewGroupFragment :
    BottomSheetDialogFragment() {

    private val viewModel by viewModel<NewGroupViewModel>()
    private var _binding: FragmentNewGroupBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.NewCardBottomSheetModal)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToData()
        setClickListeners()
    }

    private fun subscribeToData() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notificationState.collectLatest {
                    updateUI(it)
                }
            }
        }
    }

    private fun updateUI(uiState: UIState) {
        if (uiState.saveStatus)
            dismiss()
        uiState.notificationMessage?.let {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            viewModel.notificationShown()
        }
    }

    private fun setClickListeners() {
        with(binding) {
            saveView.setOnClickListener {
                viewModel.save(nameView.text.toString())
            }
        }
    }

}