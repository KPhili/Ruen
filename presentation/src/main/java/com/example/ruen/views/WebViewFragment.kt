package com.example.ruen.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.ruen.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.abs

class WebViewFragment : BottomSheetDialogFragment() {

    private val args: WebViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = args.url
        val webView = view.findViewById<WebView>(R.id.webView)
        webView.apply {
            webViewClient = WebViewClient()
            setOnTouchListener(OnTouchListener(this))
            settings.apply {
                javaScriptEnabled = true
            }
            loadUrl(url)
        }
    }

    companion object {
        private const val TAG = "WebViewFragment"
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
                    Log.d(TAG, "onViewCreated: $it")
                    dismiss()
                }
            }
        }

    }
}

