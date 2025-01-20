package org.mandziuk.calculalim.db.models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Profil")
data class Profil(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ProfilID") val id: Long,
    @ColumnInfo(name = "ProfilName") val name: String,
    @ColumnInfo(name = "ProfilPicture") val picture: Uri?
)
