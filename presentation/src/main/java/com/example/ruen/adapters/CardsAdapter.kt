package com.example.ruen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Card
import com.example.ruen.databinding.CardsItemBinding

class CardsAdapter(diffCallback: DiffUtil.ItemCallback<Card>) :
    BaseAdapter<Card, CardsAdapter.CardViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CardViewHolder(
            CardsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    inner class CardViewHolder(private val binding: CardsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.apply {
                setOnClickListener {
                    getItem(layoutPosition)?.let { position ->
                        clickListener?.invoke(position)
                    }
                }
                setOnLongClickListener {
                    getItem(layoutPosition)?.let { position ->
                        longClickListener?.invoke(position)
                    }
                    true
                }
            }
        }

        fun bind(card: Card?) = with(binding) {
            card?.let {
                wordView.text = it.value
            }
        }
    }

    object CardComparator : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Card, newItem: Card) = oldItem == newItem
    }
}