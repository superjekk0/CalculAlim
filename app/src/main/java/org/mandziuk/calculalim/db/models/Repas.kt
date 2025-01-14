package org.mandziuk.calculalim.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Repas", foreignKeys = [
    ForeignKey(
        entity = Profil::class,
        parentColumns = ["ProfilID"],
        childColumns = ["ProfilID"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )
], indices = [
    Index(value = ["ProfilID"], name = "IX_Repas_ProfilID", orders = [Index.Order.ASC]),
    Index(value = ["RepasDate"], name = "IX_Repas_RepasDate", orders = [Index.Order.DESC])
])
data class Repas(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "RepasID") var id: Long,
    @ColumnInfo(name = "ProfilID") val profilId: Long,
    @ColumnInfo(name = "RepasDate", typeAffinity = ColumnInfo.INTEGER) val date: Date = Date()
)
