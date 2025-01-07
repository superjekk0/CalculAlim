package org.mandziuk.calculalim.db.services

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.db.daos.FoodDao
import org.mandziuk.calculalim.db.dtos.FoodDTO
import org.mandziuk.calculalim.db.dtos.FoodDetailDTO
import org.mandziuk.calculalim.db.dtos.FoodGroupDTO
import org.mandziuk.calculalim.db.dtos.MealDTO
import org.mandziuk.calculalim.db.dtos.NutrientEnabledDTO
import org.mandziuk.calculalim.db.getFoodDao
import org.mandziuk.calculalim.db.models.Food
import org.mandziuk.calculalim.db.views.FoodNutrientDetails
import org.mandziuk.calculalim.db.views.NutrientNameEnability
import org.mandziuk.calculalim.dialogs.AddMealDialog
import java.time.Instant
import java.util.Date
import java.util.Locale

class FoodService(private val applicationContext: Context) {
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

    suspend fun getFoodGroups(): List<FoodGroupDTO>{
        return withContext(Dispatchers.IO){
            val groupes = foodDao.getGroups();
            val locale = Locale.getDefault().displayLanguage;
            val liste = if (locale.contains("fr", true)){
                groupes.map { g -> FoodGroupDTO(g.id, g.groupNameFr) }.toMutableList();
            } else {
                groupes.map { g -> FoodGroupDTO(g.id, g.groupName) }.toMutableList();
            };
            liste.add(0, FoodGroupDTO(0, applicationContext.getString(R.string.tousGroupes)));
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

    suspend fun getNutrientNames() : List<NutrientEnabledDTO>{
        return withContext(Dispatchers.IO){
            val nutrients: List<NutrientNameEnability> = foodDao.getNutrientNames();
            return@withContext if (Locale.getDefault().displayLanguage.contains("fr", true)){
                nutrients.map { n -> NutrientEnabledDTO(n.nutrientNameFr, n.displayed, n.nutrientId) };
            } else {
                nutrients.map { n -> NutrientEnabledDTO(n.nutrientName, n.displayed, n.nutrientId) };
            };
        }
    }

    suspend fun changeNutrientDisplays(nutrientIds: List<Long>){
        withContext(Dispatchers.IO){
            val nutrients = foodDao.getNutrientNames();
            foodDao.updateNutrientDisplayed(nutrientIds);
            foodDao.updateNutrientHidden(nutrients
                .filter { ! nutrientIds.contains(it.nutrientId) }
                .map { n -> n.nutrientId });
        }
    }

    suspend fun foodNameTaken(foodName: String) : TakenStatus{
        val exists = foodDao.foodExists(foodName);
        if (!exists){
            return TakenStatus.NOT_TAKEN;
        }
        return if (foodDao.foodExistsFr(foodName)){
            TakenStatus.TAKEN_FR;
        } else {
            TakenStatus.TAKEN_EN;
        }
    }

    suspend fun createFood(addMealDialog: AddMealDialog, aliments: MealDTO){
        // TODO : Faire les étapes de création d'un aliment
        val poidsTotal : Int = if (addMealDialog.mealWeight == null){
            aliments.sumOf { a -> a.weight };
        } else {
            addMealDialog.mealWeight!!;
        }

        val group = foodDao.getFoodGroup(addMealDialog.foodGroupId);
        if (group == null){
            return;
        }

        val food = Food(
            id = 0L,
            description = addMealDialog.mealName!!,
            descriptionFr = addMealDialog.mealName!!,
            groupId = addMealDialog.foodGroupId,
            code = 0L,
            sourceId = null,
            entryDate = null,
            scientificName = null,
            publicationDate = null,
            countryCode = null
        );

        val nutrientAmount = aliments.map { a -> getAllNutrients(a.foodId, a.weight) };
        // TODO : Continuer les calculs nutritionnels
        val nutriments = nutrientAmount.map { na -> na.nutrients }.flatten().groupBy { n -> n.nutrientId }
            .map { (i, j) -> j }


        val foodId = foodDao.createFood(food);
    }

    private suspend fun getAllNutrients(foodId: Long, weight: Int) : FoodDetailDTO {
        val locale: String = Locale.getDefault().displayLanguage;
        val food : Food = foodDao.getFood(foodId);
        val foodNutrients : List<FoodNutrientDetails> = foodDao.getFoodNutrients(foodId);
        val foodDTO = FoodDetailDTO(food, foodNutrients, locale);
        foodDTO.multiplyByWeight(weight.toLong());
        return foodDTO;
    }
}

enum class TakenStatus{
    NOT_TAKEN,
    TAKEN_FR,
    TAKEN_EN
}