package org.mandziuk.calculalim.db.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView("SELECT F.FoodDescription, F.FoodDescriptionF, FG.FoodGroupName, " +
        "FG.FoodGroupNameF, FG.FoodGroupID, F.FoodID, F.Deleted FROM Food F " +
        "INNER JOIN FoodGroup FG " +
        "ON FG.FoodGroupID = F.FoodGroupID ")
data class FoodAndGroupNames(
    @ColumnInfo(name = "FoodDescription") val foodDescription: String,
    @ColumnInfo(name = "FoodDescriptionF") val foodDescriptionFr: String,
    @ColumnInfo(name = "FoodGroupName") val foodGroupName: String,
    @ColumnInfo(name = "FoodGroupNameF") val foodGroupNameFr: String,
    @ColumnInfo(name = "FoodGroupID") val foodGroupID: Long,
    @ColumnInfo(name = "FoodID") val foodId: Long,
    @ColumnInfo(name = "Deleted") val deleted: Boolean
)
