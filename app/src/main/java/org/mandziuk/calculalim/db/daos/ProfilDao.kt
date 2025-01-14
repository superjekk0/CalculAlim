package org.mandziuk.calculalim.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import org.mandziuk.calculalim.db.models.FoodRepas
import org.mandziuk.calculalim.db.models.Profil
import org.mandziuk.calculalim.db.models.Repas
import org.mandziuk.calculalim.db.views.FoodRepasDetails

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

    @Query("SELECT * FROM Profil")
    suspend fun getProfiles(): List<Profil>;

    /**
     * Récupère le premier profil de la base de données. Devrait être utilisé si aucun profil n'est trouvé.
     */
    @Query("SELECT * FROM Profil LIMIT 1")
    suspend fun premierProfil(): Profil;

    @Insert(onConflict = OnConflictStrategy.ABORT, entity = Repas::class)
    suspend fun insertRepas(repas: Repas) : Long;

    @Insert(onConflict = OnConflictStrategy.ABORT, entity = FoodRepas::class)
    suspend fun insertFoodRepas(foodRepas: FoodRepas);

    @Transaction
    suspend fun insertFoodRepasList(foodRepas: List<FoodRepas>) = foodRepas.forEach { insertFoodRepas(it) };

    @Query("SELECT * FROM Repas WHERE ProfilID = :profilId ORDER BY RepasDate DESC")
    suspend fun getMeals(profilId: Long) : List<Repas>;

    @Query("SELECT * FROM FoodRepasDetails WHERE RepasID = :repasId")
    suspend fun getFoodRepasDetails(repasId: Long): List<FoodRepasDetails>;

    @Query("SELECT COUNT(ProfilName) FROM Profil WHERE ProfilName = :name")
    suspend fun takenProfileName(name: String): Boolean;

    @Update(entity = Profil::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun updateProfileName(profil: Profil);

    @Insert(onConflict = OnConflictStrategy.ABORT, entity = Profil::class)
    suspend fun insertProfile(profil: Profil) : Long;
}