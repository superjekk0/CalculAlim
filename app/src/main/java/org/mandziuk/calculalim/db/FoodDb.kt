package org.mandziuk.calculalim.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.mandziuk.calculalim.db.daos.FoodDao
import org.mandziuk.calculalim.db.daos.ProfilDao;
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
import org.mandziuk.calculalim.db.views.NutrientNameEnability

@Database(entities = [FoodGroup::class, Nutrient::class, Food::class, NutrientAmount::class, Discard::class,
                     DiscardFood::class, MeasureName::class, ConversionFactor::class, Profil::class, Repas::class,
                     FoodRepas::class],
    views = [FoodAndGroupNames::class, FoodNutrientDetails::class, ConversionDetails::class, NutrientNameEnability::class],
    version = 13,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11),
        AutoMigration(from = 11, to = 12),
        AutoMigration(from = 12, to = 13)
                     ],
    exportSchema = true)
@TypeConverters(Converter::class)
abstract class FoodDb : RoomDatabase() {
    abstract fun getFoodDao() : FoodDao;

    abstract fun getProfilDao() : ProfilDao;
}

var foodDao: FoodDao? = null;
var profilDao: ProfilDao? = null;
var foodDb: FoodDb? = null;

fun getFoodDao(context: Context) : FoodDao {
    if (foodDb == null) {
        foodDb = Room.databaseBuilder(context,
            FoodDb::class.java,
            "food.db")
            .createFromAsset("food.db")
            .build();
    }

    if (foodDao == null) {
        foodDao = foodDb?.getFoodDao();
    }
    return foodDao!!;
}

fun getProfilDao(context: Context) : ProfilDao {
    if (foodDb == null) {
        foodDb = Room.databaseBuilder(context,
            FoodDb::class.java,
            "food.db")
            .createFromAsset("food.db")
            .build();
    }

    if (profilDao == null) {
        profilDao = foodDb?.getProfilDao();
    }
    return profilDao!!;
}