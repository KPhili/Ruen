package com.example.ruen.views

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ruen.adapters.GroupsAdapter
import com.example.ruen.databinding.FragmentGroupsBinding
import com.example.ruen.viewmodels.GroupsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class GroupsFragment : BaseFragment<FragmentGroupsBinding>(FragmentGroupsBinding::inflate) {
    private val viewModel by viewModel<GroupsViewModel>()
    private val adapter: GroupsAdapter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        collectData()
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupsFlow.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun setAdapter() = with(binding) {
        groupsView.adapter = adapter
    }
}