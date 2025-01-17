package org.mandziuk.calculalim

import androidx.room.Dao
import androidx.room.Query

@Dao
interface TestDao {
    @Query("SELECT COUNT(FoodID) FROM Food")
    suspend fun foodCount(): Long;

    @Query("SELECT COUNT(MeasureID) FROM MeasureName")
    suspend fun measureCount(): Long;

    @Query("SELECT COUNT(*) FROM ConversionFactor")
    suspend fun conversionFactorCount(): Long;

    @Query("SELECT COUNT(*) FROM FoodNutrientAmount")
    suspend fun foodNutrientAmountCount(): Long;

    @Query("SELECT COUNT(DISTINCT NutrientID) FROM FoodNutrientAmount WHERE FoodID IN (:foodIds)")
    suspend fun nutrientCount(foodIds: List<Long>) : Long;

    @Query("SELECT COUNT(*) FROM Food WHERE FoodDescriptionF = :nom")
    suspend fun nomPris(nom: String): Boolean;
}