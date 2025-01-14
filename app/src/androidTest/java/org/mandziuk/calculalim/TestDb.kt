package org.mandziuk.calculalim

import androidx.room.Database
import androidx.room.TypeConverters
import org.mandziuk.calculalim.db.Converter
import org.mandziuk.calculalim.db.FoodDb
import org.mandziuk.calculalim.db.models.ConversionFactor
import org.mandziuk.calculalim.db.models.Discard
import org.mandziuk.calculalim.db.models.DiscardFood
import org.mandziuk.calculalim.db.models.Food
import org.mandziuk.calculalim.db.models.FoodGroup
import org.mandziuk.calculalim.db.models.FoodRepas
import org.mandziuk.calculalim.db.models.MeasureName
import org.mandziuk.calculalim.db.models.Nutrient
import org.mandziuk.calculalim.db.models.NutrientAmount
import org.mandziuk.calculalim.db.models.Profil
import org.mandziuk.calculalim.db.models.Repas
import org.mandziuk.calculalim.db.views.ConversionDetails
import org.mandziuk.calculalim.db.views.FoodAndGroupNames
import org.mandziuk.calculalim.db.views.FoodNutrientDetails
import org.mandziuk.calculalim.db.views.FoodRepasDetails
import org.mandziuk.calculalim.db.views.NutrientNameEnability

@Database(entities = [FoodGroup::class, Nutrient::class, Food::class, NutrientAmount::class, Discard::class,
    DiscardFood::class, MeasureName::class, ConversionFactor::class, Profil::class, Repas::class,
    FoodRepas::class], version = 1, views = [FoodAndGroupNames::class, FoodNutrientDetails::class, ConversionDetails::class, NutrientNameEnability::class,
    FoodRepasDetails::class])
@TypeConverters(Converter::class)
abstract class TestDb : FoodDb() {
    abstract fun getTestDao(): TestDao;
}