package org.mandziuk.calculalim.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FoodGroup")
data class FoodGroup(
    @ColumnInfo(name = "FoodGroupID") @PrimaryKey(autoGenerate = true) val id : Long,
    @ColumnInfo(name = "FoodGroupName") val groupName : String,
    @ColumnInfo(name = "FoodGroupNameF") val groupNameFr : String
);