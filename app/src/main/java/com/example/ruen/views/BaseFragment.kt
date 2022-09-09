package com.example.ruen.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T: ViewBinding>(
    private val viewBindingInflate: (LayoutInflater, ViewGroup?, Boolean) -> T
) : Fragment() {
//
    private var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = viewBindingInflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}