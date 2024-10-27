package org.mandziuk.calculalim.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Nutrient(
    @ColumnInfo(name = "NutrientID") @PrimaryKey(autoGenerate = true) val id : Long,
    @ColumnInfo(name = "NutrientCode") val code : Long,
    @ColumnInfo(name = "NutrientSymbol") val symbol : String,
    @ColumnInfo(name = "NutrientUnit") val unit : String,
    @ColumnInfo(name = "NutrientName") val name : String,
    @ColumnInfo(name = "NutrientNameF") val nameFr : String,
    @ColumnInfo(name = "Tagname") val tagName : String?,
    @ColumnInfo(name = "NutrientDecimals") val decimals : Long
);