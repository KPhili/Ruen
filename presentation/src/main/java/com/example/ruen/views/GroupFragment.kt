package com.example.ruen.views

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.ruen.R
import com.example.ruen.databinding.FragmentGroupBinding
import com.example.ruen.viewmodels.GroupViewModel
import com.example.ruen.viewmodels.GroupViewModel.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class GroupFragment :
    BottomSheetDialogFragment() {

    private val viewModel: GroupViewModel by viewModel { parametersOf(groupId) }
    private var _binding: FragmentGroupBinding? = null
    private val binding get() = _binding!!
    private val args: GroupFragmentArgs by navArgs()
    private val groupId by lazy { args.groupId.takeIf { it > 0 } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.apply{
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
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
            viewModel.getGroupName()
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notificationState.collectLatest {
                    updateUI(it)
                }
            }
        }
    }

    private fun updateUI(uiState: UIState) {
        when {
            uiState.saveStatus -> {
                dismiss()
                return
            }
            uiState.notificationMessage != null -> {
                Toast.makeText(requireContext(), uiState.notificationMessage, Toast.LENGTH_SHORT)
                    .show()
                viewModel.notificationShown()
            }
            uiState.groupName.isNotEmpty() -> {
                binding.nameView.apply {
                    setText(uiState.groupName)
                    setSelection(uiState.groupName.length)
                }
            }
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