package org.mandziuk.calculalim.db.daos

import androidx.room.Dao
import androidx.room.Query
import org.mandziuk.calculalim.db.models.Profil

@Dao
interface ProfilDao {
    @Query("SELECT COUNT(ProfilID) FROM Profil")
    suspend fun profilCount(): Long

    @Query("INSERT INTO Profil (ProfilName, ProfilID) VALUES (:name, :id)")
    suspend fun insertProfil(name: String, id: Long);

    /**
     * Récupère le profil recherché. S'il n'existe pas, rien n'est retourné.
     */
    @Query("SELECT * FROM Profil WHERE ProfilID = :id")
    suspend fun getProfil(id: Long): Profil?;

    /**
     * Récupère le premier profil de la base de données. Devrait être utilisé si aucun profil n'est trouvé.
     */
    @Query("SELECT * FROM Profil LIMIT 1")
    suspend fun premierProfil(): Profil
}