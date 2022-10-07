package com.example.ruen.adapters

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class BaseAdapter<T : Any, V : ViewHolder>(diffCallback: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, V>(diffCallback), IDeleteItemAdapter {

    private var deleteListener: ((position: Int, item: T) -> Unit)? = null
    protected var clickListener: ((item: T) -> Unit)? = null


    fun setOnDeleteListener(listener: (position: Int, item: T) -> Unit) {
        deleteListener = listener
    }

    fun setOnClickListener(listener: (item: T) -> Unit) {
        clickListener = listener
    }

    override fun deleteItem(position: Int) {
        deleteListener?.let { deleteFunc ->
            getItem(position)?.let {
                deleteFunc(position, it)
            }
        }
    }
}