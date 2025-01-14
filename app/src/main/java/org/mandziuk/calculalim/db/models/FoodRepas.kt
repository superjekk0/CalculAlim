package org.mandziuk.calculalim.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

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
], indices = [
    Index(name = "IX_FoodRepas_FoodID", value = ["FoodID"], orders = [Index.Order.ASC]),
    Index(name = "IX_FoodRepas_RepasID", value = ["RepasID"], orders = [Index.Order.ASC])
])
data class FoodRepas(
    @ColumnInfo(name = "FoodID") val foodId: Long,
    @ColumnInfo(name = "RepasID") val repasId: Long,
    @ColumnInfo(name = "Quantity") val quantity: Int
)
