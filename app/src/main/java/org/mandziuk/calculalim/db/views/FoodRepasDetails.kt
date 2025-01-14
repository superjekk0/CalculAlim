package org.mandziuk.calculalim.db.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView("SELECT FR.FoodID, FR.RepasID, FR.Quantity, F.FoodDescription, F.FoodDescriptionF " +
        "FROM FoodRepas FR " +
        "INNER JOIN Food F " +
        "ON FR.FoodID = F.FoodID")
data class FoodRepasDetails(
    @ColumnInfo(name = "FoodID") val foodId : Long,
    @ColumnInfo(name = "RepasID") val mealId : Long,
    @ColumnInfo(name = "Quantity") val quantity : Int,
    @ColumnInfo(name = "FoodDescription") val foodName : String,
    @ColumnInfo(name = "FoodDescriptionF") val foodNameFr : String
)