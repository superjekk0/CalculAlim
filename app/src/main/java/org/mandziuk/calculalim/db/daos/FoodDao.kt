package org.mandziuk.calculalim.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.mandziuk.calculalim.db.models.ConversionFactor
import org.mandziuk.calculalim.db.models.DiscardFood
import org.mandziuk.calculalim.db.models.Food
import org.mandziuk.calculalim.db.models.FoodGroup
import org.mandziuk.calculalim.db.models.MeasureName
import org.mandziuk.calculalim.db.models.Nutrient
import org.mandziuk.calculalim.db.models.NutrientAmount
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
            "WHERE Deleted = 0 AND (:groupId = 0 OR FoodGroupID = :groupId) " +
            "AND (:nom IS NULL OR FoodDescriptionF LIKE '%' || :nom || '%') " +
            "ORDER BY instr(FoodDescriptionF ,:nom) ASC, FoodGroupID ASC")
    suspend fun getFoodsFr(groupId: Long, nom: String) : List<FoodAndGroupNames>

    @Query("SELECT * FROM FoodAndGroupNames " +
            "WHERE Deleted = 0 AND (:groupId = 0 OR FoodGroupID = :groupId)" +
            "AND (:nom IS NULL OR FoodDescription LIKE '%' || :nom || '%') " +
            "ORDER BY instr(FoodDescription ,:nom) ASC, FoodGroupID ASC")
    suspend fun getFoodsEn(groupId: Long, nom: String) : List<FoodAndGroupNames>;

    @Query("SELECT * FROM FoodAndGroupNames " +
            "WHERE Deleted = 1 AND (:groupId = 0 OR FoodGroupID = :groupId) " +
            "AND (:nom IS NULL OR FoodDescriptionF LIKE '%' || :nom || '%') " +
            "ORDER BY instr(FoodDescriptionF ,:nom) ASC, FoodGroupID ASC")
    suspend fun getDeletedFoodsFr(groupId: Long, nom: String) : List<FoodAndGroupNames>;

    @Query("SELECT * FROM FoodAndGroupNames " +
            "WHERE Deleted = 1 AND (:groupId = 0 OR FoodGroupID = :groupId)" +
            "AND (:nom IS NULL OR FoodDescription LIKE '%' || :nom || '%') " +
            "ORDER BY instr(FoodDescription ,:nom) ASC, FoodGroupID ASC")
    suspend fun getDeletedFoodsEn(groupId: Long, nom: String) : List<FoodAndGroupNames>;

    @Query("SELECT * FROM Food " +
            "WHERE FoodID = :foodId")
    suspend fun getFood(foodId: Long) : Food;

    @Query("SELECT * FROM FoodNutrientDetails " +
            "WHERE FoodID = :foodId AND Displayed = 1")
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

    @Query("SELECT COUNT(FoodID) FROM Food WHERE Deleted = 0 AND UPPER(FoodDescription) = UPPER(:foodName) OR UPPER(FoodDescriptionF) = UPPER(:foodName)")
    suspend fun foodExists(foodName: String) : Boolean;

    @Query("SELECT COUNT(FoodID) FROM Food WHERE Deleted = 0 AND UPPER(FoodDescriptionF) = UPPER(:foodName)")
    suspend fun foodExistsFr(foodName: String) : Boolean;

    @Insert(onConflict = OnConflictStrategy.ABORT, entity = Food::class)
    suspend fun createFood(food: Food) : Long

    @Query("SELECT * FROM FoodGroup WHERE FoodGroupID = :foodId")
    suspend fun getFoodGroup(foodId: Long) : FoodGroup?;

    @Query("SELECT * FROM FoodNutrientAmount WHERE FoodID IN (:foodIds)")
    suspend fun getFoodAmounts(foodIds: List<Long>) : List<NutrientAmount>;

    @Query("SELECT * FROM FoodNutrientDetails " +
            "WHERE FoodID = :foodId")
    suspend fun getFoodNutrientsList(foodId: Long) : List<FoodNutrientDetails>;

    @Query("SELECT * FROM MeasureName " +
            "WHERE MeasureDescriptionF = :measureName OR MeasureDescription = :measureName LIMIT 1")
    suspend fun getMeasureName(measureName: String) : MeasureName?;

    @Insert(onConflict = OnConflictStrategy.ABORT, entity = MeasureName::class)
    suspend fun insertMeasureName(measureName: MeasureName) : Long;

    @Insert(onConflict = OnConflictStrategy.ABORT, entity = ConversionFactor::class)
    suspend fun insertConversionFactor(conversionFactor: ConversionFactor) : Long;

    @Insert(onConflict = OnConflictStrategy.ABORT, entity = NutrientAmount::class)
    suspend fun insertNutrientsAmounts(nutrientAmount: List<NutrientAmount>);

    @Query("UPDATE Food SET Deleted = 1 WHERE FoodID = :foodId")
    suspend fun deleteFood(foodId: Long);

    @Query("UPDATE Food SET Deleted = 0 WHERE FoodID = :foodId")
    suspend fun restoreFood(foodId: Long);

    @Query("SELECT * FROM Nutrient")
    suspend fun getNutrients() : List<Nutrient>;
}