package org.mandziuk.calculalim.db.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView("SELECT N.NutrientID, NA.FoodID, N.NutrientName, N.NutrientNameF, N.NutrientUnit, NA.NutrientValue, N.NutrientDecimals " +
        "FROM Nutrient N " +
        "INNER JOIN FoodNutrientAmount NA " +
        "ON NA.NutrientID = N.NutrientID " +
        "WHERE N.Displayed = 1")
data class FoodNutrientDetails(
    @ColumnInfo(name = "NutrientID") val nutrientId : Long,
    @ColumnInfo(name = "FoodID") val foodId : Long,
    @ColumnInfo(name = "NutrientName") val nutrientName : String,
    @ColumnInfo(name = "NutrientNameF") val nutrientNameFr : String,
    @ColumnInfo(name = "NutrientUnit") val unit : String,
    @ColumnInfo(name = "NutrientValue") var value : Float,
    @ColumnInfo(name = "NutrientDecimals") val precision : Long
)
