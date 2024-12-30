package org.mandziuk.calculalim.db.services

import android.content.Context
import org.mandziuk.calculalim.db.daos.ProfilDao
import org.mandziuk.calculalim.db.getProfilDao
import org.mandziuk.calculalim.db.models.Profil

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
}