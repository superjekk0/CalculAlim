package org.mandziuk.calculalim.db

import androidx.room.Dao
import androidx.room.Query
import org.mandziuk.calculalim.db.models.FoodGroup

@Dao
interface FoodDao {
    @Query("SELECT * FROM FoodGroup")
    fun getGroups() : List<FoodGroup>

    // TODO : Utiliser l'annotation @DatabaseView() pour obtenir les informations d'aliments
}