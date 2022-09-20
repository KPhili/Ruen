package com.example.ruen.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.ruen.R
import com.example.ruen.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
    }

    private fun setupNavigation() = with(binding) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        getStartFragmentId()?.let {
            navGraph.setStartDestination(it)
        }
        navController.graph = navGraph
        bottomNavigationView.setupWithNavController(navController)
    }

    private fun getStartFragmentId(): Int? {
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPreference.getString(getString(R.string.preference_key_start_fragment), null)
            ?.let {
                when (it) {
                    getString(R.string.setting_start_fragment_values_new_card) -> R.id.newCardFragment
                    getString(R.string.setting_start_fragment_values_repeat) -> R.id.cardRepeatFragment
                    getString(R.string.setting_start_fragment_values_cards) -> R.id.cardsFragment
                    else -> throw UnsupportedOperationException("Unknown fragment for navigation graph")
                }
            }
    }
}