package com.example.ruen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Group
import com.example.ruen.databinding.GroupsItemBinding

class GroupsAdapter(diffCallback: DiffUtil.ItemCallback<Group>) :
    BaseAdapter<Group, GroupsAdapter.GroupViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GroupViewHolder(
            GroupsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    inner class GroupViewHolder(private val binding: GroupsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding.root) {
                setOnClickListener {
                    getItem(layoutPosition)?.let { group ->
                        clickListener?.invoke(group)
                    }
                }
                setOnLongClickListener {
                    getItem(layoutPosition)?.let { group ->
                        longClickListener?.invoke(group)
                    }
                    true
                }
            }
        }

        fun bind(group: Group?) = with(binding) {
            group?.let {
                nameView.text = group.name
            }
        }
    }

    object GroupComparator : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Group, newItem: Group) = oldItem == newItem
    }
}