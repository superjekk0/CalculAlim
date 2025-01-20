package org.mandziuk.calculalim.db.services

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.mandziuk.calculalim.activities.dataStore
import org.mandziuk.calculalim.db.daos.ProfilDao
import org.mandziuk.calculalim.db.dtos.MealDTO
import org.mandziuk.calculalim.db.dtos.mealDtoInstance
import org.mandziuk.calculalim.db.getProfilDao
import org.mandziuk.calculalim.db.models.FoodRepas
import org.mandziuk.calculalim.db.models.Profil
import org.mandziuk.calculalim.db.models.Repas
import java.util.Locale

class ProfileService(private val context: Context) {
    private val profilDao: ProfilDao = getProfilDao(context);
    private val preferences = context.dataStore;

    private val profilKey = intPreferencesKey("profil");

    suspend fun getProfile(): Profil {
        val profileService = ProfileService(context);
        val profilActuel: Flow<Int> = preferences.data.map { d ->
            d[profilKey] ?: 0;
        };
        val profilId = profilActuel.first().toLong();
        Log.i("Profil", profilId.toString());
        val profile = profileService.getProfile(profilId);

        if (profilId != profile.id){
            preferences.edit { p ->
                p[profilKey] = profile.id.toInt();
            }
        }

        return profile;
    }

    suspend fun getProfiles(): ArrayList<Profil>{
        return ArrayList(profilDao.getProfiles());
    }

    suspend fun availableProfileName(name: String): Boolean{
        return ! profilDao.takenProfileName(name);
    }

    suspend fun updateProfileName(profil: Profil){
        profilDao.updateProfileName(profil);
    }

    suspend fun createProfile(profil: Profil): Profil{
        val profilId = profilDao.insertProfile(profil);
        return Profil(profilId, profil.name, null);
    }

    suspend fun setProfile(profilId: Long): Profil{
        preferences.edit { p ->
            p[profilKey] = profilId.toInt();
        }
        return getProfile(profilId);
    }

    private suspend fun getProfile(id: Long): Profil {
        if (profilDao.profilCount() == 0L){
            val profil = Profil(0L, "Profil 1", null);
            profilDao.insertProfil(profil.name, profil.id);
            return profil;
        }

        return profilDao.getProfil(id) ?: return profilDao.premierProfil();
    }

    suspend fun addMeal(){
        val profil = getProfile();
        val repas = Repas(0L, profil.id);
        repas.id = profilDao.insertRepas(repas);
        val foodRepas = mealDtoInstance.filter { it.displayed }.map { f ->
            FoodRepas(f.foodId, repas.id, f.weight);
        }
        profilDao.insertFoodRepasList(foodRepas);
        mealDtoInstance.clear();
    }

    suspend fun getMeals() : List<Repas>{
        val profil = getProfile();
        return profilDao.getMeals(profil.id);
    }

    suspend fun getFoodRepas(repasId: Long): MealDTO {
        val resultat = MealDTO();
        val foodRepasDetails = profilDao.getFoodRepasDetails(repasId);
        val locale = Locale.getDefault().displayLanguage;
        resultat.addAll(foodRepasDetails.map { f ->
            MealDTO.FoodMealDTO(f.foodId,
                if (locale.contains("fr", true))
                    f.foodNameFr
                else
                    f.foodName,
                f.quantity);
        });
        return resultat;
    }
}