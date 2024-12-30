package org.mandziuk.calculalim.db.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import org.mandziuk.calculalim.db.models.DiscardFood
import org.mandziuk.calculalim.db.models.Food
import org.mandziuk.calculalim.db.models.FoodGroup
import org.mandziuk.calculalim.db.views.ConversionDetails
import org.mandziuk.calculalim.db.views.FoodAndGroupNames
import org.mandziuk.calculalim.db.views.FoodNutrientDetails
import org.mandziuk.calculalim.db.views.NutrientNameEnability

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

    @Query("SELECT * FROM NutrientNameEnability")
    suspend fun getNutrientNames() : List<NutrientNameEnability>;

    @Query("UPDATE Nutrient " +
            "SET Displayed = 1 " +
            "WHERE NutrientID IN (:nutrientIds)")
    suspend fun updateNutrientDisplayed(nutrientIds: List<Long>);

    @Query("UPDATE Nutrient " +
            "SET Displayed = 0 " +
            "WHERE NutrientID IN (:nutrientIds)")
    suspend fun updateNutrientHidden(nutrientIds: List<Long>);
}