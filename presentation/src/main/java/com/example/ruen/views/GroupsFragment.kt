package com.example.ruen.views

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.ruen.R
import com.example.ruen.adapters.GroupsAdapter
import com.example.ruen.databinding.FragmentGroupsBinding
import com.example.ruen.helpers.GroupItemTouchHelper
import com.example.ruen.viewmodels.GroupsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.nio.file.Files.delete

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
            setDeleteLogic { position, group ->
                Log.d(TAG, "setupRecyclerView: ${group.name}")
                AlertDialog.Builder(requireContext())
                    .setTitle("Удалить группу: ${group.name}?")
                    .setMessage("Также удалятся все карточки группы")
                    .setPositiveButton("Да") { dialogInterface: DialogInterface, i: Int ->
                        viewModel.deleteGroup(group)
                    }
                    .setNegativeButton("Отмена") { dialogInterface: DialogInterface, i: Int ->
                        adapter.notifyItemChanged(position)
                    }
                    .setOnDismissListener{
                        adapter.notifyItemChanged(position)
                    }
                    .setIcon(R.drawable.folder_delete)
                    .create()
                    .show()
            }
        }
        ItemTouchHelper(GroupItemTouchHelper(adapter, requireContext())).also {
            it.attachToRecyclerView(groupsView)
        }
    }

    companion object {
        private const val TAG = "GroupsFragment"
    }
}