package org.mandziuk.calculalim.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.Instant

@Entity(tableName = "ConversionFactor", primaryKeys = ["FoodID", "MeasureID"], foreignKeys = [
    ForeignKey(
        entity = Food::class,
        parentColumns = ["FoodID"],
        childColumns = ["FoodID"],
        onDelete = ForeignKey.CASCADE)
])
data class ConversionFactor(
    @ColumnInfo(name = "FoodID") val foodId: Long,
    @ColumnInfo(name = "MeasureID") val measureId: Long,
    @ColumnInfo(name = "ConversionFactorValue") val conversionFactor: Float,
    @ColumnInfo(name = "ConvFactorDateOfEntry") val dateOfEntry: Instant?
);
