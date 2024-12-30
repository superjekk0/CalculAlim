package org.mandziuk.calculalim.fragments

import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.LocaleManagerCompat
import androidx.core.os.LocaleListCompat
import androidx.preference.DropDownPreference
import androidx.preference.MultiSelectListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import org.mandziuk.calculalim.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<SwitchPreference>("modeSombre")?.setOnPreferenceChangeListener { preference, newValue ->
            requireActivity().recreate();
            true;
        }

        findPreference<DropDownPreference>("language")?.setOnPreferenceChangeListener { _, newValue ->
            val localeListPreference = LocaleListCompat.forLanguageTags(newValue as String);
            AppCompatDelegate.setApplicationLocales(localeListPreference);

            requireActivity().recreate();
            return@setOnPreferenceChangeListener true;
        }

        findPreference<MultiSelectListPreference>("nutriments")?.setOnPreferenceClickListener {
            _ ->
            return@setOnPreferenceClickListener true;
        }
    }
}