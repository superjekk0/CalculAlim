package org.mandziuk.calculalim.db.services

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.db.FoodDao
import org.mandziuk.calculalim.db.FoodDb
import org.mandziuk.calculalim.db.dtos.FoodDTO
import org.mandziuk.calculalim.db.dtos.FoodGroupDTO
import java.util.Locale

class FoodService(applicationContext: Context) {
    private val foodDao: FoodDao = Room.databaseBuilder(applicationContext,
        FoodDb::class.java,
        "food.db")
        .createFromAsset("food.db")
        .build().getFoodDao();


    suspend fun getFood(foodName: String, groupId: Long) : List<FoodDTO>{
        return withContext(Dispatchers.IO){
            val locale = Locale.getDefault().displayLanguage;
            val result = if (locale.contains("fr", true)){
                foodDao.getFoodsFr(groupId, foodName);
            } else {
                foodDao.getFoodsEn(groupId, foodName);
            };

            if (locale.contains("fr", true)){
                result.map { f -> FoodDTO(f.foodDescriptionFr, f.foodGroupNameFr)};
            } else {
                result.map { f -> FoodDTO(f.foodDescription, f.foodGroupName)};
            };
        }
    }

    suspend fun getFoodGroups(context: Context) : List<FoodGroupDTO>{
        return withContext(Dispatchers.IO){
            val groups = foodDao.getGroups();
            val locale = Locale.getDefault().displayLanguage;
            val liste = if (locale.contains("fr", true)){
                groups.map { g -> FoodGroupDTO(g.id, g.groupNameFr) }.toMutableList();
            } else {
                groups.map { g -> FoodGroupDTO(g.id, g.groupName) }.toMutableList();
            };
            liste.add(0, FoodGroupDTO(0, context.getString(R.string.tousGroupes)));
            return@withContext liste;
        }
    }
}