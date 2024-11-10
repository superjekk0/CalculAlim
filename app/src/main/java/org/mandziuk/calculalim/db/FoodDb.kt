package org.mandziuk.calculalim.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.mandziuk.calculalim.db.models.Food
import org.mandziuk.calculalim.db.models.FoodGroup
import org.mandziuk.calculalim.db.models.Nutrient
import org.mandziuk.calculalim.db.models.NutrientAmount
import org.mandziuk.calculalim.db.views.FoodAndGroupNames

@Database(entities = [FoodGroup::class, Nutrient::class, Food::class, NutrientAmount::class],
    views = [FoodAndGroupNames::class],
    version = 5,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5)
                     ],
    exportSchema = true)
@TypeConverters(Converter::class)
abstract class FoodDb : RoomDatabase() {
    abstract fun getFoodDao() : FoodDao;
}