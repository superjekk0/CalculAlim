package org.mandziuk.calculalim.db.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView("SELECT N.NutrientName, N.NutrientNameF, N.Displayed, N.NutrientID " +
        "FROM Nutrient N")
data class NutrientNameEnability(
    @ColumnInfo(name = "NutrientName") val nutrientName : String,
    @ColumnInfo(name = "NutrientNameF") val nutrientNameFr : String,
    @ColumnInfo(name = "Displayed") val displayed : Boolean,
    @ColumnInfo(name = "NutrientID") val nutrientId : Long
)
