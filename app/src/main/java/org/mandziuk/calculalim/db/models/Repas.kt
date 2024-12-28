package org.mandziuk.calculalim.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.Date

@Entity(tableName = "Repas", foreignKeys = [
    ForeignKey(
        entity = Profil::class,
        parentColumns = ["ProfilID"],
        childColumns = ["ProfilID"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )
])
data class Repas(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "RepasID") val id: Long,
    @ColumnInfo(name = "ProfilID") val profilId: Long,
    @ColumnInfo(name = "RepasDate", typeAffinity = ColumnInfo.INTEGER) val date: Date
)
