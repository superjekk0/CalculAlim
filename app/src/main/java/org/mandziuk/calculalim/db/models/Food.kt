package org.mandziuk.calculalim.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import org.mandziuk.calculalim.db.Converter
import java.util.Date

@Entity(foreignKeys = [ForeignKey(
    entity = FoodGroup::class,
    childColumns = ["FoodGroupID"],
    parentColumns = ["FoodGroupID"],
    onDelete = ForeignKey.SET_NULL
)], indices = [
    Index(value = ["FoodGroupID"], orders = [Index.Order.ASC], name = "IX_Food_FoodGroupID"),
    Index(value = ["FoodDescription"], orders = [Index.Order.ASC], name = "IX_Food_Description"),
    Index(value = ["FoodDescriptionF"], orders = [Index.Order.ASC], name = "IX_Food_DescriptionF")
])
// TODO: Faire un remplacement de la suppression d'un enregistrement de la table Food par un champ de suppression logique
data class Food(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "FoodID") val id : Long,
    @ColumnInfo(name = "FoodCode") val code : Long,
    @ColumnInfo(name = "FoodGroupID") val groupId : Long,
    @ColumnInfo(name = "FoodSourceID") val sourceId : Long?,
    @ColumnInfo(name = "FoodDescription") val description : String,
    @ColumnInfo(name = "FoodDescriptionF") val descriptionFr: String,
    @ColumnInfo(name = "FoodDateOfEntry") val entryDate : String?,
    @ColumnInfo(name = "FoodDateOfPublication") val publicationDate : String?,
    @ColumnInfo(name = "CountryCode") val countryCode : String?,
    @ColumnInfo(name = "ScientificName") val scientificName : String?
);
