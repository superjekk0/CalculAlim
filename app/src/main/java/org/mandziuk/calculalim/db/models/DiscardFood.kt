package org.mandziuk.calculalim.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.Date

@Entity(tableName = "DiscardFood", primaryKeys = ["FoodID", "DiscardID"], foreignKeys = [
    ForeignKey(
    entity = Discard::class,
    parentColumns = ["DiscardID"],
    childColumns = ["DiscardID"],
    onDelete = ForeignKey.NO_ACTION),
    ForeignKey(
    entity = Food::class,
    parentColumns = ["FoodID"],
    childColumns = ["FoodID"],
    onDelete = ForeignKey.CASCADE)
])
data class DiscardFood(
    @ColumnInfo(name = "FoodID") val foodId: Long,
    @ColumnInfo(name = "DiscardID") val discardId: Long,
    @ColumnInfo(name = "DiscardPercentage", defaultValue = "0") val discardPercentage: Long,
    @ColumnInfo(name = "DiscardDateOfEntry") val discardDateOfEntry: Date?
)
