package org.mandziuk.calculalim.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profil")
data class Profil(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ProfilID") val id: Long,
    @ColumnInfo(name = "ProfilName") val name: String,
)
