package org.mandziuk.calculalim.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "FoodNutrientAmount", primaryKeys = ["FoodID", "NutrientID"], foreignKeys = [
    ForeignKey(Food::class, childColumns = ["FoodID"], parentColumns = ["FoodID"], onDelete = ForeignKey.CASCADE),
    ForeignKey(Nutrient::class, childColumns = ["NutrientID"], parentColumns = ["NutrientID"], onDelete = ForeignKey.CASCADE)
], indices = [
    Index(value = ["FoodID"], orders = [Index.Order.ASC], name = "IX_FoodNutrientAmount_FoodID"),
    Index(value = ["NutrientID"], orders = [Index.Order.ASC], name = "IX_FoodNutrientAmount_NutrientID")
])
data class NutrientAmount(
    @ColumnInfo(name = "FoodID") val foodId : Long,
    @ColumnInfo(name = "NutrientID") val nutrientId : Long,
    @ColumnInfo(name = "NutrientValue") val value : Float,
    @ColumnInfo(name = "StandardError") val errorMargin : Float?,
    @ColumnInfo(name = "NumberOfObservations") val observationNumber : Long?,
    @ColumnInfo(name = "NutrientSourceID") val nutrientSourceId : Long,
    @ColumnInfo(name = "NutrientDateOfEntry") val entryDate : String?
);
