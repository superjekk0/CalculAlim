package org.mandziuk.calculalim.db.services

import android.content.Context
import android.util.Log
import org.mandziuk.calculalim.db.daos.ProfilDao
import org.mandziuk.calculalim.db.dtos.mealDtoInstance
import org.mandziuk.calculalim.db.getProfilDao
import org.mandziuk.calculalim.db.models.FoodRepas
import org.mandziuk.calculalim.db.models.Profil
import org.mandziuk.calculalim.db.models.Repas

class ProfileService(private val context: Context) {
    private val profilDao: ProfilDao = getProfilDao(context);

    suspend fun getProfile(id: Long): Profil {
        if (profilDao.profilCount() == 0L){
            val profil = Profil(0L, "Profil 1");
            profilDao.insertProfil(profil.name, profil.id);
            return profil;
        }

        return profilDao.getProfil(id) ?: return profilDao.premierProfil();
    }

    suspend fun addMeal(){
        val profil = PreferencesService(context).getProfile();
        val repas = Repas(0L, profil.id);
        repas.id = profilDao.insertRepas(repas);
        val foodRepas = mealDtoInstance.map { f ->
            FoodRepas(f.foodId, repas.id, f.weight);
        }
        profilDao.insertFoodRepasList(foodRepas);
        mealDtoInstance.clear();
    }
}