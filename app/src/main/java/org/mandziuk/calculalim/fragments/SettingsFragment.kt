package org.mandziuk.calculalim.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.coroutineScope
import androidx.preference.ListPreference
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.activities.MainActivity
import org.mandziuk.calculalim.db.foodDao
import org.mandziuk.calculalim.db.foodDb
import org.mandziuk.calculalim.db.profilDao
import org.mandziuk.calculalim.db.services.FoodService
import org.mandziuk.calculalim.dialogs.ResetDialog
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat() {
    val version: String?
        get(){
            return requireContext().packageManager.getPackageInfo(requireContext().packageName, 0).versionName;
        }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val nutrimentsAffiches = findPreference<MultiSelectListPreference>("nutriments");
        val language = findPreference<ListPreference>("language");
        language?.value = Locale.getDefault().toLanguageTag();

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

        findPreference<Preference>("reset")?.setOnPreferenceClickListener {
            val alertDialog = ResetDialog(requireContext());

            alertDialog.setOnDismissListener {
                if (!alertDialog.annule) {
                    requireContext().deleteDatabase("food.db");
                    foodDb = null;
                    foodDao = null;
                    profilDao = null;
                    Runtime.getRuntime().exit(0);
                }
            }

            alertDialog.show();
            true;
        }

        findPreference<Preference>("corbeille")?.setOnPreferenceClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java);
            intent.putExtra("elementsSupprimes", true);
            startActivity(intent);
            true;
        }

        findPreference<Preference>("version")?.summary = requireContext().getString(R.string.versionUtilisee, version);

        language?.setOnPreferenceChangeListener { _, newValue ->
            val localeListPreference = LocaleListCompat.forLanguageTags(newValue as String);
            AppCompatDelegate.setApplicationLocales(localeListPreference);

            return@setOnPreferenceChangeListener true;
        }

        nutrimentsAffiches?.setOnPreferenceChangeListener {
            _, newValue ->
            lifecycle.coroutineScope.launch {
                if (newValue is Set<*>)
                    modificationAffichageNutriments(newValue as Set<CharSequence>);
            }
            return@setOnPreferenceChangeListener true;
        }
    }

    private suspend fun modificationAffichageNutriments(nutrimentsIdAffiches: Set<CharSequence>) {
        val foodService = FoodService(requireContext());
        foodService.changeNutrientDisplays(nutrimentsIdAffiches.map { n -> n.toString().toLong() });
    }
}