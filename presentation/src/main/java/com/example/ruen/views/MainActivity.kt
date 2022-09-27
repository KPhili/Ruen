package com.example.ruen.views

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.example.ruen.R
import com.example.ruen.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var binding: ActivityMainBinding

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navHostFragment.navController
    }
    private val sharedPreference by lazy { PreferenceManager.getDefaultSharedPreferences(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkAndSetTheme()
        setupActionBar()
        setupNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        sharedPreference.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        sharedPreference.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                navController.navigate(R.id.action_cardRepeatFragment_to_settingFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupNavigation() = with(binding) {
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        getStartFragmentId()?.let {
            navGraph.setStartDestination(it)
        }
        navController.graph = navGraph
    }

    private fun getStartFragmentId() =
        sharedPreference.getString(getString(R.string.preference_key_start_fragment), null)?.let {
            when (it) {
                getString(R.string.setting_start_fragment_values_new_card) -> R.id.newCardFragment
                getString(R.string.setting_start_fragment_values_repeat) -> R.id.cardRepeatFragment
                getString(R.string.setting_start_fragment_values_cards) -> R.id.cardsFragment
                else -> throw IllegalArgumentException("Unknown fragment for navigation graph")
            }
        }

    private fun checkAndSetTheme() {
        sharedPreference.getString(getString(R.string.preference_key_theme), null)?.let {
            val dayNightMode = when (it) {
                getString(R.string.setting_theme_values_system) -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                getString(R.string.setting_theme_values_light) -> AppCompatDelegate.MODE_NIGHT_NO
                getString(R.string.setting_theme_values_dark) -> AppCompatDelegate.MODE_NIGHT_YES
                else -> throw IllegalArgumentException("Illegal theme argument")
            }
            AppCompatDelegate.setDefaultNightMode(dayNightMode)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, name: String?) {
        if (name == getString(R.string.preference_key_theme)) {
            checkAndSetTheme()
        }
    }
}
