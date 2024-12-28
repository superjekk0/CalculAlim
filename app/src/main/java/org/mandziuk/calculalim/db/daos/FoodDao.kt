package org.mandziuk.calculalim.db.daos

import androidx.room.Dao
import androidx.room.Query
import org.mandziuk.calculalim.db.models.DiscardFood
import org.mandziuk.calculalim.db.models.Food
import org.mandziuk.calculalim.db.models.FoodGroup
import org.mandziuk.calculalim.db.views.ConversionDetails
import org.mandziuk.calculalim.db.views.FoodAndGroupNames
import org.mandziuk.calculalim.db.views.FoodNutrientDetails

@Dao
interface FoodDao {
    @Query("SELECT * FROM FoodGroup " +
            "ORDER BY FoodGroupID ASC")
    suspend fun getGroups() : List<FoodGroup>

    @Query("SELECT * FROM FoodAndGroupNames " +
            "WHERE (:groupId = 0 OR FoodGroupID = :groupId)" +
            "AND (:nom IS NULL OR FoodDescriptionF LIKE '%' || :nom || '%') " +
            "ORDER BY instr(FoodDescriptionF ,:nom) ASC, FoodGroupID ASC")
    suspend fun getFoodsFr(groupId: Long, nom: String) : List<FoodAndGroupNames>

    @Query("SELECT * FROM FoodAndGroupNames " +
            "WHERE (:groupId = 0 OR FoodGroupID = :groupId)" +
            "AND (:nom IS NULL OR FoodDescription LIKE '%' || :nom || '%') " +
            "ORDER BY instr(FoodDescription ,:nom) ASC, FoodGroupID ASC")
    suspend fun getFoodsEn(groupId: Long, nom: String) : List<FoodAndGroupNames>;

    @Query("SELECT * FROM Food " +
            "WHERE FoodID = :foodId")
    suspend fun getFood(foodId: Long) : Food;

    @Query("SELECT * FROM FoodNutrientDetails " +
            "WHERE FoodID = :foodId ")
    suspend fun getFoodNutrients(foodId: Long) : List<FoodNutrientDetails>;

    @Query("SELECT * FROM DiscardFood " +
            "WHERE FoodID = :foodId")
    suspend fun getDiscardFood(foodId: Long) : DiscardFood;

    @Query("SELECT * FROM ConversionDetails " +
            "WHERE FoodID = :foodId " +
            "ORDER BY ConvFactorDateOfEntry DESC " +
            "LIMIT 1")
    suspend fun getConverterFactor(foodId: Long) : ConversionDetails;
}