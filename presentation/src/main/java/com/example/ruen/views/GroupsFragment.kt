package com.example.ruen.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.domain.models.Group
import com.example.ruen.R
import com.example.ruen.adapters.GroupsAdapter
import com.example.ruen.databinding.FragmentGroupsBinding
import com.example.ruen.helpers.ItemTouchHelperCallback
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
        setupRecyclerView()
        collectData()
        setClickListeners()
    }

    private fun setClickListeners() = with(binding) {
        createGroupView.setOnClickListener {
            navController?.navigate(R.id.action_groupsFragment_to_newGroupDialogFragment)
        }
        startRepeatingView.setOnClickListener {
            navController?.navigate(R.id.action_groupsFragment_to_cardRepeatFragment)
        }
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

    private fun setupRecyclerView() = with(binding) {
        groupsView.adapter = adapter.apply {
            setOnDeleteListener { position, group ->
                showGroupDeleteAlertDialog(group, position)
            }
            setOnClickListener { group ->
                group.id?.let {
                    val direction =
                        GroupsFragmentDirections.actionGroupsFragmentToCardsFragment(it)
                    navController?.navigate(direction)
                }
            }
            setOnLongClickListener { group ->
                group.id?.let {
                    val direction =
                        GroupsFragmentDirections.actionGroupsFragmentToNewGroupDialogFragment()
                                        .setGroupId(it)
                    navController?.navigate(direction)
                }
            }
        }
        ItemTouchHelper(ItemTouchHelperCallback(adapter, requireContext())).also {
            it.attachToRecyclerView(groupsView)
        }
    }

    private fun showGroupDeleteAlertDialog(
        group: Group,
        position: Int
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.group_alert_delete_title, group.name))
            .setMessage(getString(R.string.group_alert_delete_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteGroup(group)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setOnDismissListener {
                adapter.notifyItemChanged(position)
            }
            .setIcon(R.drawable.folder_delete)
            .create()
            .show()
    }

    companion object {
        private const val TAG = "GroupsFragment"
    }
}
