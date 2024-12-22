package org.mandziuk.calculalim.db.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView("SELECT CF.FoodID, CF.MeasureID, CF.ConversionFactorValue, CF.ConvFactorDateOfEntry, " +
        "M.MeasureDescription, M.MeasureDescriptionF " +
        "FROM ConversionFactor CF " +
        "INNER JOIN MeasureName M " +
        "ON CF.MeasureID = M.MeasureID")
data class ConversionDetails(
    @ColumnInfo(name = "FoodID") val foodId: Long,
    @ColumnInfo(name = "MeasureID") val measureId: Long,
    @ColumnInfo(name = "ConversionFactorValue") val conversionFactor: Float,
    @ColumnInfo(name = "ConvFactorDateOfEntry") val dateOfEntry: Long,
    @ColumnInfo(name = "MeasureDescription") val measureName: String,
    @ColumnInfo(name = "MeasureDescriptionF") val measureNameFr: String
)
