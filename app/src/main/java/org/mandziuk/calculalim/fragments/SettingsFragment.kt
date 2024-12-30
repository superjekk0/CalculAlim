package org.mandziuk.calculalim.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.coroutineScope
import androidx.preference.ListPreference
import androidx.preference.MultiSelectListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.db.services.FoodService

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val nutrimentsAffiches = findPreference<MultiSelectListPreference>("nutriments");

        lifecycle.coroutineScope.launch {
            val foodService = FoodService(requireContext());
            val nutriments = foodService.getNutrientNames();
            nutrimentsAffiches?.entries = nutriments.map { n -> n.nutrientName }.toTypedArray();
            nutrimentsAffiches?.entryValues = nutriments.map { n -> n.nutrientId.toString() }.toTypedArray();
            nutrimentsAffiches?.values = nutriments.filter { n -> n.enabled }.map { n -> n.nutrientId.toString() }.toSet();
        }

        findPreference<SwitchPreference>("modeSombre")?.setOnPreferenceChangeListener { preference, newValue ->
            requireActivity().recreate();
            true;
        }

        findPreference<ListPreference>("language")?.setOnPreferenceChangeListener { _, newValue ->
            val localeListPreference = LocaleListCompat.forLanguageTags(newValue as String);
            AppCompatDelegate.setApplicationLocales(localeListPreference);

            requireActivity().recreate();
            return@setOnPreferenceChangeListener true;
        }

        nutrimentsAffiches?.setOnPreferenceClickListener {
            preference ->
            
            return@setOnPreferenceClickListener true;
        }
    }
}