package org.mandziuk.calculalim.db

import androidx.room.Dao
import androidx.room.Query
import org.mandziuk.calculalim.db.dtos.FoodDTO
import org.mandziuk.calculalim.db.models.Food
import org.mandziuk.calculalim.db.models.FoodGroup
import org.mandziuk.calculalim.db.views.FoodAndGroupNames
import org.mandziuk.calculalim.db.views.FoodNutrientDetails

@Dao
interface FoodDao {
    @Query("SELECT * FROM FoodGroup " +
            "ORDER BY FoodGroupID ASC")
    suspend fun getGroups() : List<FoodGroup>

    @Query("SELECT * FROM FoodAndGroupNames " +
            "WHERE (:groupeId = 0 OR FoodGroupID = :groupeId)" +
            "AND (:nom IS NULL OR FoodDescriptionF LIKE '%' || :nom || '%') " +
            "ORDER BY instr(FoodDescription ,:nom) ASC, FoodGroupID ASC")
    suspend fun getFoodsFr(groupeId: Long, nom: String) : List<FoodAndGroupNames>

    @Query("SELECT * FROM FoodAndGroupNames " +
            "WHERE (:groupeId = 0 OR FoodGroupID = :groupeId)" +
            "AND (:nom IS NULL OR FoodDescription LIKE '%' || :nom || '%') " +
            "ORDER BY instr(FoodDescription ,:nom) ASC, FoodGroupID ASC")
    suspend fun getFoodsEn(groupeId: Long, nom: String) : List<FoodAndGroupNames>

    @Query("SELECT * FROM Food " +
            "WHERE FoodID = :foodId")
    suspend fun getFood(foodId: Long) : Food

    @Query("SELECT * FROM FoodNutrientDetails " +
            "WHERE FoodID = :foodId ")
    suspend fun getFoodNutrients(foodId: Long) : List<FoodNutrientDetails>;
}