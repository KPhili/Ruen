package com.example.ruen.views

import android.os.Bundle
import android.text.TextUtils
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import com.example.ruen.R

class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        findPreference<ListPreference>("theme")?.setSummaryProvider {
            val text = (it as? ListPreference)?.entry
            text
        }
        val startTimeNotificationSeekBar =
            findPreference<SeekBarPreference>(getString(R.string.preference_key_notification_start_time)) ?: return
        val endTimeNotificationSeekBar =
            findPreference<SeekBarPreference>(getString(R.string.preference_key_notification_end_time)) ?: return
        startTimeNotificationSeekBar.apply {
            setSeekbarSummary(this, value)
            setOnPreferenceChangeListener { _, newValueObject ->
                val newValue = newValueObject as Int
                if (endTimeNotificationSeekBar.value < newValue) {
                    setSeekbarValueAndSummary(endTimeNotificationSeekBar, newValue)
                }
                setSeekbarSummary(this, newValue)
                true
            }
        }
        endTimeNotificationSeekBar.apply {
            setSeekbarSummary(this, value)
            setOnPreferenceChangeListener { _, newValueObject ->
                val newValue = newValueObject as Int
                val startTimeValue = startTimeNotificationSeekBar.value
                if (startTimeValue > newValue) {
                    setSeekbarValueAndSummary(this, startTimeValue)
                    return@setOnPreferenceChangeListener false
                } else {
                    setSeekbarSummary(this, newValue)
                    return@setOnPreferenceChangeListener true
                }
            }
        }
    }

    private fun setSeekbarValueAndSummary(preference: SeekBarPreference, value: Int) {
        preference.value = value
        setSeekbarSummary(preference, value)
    }

    private fun setSeekbarSummary(preference: SeekBarPreference, value: Int) {
        val hourPlural = resources.getQuantityString(R.plurals.hours, value)
        preference.summary = "$value $hourPlural"
    }
}