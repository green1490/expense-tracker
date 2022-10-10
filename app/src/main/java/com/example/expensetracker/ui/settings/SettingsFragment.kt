package com.example.expensetracker.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import com.example.expensetracker.R


class SettingsFragment : PreferenceFragmentCompat(),SharedPreferences.OnSharedPreferenceChangeListener{

    lateinit var sharedPreferences:SharedPreferences


    override  fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences,rootKey)
    }

    override fun onStart() {
        super.onStart()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        sharedPreferences =  requireContext().getSharedPreferences("setting",Context.MODE_PRIVATE)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val darkModeString = getString(R.string.dark_mode)
        val languageString =  getString(R.string.language_key)
        key?.let {
            if (it == darkModeString) sharedPreferences?.let { pref ->
                val darkModeValues = resources.getStringArray(R.array.dark_mode_values)
                when (pref.getString(darkModeString, darkModeValues[0])) {
                    darkModeValues[0] -> darkMode()
                    darkModeValues[1] -> lightMode()
                }
            }
            //Kotlin's if expressions must have else expression, but we
            //don't for that so we are checking for every key.
            if (it == languageString) sharedPreferences?.let { pref ->
                val languageValues = resources.getStringArray(R.array.language_values)
                when (pref.getString(languageString, languageValues[0])) {
                    languageValues[0] -> englishLanguage()
                    languageValues[1] -> hungarianLanguage()
                }
            }
        }
    }

    private fun hungarianLanguage() {

    }

    private fun englishLanguage() {

    }

    fun darkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        with (sharedPreferences.edit()) {
            this?.putInt("Theme", AppCompatDelegate.MODE_NIGHT_NO)
            this?.apply()
        }
    }

    fun lightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        with (sharedPreferences.edit()) {
            this?.putInt("Theme", AppCompatDelegate.MODE_NIGHT_YES)
            this?.apply()
        }
    }
}