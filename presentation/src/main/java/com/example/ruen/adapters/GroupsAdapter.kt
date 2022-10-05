package com.example.ruen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Group
import com.example.ruen.databinding.GroupsItemBinding

class GroupsAdapter(diffCallback: DiffUtil.ItemCallback<Group>) :
    PagingDataAdapter<Group, GroupsAdapter.GroupViewHolder>(diffCallback) {

    private var onDeleteListener: ((position: Int, group: Group) -> Unit)? = null
    private var onClickListener: ((group: Group) -> Unit)? = null

    fun setOnDeleteListener(listener: (position: Int, group: Group) -> Unit) {
        onDeleteListener = listener
    }

    fun setOnClickListener(listener: (group: Group) -> Unit) {
        onClickListener = listener
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
        onDeleteListener?.let { deleteFunc ->
            getItem(position)?.let {
                deleteFunc(position, it)
            }
        }
    }


    inner class GroupViewHolder(private val binding: GroupsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(group: Group?) = with(binding) {
            group?.let {
                nameView.text = group.name
                groupItemView.setOnClickListener{
                    onClickListener?.invoke(group)
                }
            }
        }
    }

    object GroupComparator : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Group, newItem: Group) = oldItem == newItem
    }
}