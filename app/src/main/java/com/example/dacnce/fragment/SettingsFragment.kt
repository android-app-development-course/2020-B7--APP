package com.example.dacnce.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.dacnce.R
import com.google.android.material.snackbar.Snackbar


class SettingsFragment: PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_settings);
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        val stringValue = newValue.toString();
        if (preference is ListPreference) {
            val listPreference = preference as ListPreference;
            val index = listPreference.findIndexOfValue(stringValue);
            if(index >= 0){
                preference.summary = listPreference.entry[index].toString()
            }else{
                preference.summary = null
            }
        } else {
            preference?.summary = stringValue;
        }
        return true;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        onPreferenceChange(findPreference("pref_text"), preferences.getString("pref_text", ""))
        onPreferenceChange(findPreference("pref_list"), preferences.getString("pref_list", ""))
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference.key != null) {
            when (preference.key) {
                "pref_click" -> Snackbar.make(
                    listView,
                    getString(R.string.pref_on_preference_click),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            return true
        }
        return super.onPreferenceTreeClick(preference)
    }


}