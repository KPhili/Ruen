package com.example.ruen.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.ruen.R
import com.example.ruen.adapters.CardsAdapter
import com.example.ruen.databinding.FragmentCardsBinding
import com.example.ruen.helpers.ItemTouchHelperCallback
import com.example.ruen.viewmodels.CardsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class CardsFragment : BaseFragment<FragmentCardsBinding>(FragmentCardsBinding::inflate) {

    private val adapter: CardsAdapter by inject()
    private val viewModel: CardsViewModel by viewModel { parametersOf(groupId) }
    private val groupId by lazy { args.groupId }
    private val args: CardsFragmentArgs by navArgs()
    private val actionBar by lazy { (requireActivity() as AppCompatActivity).supportActionBar }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        fetchData()
        setClickListeners()
    }

    override fun onPause() {
        super.onPause()
        actionBar?.subtitle = ""
    }

    private fun setClickListeners() = with(binding) {
        createCardView.setOnClickListener {
            navController?.navigate(
                CardsFragmentDirections.actionCardsFragmentToNewCardDialogFragment(
                    groupId
                )
            )
        }
        startRepeatingView.setOnClickListener {
            navController?.navigate(
                CardsFragmentDirections.actionCardsFragmentToCardRepeatFragment(
                    groupId
                )
            )
        }
    }

    private fun fetchData() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.cardsFlow.collectLatest {
                        adapter.submitData(it)
                    }
                }
                launch {
                    viewModel.loadGroupName().collectLatest {
                        actionBar?.title = it
                    }
                }
//                launch {
//                    viewModel.getCount().collectLatest {
//                        val pieces = getString(R.string.pieces)
//                        actionBar?.subtitle = "$it $pieces"
//                    }
//                }
            }
        }
    }

    private fun setAdapter() = with(binding) {
        cardsView.adapter = adapter
        adapter.setOnClickListener {
            it.id?.let { cardId ->
                val direction =
                    CardsFragmentDirections.actionCardsFragmentToSpecificCardRepeatFragment(
                        cardId, groupId
                    )
                navController?.navigate(direction)
            }
        }
        adapter.setOnLongClickListener {
            it.id?.let { cardId ->
                val direction =
                    CardsFragmentDirections.actionCardsFragmentToCardDialogFragment(groupId, cardId)
                navController?.navigate(direction)
            }
        }
        adapter.setOnDeleteListener { _, item ->
            viewModel.deleteCard(item)
        }
        ItemTouchHelper(ItemTouchHelperCallback(adapter, requireContext())).run {
            attachToRecyclerView(cardsView)
        }
    }
}