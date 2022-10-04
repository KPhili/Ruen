package com.example.ruen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Group
import com.example.ruen.databinding.GroupsItemBinding

class GroupsAdapter(diffCallback: DiffUtil.ItemCallback<Group>) :
    PagingDataAdapter<Group, GroupsAdapter.GroupViewHolder>(diffCallback) {

    private var deleteFunc: ((position: Int, group: Group) -> Unit)? = null

    fun setDeleteLogic(func: (position: Int,group: Group) -> Unit) {
        deleteFunc = func
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GroupViewHolder(
            GroupsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    fun deleteItem(position: Int) {
        deleteFunc?.let { deleteFunc ->
            getItem(position)?.let {
                deleteFunc(position, it)
            }
        }
    }


    class GroupViewHolder(private val binding: GroupsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(group: Group?) = with(binding) {
            group?.let {
                nameView.text = it.name
            }
        }
    }

    object GroupComparator : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Group, newItem: Group) = oldItem == newItem
    }
}