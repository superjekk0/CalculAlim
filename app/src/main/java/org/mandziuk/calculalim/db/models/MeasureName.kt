package org.mandziuk.calculalim.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MeasureName")
data class MeasureName(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "MeasureID") val measureId: Long,
    @ColumnInfo(name = "MeasureDescription") val measureName: String,
    @ColumnInfo(name = "MeasureDescriptionF") val measureNameFr: String
);