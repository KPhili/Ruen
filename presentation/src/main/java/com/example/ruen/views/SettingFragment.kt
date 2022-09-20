package com.example.ruen.views

import android.os.Bundle
import android.text.TextUtils
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.ruen.R

class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        arrayOf(
            findPreference<ListPreference>("theme"),
            findPreference<ListPreference>("start_fragment")
        ).forEach {
            it?.setSummaryProvider {
                val text = (it as? ListPreference)?.entry
                text
            }
        }

    }
}