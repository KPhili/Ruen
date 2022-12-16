package com.example.ruen.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.ruen.R

class ShareIntentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_intent)
        findViewById<FrameLayout>(R.id.backgroundView).setOnClickListener {
            finish()
        }
        onNewIntent(intent)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navHostFragment.navController.setOnBackPressedDispatcher(onBackPressedDispatcher)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            if (checkIntentType(intent)) {
                val text = intent.getStringExtra(Intent.EXTRA_TEXT)?.capitalize()
                val regex =
                    Regex("^\"([^\"]+)", setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL))
                text?.let {
                    val match = regex.find(text)
                    val resultWord =
                        (match?.groups?.takeIf { it.isNotEmpty() }?.get(1)?.value ?: text).trim()
                    if (resultWord.isEmpty()) finish()
                    val bundle = CardFragmentArgs.Builder(-1L, -1L, resultWord).build().toBundle()
                    val navHostFragment =
                        NavHostFragment.create(R.navigation.new_card_nav_graph, bundle)
                    supportFragmentManager.commit {
                        replace(R.id.fragmentContainerView, navHostFragment)
                        setPrimaryNavigationFragment(navHostFragment)
                    }
                }
            } else {
                finish()
            }
        } ?: finish()
    }

    private fun checkIntentType(intent: Intent) =
        intent.action == Intent.ACTION_SEND && intent.type == "text/plain"


    companion object {
        private const val TAG = "ShareIntentActivity"
    }
}