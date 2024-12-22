package org.mandziuk.calculalim.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.mandziuk.calculalim.db.models.ConversionFactor
import org.mandziuk.calculalim.db.models.Discard
import org.mandziuk.calculalim.db.models.DiscardFood
import org.mandziuk.calculalim.db.models.Food
import org.mandziuk.calculalim.db.models.FoodGroup
import org.mandziuk.calculalim.db.models.MeasureName
import org.mandziuk.calculalim.db.models.Nutrient
import org.mandziuk.calculalim.db.models.NutrientAmount
import org.mandziuk.calculalim.db.views.ConversionDetails
import org.mandziuk.calculalim.db.views.FoodAndGroupNames
import org.mandziuk.calculalim.db.views.FoodNutrientDetails

@Database(entities = [FoodGroup::class, Nutrient::class, Food::class, NutrientAmount::class, Discard::class,
                     DiscardFood::class, MeasureName::class, ConversionFactor::class],
    views = [FoodAndGroupNames::class, FoodNutrientDetails::class, ConversionDetails::class],
    version = 9,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9)
                     ],
    exportSchema = true)
@TypeConverters(Converter::class)
abstract class FoodDb : RoomDatabase() {
    abstract fun getFoodDao() : FoodDao;
}

lateinit var foodDao: FoodDao;

fun getFoodDao(context: Context) : FoodDao{
    if (! ::foodDao.isInitialized){
        foodDao = Room.databaseBuilder(context,
            FoodDb::class.java,
            "food.db")
            .createFromAsset("food.db")
            .build().getFoodDao();
    }
    return foodDao;
}