package org.mandziuk.calculalim.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Discard")
data class Discard(
    @PrimaryKey @ColumnInfo(name = "DiscardID") val discardId: Long,
    @ColumnInfo(name = "RefuseDescription") val discardDescription: String,
    @ColumnInfo(name = "RefuseDescriptionF") val discardDescriptionFr: String
)
