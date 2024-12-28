package org.mandziuk.calculalim.db.daos

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ProfilDao {
    @Query("SELECT COUNT(ProfilID) FROM Profil")
    suspend fun profilCount(): Long
}