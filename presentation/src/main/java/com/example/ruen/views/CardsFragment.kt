package com.example.ruen.views

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ruen.R
import com.example.ruen.adapters.CardsAdapter
import com.example.ruen.databinding.FragmentCardsBinding
import com.example.ruen.viewmodels.CardsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class CardsFragment : BaseFragment<FragmentCardsBinding>(FragmentCardsBinding::inflate) {

    private val adapter: CardsAdapter by inject()
    private val viewModel: CardsViewModel by viewModel()
    private var groupId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = arguments?.getLong(GROUP_ID)
        if (id != null) {
            groupId = id
        } else {
            Log.e(TAG, "onCreate: No groupId in arguments bundle!")
            navController?.navigateUp()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        fetchData()
        setClickListeners()
    }

    private fun setClickListeners() = with(binding) {
        createCardView.setOnClickListener {
            navController?.navigate(R.id.action_cardsFragment_to_newCardDialogFragment)
        }
        startRepeatingView.setOnClickListener {
            navController?.navigate(R.id.action_cardsFragment_to_cardRepeatFragment)
        }
    }

    private fun fetchData() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupId?.let { groupId ->
                    viewModel.getCardsFlow(groupId).collectLatest {
                        adapter.submitData(it)
                    }
                }
            }
        }
    }

    private fun setAdapter() = with(binding) {
        cardsView.adapter = adapter
    }

    companion object {
        const val GROUP_ID = "group_id"
        private const val TAG = "CardsFragment"
    }
}