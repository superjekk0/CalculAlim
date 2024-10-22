package org.mandziuk.calculalim.db.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "FoodGroup")
data class FoodGroup(
    @PrimaryKey(autoGenerate = true) val FoodGroupID : Long,
    @ColumnInfo(name = "FoodGroupName") val groupName : String,
    @ColumnInfo(name = "FoodGroupNameF") val groupNameFr : String
);