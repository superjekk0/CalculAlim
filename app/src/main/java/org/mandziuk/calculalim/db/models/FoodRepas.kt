package org.mandziuk.calculalim.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "FoodRepas", primaryKeys = ["FoodID", "RepasID"], foreignKeys = [
    ForeignKey(
        entity = Food::class,
        parentColumns = ["FoodID"],
        childColumns = ["FoodID"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = Repas::class,
        parentColumns = ["RepasID"],
        childColumns = ["RepasID"],
        onDelete = ForeignKey.CASCADE
    )
])
data class FoodRepas(
    @ColumnInfo(name = "FoodID") val foodId: Long,
    @ColumnInfo(name = "RepasID") val repasId: Long,
    @ColumnInfo(name = "Quantity") val quantity: Int
)
