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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class CardsFragment : BaseFragment<FragmentCardsBinding>(FragmentCardsBinding::inflate) {

    private val adapter: CardsAdapter by inject()
    private val viewModel: CardsViewModel by viewModel()

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
    }

    private fun fetchData() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cardsFlow.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun setAdapter() = with(binding) {
        cardsView.adapter = adapter
    }
}