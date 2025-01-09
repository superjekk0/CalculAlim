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
import org.mandziuk.calculalim.db.dtos.NewFoodDTO
import org.mandziuk.calculalim.db.dtos.NutrientEnabledDTO
import org.mandziuk.calculalim.db.getFoodDao
import org.mandziuk.calculalim.db.models.ConversionFactor
import org.mandziuk.calculalim.db.models.Food
import org.mandziuk.calculalim.db.models.MeasureName
import org.mandziuk.calculalim.db.models.NutrientAmount
import org.mandziuk.calculalim.db.views.FoodNutrientDetails
import org.mandziuk.calculalim.db.views.NutrientNameEnability
import java.time.Instant
import java.util.Locale

private const val pourCentGrammes = 100.0F;

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

    suspend fun createFood(newFoodDTO: NewFoodDTO, aliments: MealDTO): Long{
        // TODO : Faire les étapes de création d'un aliment
        return withContext(Dispatchers.IO){
            foodDao.getFoodGroup(newFoodDTO.foodGroupId) ?: return@withContext -1L;
            if (newFoodDTO.mealName.isNullOrBlank()){
                return@withContext -1L;
            }
            val poidsTotal: Int = poidsRepas(newFoodDTO, aliments)
            val measureId = recupererMeasureId(newFoodDTO, poidsTotal)
            val foodId = ajoutAliment(newFoodDTO);
            creerPortionRepas(measureId, foodId, poidsTotal);

            creationNutriments(aliments, foodId, poidsTotal);
            return@withContext foodId;
        }
    }

    private suspend fun creationNutriments(aliments: MealDTO, foodId: Long, poidsTotal: Int) {
        val nutrientAmount = aliments.map { a -> getAllNutrients(a.foodId, a.weight) };
        // TODO : Continuer les calculs nutritionnels
        val nutriments = nutrientAmount
            .map { na -> na.nutrients }.flatten().groupBy { n -> n.nutrientId }
            .map { (nutrientId, j) ->
                NutrientAmount(
                    foodId,
                    nutrientId = nutrientId,
                    value = j.sumOf { it.value.toDouble() }.toFloat() / (poidsTotal / pourCentGrammes),
                    errorMargin = null,
                    observationNumber = null,
                    nutrientSourceId = 0L,
                    entryDate = Instant.now(),
                )
            };
        foodDao.insertNutrientsAmounts(nutriments);
    }

    private suspend fun ajoutAliment(newFoodDTO: NewFoodDTO): Long {
        val food = Food(
            id = 0L,
            description = newFoodDTO.mealName!!,
            descriptionFr = newFoodDTO.mealName!!,
            groupId = newFoodDTO.foodGroupId,
            code = 0L,
            sourceId = null,
            entryDate = Instant.now(),
            scientificName = null,
            publicationDate = Instant.now(),
            countryCode = null
        );

        return foodDao.createFood(food);
    }

    private suspend fun recupererMeasureId(newFoodDTO: NewFoodDTO, poidsTotal: Int): Long {
        val nomPortion =
            if (newFoodDTO.portionName == null && newFoodDTO.portionWeight != null) {
                "${newFoodDTO.portionWeight}g"
            } else if (newFoodDTO.portionName != null) {
                newFoodDTO.portionName!!;
            } else {
                "${poidsTotal}g";
            }
        val portion = foodDao.getMeasureName(nomPortion);
        return portion?.measureId ?: foodDao.insertMeasureName(MeasureName(0L, nomPortion, nomPortion))
    }

    private suspend fun creerPortionRepas(measureId: Long, foodId: Long, poidsTotal: Int){
        val facteurConversion = poidsTotal / pourCentGrammes;
        foodDao.insertConversionFactor(ConversionFactor(
            foodId = foodId,
            measureId = measureId,
            conversionFactor = facteurConversion,
            dateOfEntry = Instant.now()
        ));
    }

    private fun poidsRepas(newFoodDTO: NewFoodDTO, aliments: MealDTO) : Int {
        return if (newFoodDTO.mealWeight == null) {
            aliments.sumOf { a -> a.weight };
        } else {
            newFoodDTO.mealWeight!!;
        }
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