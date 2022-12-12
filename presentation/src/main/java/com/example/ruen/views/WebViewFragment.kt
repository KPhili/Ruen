package com.example.ruen.views

import android.annotation.SuppressLint
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ruen.R
import com.example.ruen.viewmodels.WebViewViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class WebViewFragment : BottomSheetDialogFragment() {

    private val args: WebViewFragmentArgs by navArgs()
    private val viewModel: WebViewViewModel by viewModel()
    private var webView: WebView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val word = args.word
        viewModel.findImages(word)
        webViewSetup(view)
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { url ->
                    if (url != null) {
                        webView?.loadUrl(url)
                    }
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewSetup(view: View) {
        webView = view.findViewById<WebView>(R.id.webView)
        webView?.apply {
            webViewClient = WebViewClient()
            setOnTouchListener(OnTouchListener(this))
            settings.apply {
                javaScriptEnabled = true
            }
        }
    }

    companion object {
        private const val TAG = "WebViewFragment"
        const val URL = "url"
    }

    private inner class OnTouchListener(private val webView: WebView) :
        View.OnTouchListener {
        private var rawX = 0f
        private var rawY = 0f

        private val maxOffset = 20

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            when (event.getAction()) {
                MotionEvent.ACTION_DOWN -> {
                    rawX = event.rawX
                    rawY = event.rawY
                }
                MotionEvent.ACTION_UP -> {
                    val offsetX = rawX - event.rawX
                    val offsetY = rawY - event.rawY
                    if (abs(offsetX) < maxOffset && abs(offsetY) < maxOffset) {
                        onClick()
                        return true
                    }
                }
            }
            return false

        }

        private fun onClick() {
            val hitTestResult = webView.hitTestResult
            if (hitTestResult.type in arrayOf(
                    WebView.HitTestResult.IMAGE_TYPE,
                    WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE
                )
            ) {
                val imageUrl = webView.hitTestResult.extra
                imageUrl?.let {
                    if (URLUtil.isNetworkUrl(it)) {
                        findNavController()
                            .previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(URL, it)
                    }
                    dismiss()
                }
            }
        }
    }

}

