package org.mandziuk.calculalim.db.services

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.mandziuk.calculalim.activities.dataStore
import org.mandziuk.calculalim.db.models.Profil

class PreferencesService(val context: Context) {
    private val preferences = context.dataStore;
    private val profilKey = intPreferencesKey("profil");

    suspend fun getProfile(profileService: ProfileService): Profil {
        val profilActuel: Flow<Int> = preferences.data.map { d ->
            d[profilKey] ?: 0;
        };
        val profilId = profilActuel.first().toLong();
        val profile = profileService.getProfile(profilId);

        if (profilId != profile.id){
            preferences.edit { p ->
                p[profilKey] = profile.id.toInt();
            }
        }

        return profile;
    }
}