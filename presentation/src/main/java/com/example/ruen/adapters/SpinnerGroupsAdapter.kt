package com.example.ruen.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.example.domain.models.Group
import com.example.ruen.R

class SpinnerGroupsAdapter(
    context: Context,
    @LayoutRes private val resource: Int,
    @IdRes private val textViewResourceId: Int
) : ArrayAdapter<Group>(context, resource, textViewResourceId) {
    private val mObjects: List<Group>? = null
    private val inflater = LayoutInflater.from(context)

    override fun getItemId(position: Int): Long {
        return this.getItem(position)?.id ?: throw Exception("The group cannot be without an id")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(convertView, parent, position)
    }

    fun getPositionById(id: Long): Int {
        return mObjects?.indexOfFirst { it.id == id } ?: -1
    }

    private fun createView(
        convertView: View?,
        parent: ViewGroup,
        position: Int
    ): View {
        val view = convertView ?: inflater.inflate(resource, parent, false)
        view.findViewById<TextView>(R.id.groupNameView).text = getItem(position)?.name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(convertView, parent, position)
    }
}