package com.example.ruen.views

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.example.ruen.R
import com.example.ruen.databinding.ActivityMainBinding
import com.example.ruen.helpers.notifications.INotificationChannelHelper
import com.example.ruen.helpers.workmanager.WorkManagerHelper
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var binding: ActivityMainBinding
    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navHostFragment.navController
    }
    private val sharedPreference by lazy { PreferenceManager.getDefaultSharedPreferences(this) }
    private val notificationChannel: INotificationChannelHelper by inject()
    private val workManagerHelper: WorkManagerHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        checkAndSetTheme()
        setContentView(binding.root)
        setupToolbar()
    }

    override fun onResume() {
        super.onResume()
        sharedPreference.registerOnSharedPreferenceChangeListener(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }

    override fun onPause() {
        super.onPause()
        sharedPreference.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!onSupportNavigateUp()) super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupToolbar() = with(binding) {
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)
    }

    private fun checkAndSetTheme() {
        sharedPreference.getString(getString(R.string.preference_key_theme), null)?.let {
            val dayNightMode = when (it) {
                getString(R.string.setting_theme_values_system) -> {
                    setTheme(R.style.Theme_Ruen)
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                getString(R.string.setting_theme_values_light) -> {
                    setTheme(R.style.Theme_Ruen)
                    AppCompatDelegate.MODE_NIGHT_NO
                }
                getString(R.string.setting_theme_values_dark) -> {
                    setTheme(R.style.Theme_Ruen)
                    AppCompatDelegate.MODE_NIGHT_YES
                }
                getString(R.string.setting_theme_values_light_blue) -> {
                    setTheme(R.style.Theme_Ruen_Cornflower)
                    AppCompatDelegate.MODE_NIGHT_NO
                }
                else -> throw IllegalArgumentException("Illegal theme argument")
            }
            AppCompatDelegate.setDefaultNightMode(dayNightMode)
        }
    }

    private fun switchRepeatNotificationEnable() {
        val enable = sharedPreference.getBoolean(
            getString(R.string.preference_key_notification_repeat),
            false
        )
        if (enable) {
            notificationChannel.createChannels(INotificationChannelHelper.NotificationChannelType.DEFAULT)
            workManagerHelper.enableRepeatNotificationWorker()
        } else {
            workManagerHelper.cancelRepeatNotificationWorker()
        }
    }

    override fun onSharedPreferenceChanged(
        sp: SharedPreferences,
        name: String
    ) {
        when (name) {
            getString(R.string.preference_key_theme) -> {
                checkAndSetTheme()
                recreate()
            }
            getString(R.string.preference_key_notification_repeat) -> switchRepeatNotificationEnable()
        }
    }
}
