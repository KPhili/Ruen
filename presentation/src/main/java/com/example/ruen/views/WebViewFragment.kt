package com.example.ruen.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.ruen.databinding.FragmentWebviewBinding
import kotlin.math.abs

class WebViewFragment : BaseFragment<FragmentWebviewBinding>(FragmentWebviewBinding::inflate) {

    private val args: WebViewFragmentArgs by navArgs()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = args.url
        binding.webView.apply {
            webViewClient = WebViewClient()
            setOnTouchListener(OnTouchToClickListenerAdapter(this))
            settings.apply {
                javaScriptEnabled = true
            }
            loadUrl(url)
        }
    }

    companion object {
        private const val TAG = "WebViewFragment"
    }

    private inner class OnTouchToClickListenerAdapter(private val webView: WebView) :
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
                    Log.d(TAG, "onViewCreated: ${webView.hitTestResult.type}")
                    Log.d(TAG, "onViewCreated: $it")
                }
            }
        }

    }
}

