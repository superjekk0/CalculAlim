package org.mandziuk.calculalim.db.services

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.db.daos.FoodDao
import org.mandziuk.calculalim.db.dtos.FoodDTO
import org.mandziuk.calculalim.db.dtos.FoodDetailDTO
import org.mandziuk.calculalim.db.dtos.FoodGroupDTO
import org.mandziuk.calculalim.db.getFoodDao
import org.mandziuk.calculalim.db.models.Food
import org.mandziuk.calculalim.db.views.FoodNutrientDetails
import java.util.Locale

class FoodService(applicationContext: Context) {
    private val foodDao: FoodDao = getFoodDao(applicationContext);

    suspend fun getFood(foodName: String, groupId: Long) : List<FoodDTO>{
        return withContext(Dispatchers.IO){
            val locale = Locale.getDefault().displayLanguage;
            val result = if (locale.contains("fr", true)){
                foodDao.getFoodsFr(groupId, foodName);
            } else {
                foodDao.getFoodsEn(groupId, foodName);
            };

            if (locale.contains("fr", true)){
                return@withContext result.map { f -> FoodDTO(f.foodDescriptionFr, f.foodGroupNameFr, f.foodId)};
            } else {
                return@withContext result.map { f -> FoodDTO(f.foodDescription, f.foodGroupName, f.foodId)};
            };
        };
    }

    suspend fun getFoodGroups(context: Context) : List<FoodGroupDTO>{
        return withContext(Dispatchers.IO){
            val groupes = foodDao.getGroups();
            val locale = Locale.getDefault().displayLanguage;
            val liste = if (locale.contains("fr", true)){
                groupes.map { g -> FoodGroupDTO(g.id, g.groupNameFr) }.toMutableList();
            } else {
                groupes.map { g -> FoodGroupDTO(g.id, g.groupName) }.toMutableList();
            };
            liste.add(0, FoodGroupDTO(0, context.getString(R.string.tousGroupes)));
            return@withContext liste;
        }
    }

    suspend fun getFoodDetails(foodId : Long) : FoodDetailDTO{
        return withContext(Dispatchers.IO){
            val locale: String = Locale.getDefault().displayLanguage;
            val food : Food = foodDao.getFood(foodId);
            val foodNutrients : List<FoodNutrientDetails> = foodDao.getFoodNutrients(foodId);
            val foodDTO = FoodDetailDTO(food, foodNutrients, locale);
            foodDTO.multiplyByFactor(foodDao.getConverterFactor(foodId), locale);
            return@withContext foodDTO;
        }
    }

    suspend fun getFoodDetails(foodId: Long, weight: Long) : FoodDetailDTO{
        return withContext(Dispatchers.IO){
            val locale: String = Locale.getDefault().displayLanguage;
            val food : Food = foodDao.getFood(foodId);
            val foodNutrients : List<FoodNutrientDetails> = foodDao.getFoodNutrients(foodId);
            val foodDTO = FoodDetailDTO(food, foodNutrients, locale);
            foodDTO.multiplyByWeight(weight);
            return@withContext foodDTO;
        }
    }
}