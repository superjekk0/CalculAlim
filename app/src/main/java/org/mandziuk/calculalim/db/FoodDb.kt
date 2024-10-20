package org.mandziuk.calculalim.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.mandziuk.calculalim.db.models.FoodGroup

@Database(entities = [FoodGroup::class], version = 1, exportSchema = false)
abstract class FoodDb : RoomDatabase() {
    abstract fun getFoodDao() : FoodDao;
}