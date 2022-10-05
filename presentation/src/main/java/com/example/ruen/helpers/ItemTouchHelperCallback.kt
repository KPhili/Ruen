package com.example.ruen.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.ruen.R
import com.example.ruen.adapters.GroupsAdapter

class ItemTouchHelperCallback(private val myAdapter: GroupsAdapter, private val context: Context) :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.LEFT
    ) {

    private val background = ColorDrawable(Color.RED);
    private val icon by lazy { ContextCompat.getDrawable(context, R.drawable.folder_delete) }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.layoutPosition
        myAdapter.deleteItem(position)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val view = viewHolder.itemView
        if (dX < 0) {
            background.setBounds((view.right + dX).toInt(), view.top, view.right, view.bottom)
            icon?.setBounds(view.left, view.top, view.right, view.bottom)
        } else {
            background.setBounds(0, 0, 0, 0)
        }
        background.draw(c)
        icon?.let { icon ->
            val iconMargin = (view.height - icon.intrinsicHeight) / 2
            val iconTop = view.top + iconMargin
            val iconBottom = iconTop + icon.intrinsicHeight
            if (dX < 0) {
                val iconLeft = view.right - iconMargin - icon.intrinsicWidth
                val iconRight = view.right - iconMargin
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                icon.draw(c)
            }
        }
    }
}