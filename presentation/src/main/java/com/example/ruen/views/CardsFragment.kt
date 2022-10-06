package com.example.ruen.views

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
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
    private val groupId by lazy { args.groupId }
    private val args: CardsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        fetchData()
        setClickListeners()
    }

    private fun setClickListeners() = with(binding) {
        createCardView.setOnClickListener {
            navController?.navigate(CardsFragmentDirections.actionCardsFragmentToNewCardDialogFragment(groupId))
        }
        startRepeatingView.setOnClickListener {
            navController?.navigate(CardsFragmentDirections.actionCardsFragmentToCardRepeatFragment(groupId))
        }
    }

    private fun fetchData() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getCardsFlow(groupId).collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun setAdapter() = with(binding) {
        cardsView.adapter = adapter
    }
}