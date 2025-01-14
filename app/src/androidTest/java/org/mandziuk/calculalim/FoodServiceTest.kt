package org.mandziuk.calculalim

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mandziuk.calculalim.db.dtos.MealDTO
import org.mandziuk.calculalim.db.dtos.NewFoodDTO
import org.mandziuk.calculalim.db.dtos.mealDtoInstance
import org.mandziuk.calculalim.db.foodDao
import org.mandziuk.calculalim.db.foodDb
import org.mandziuk.calculalim.db.profilDao
import org.mandziuk.calculalim.db.services.FoodService

private const val NOM_ALIMENT = "Tarte aux pommes maison à la mode"
private const val AUCUN_ALIMENT = -1L
private const val PORTION = "1 pointe avec 100 mL de crème glacée"

fun Boolean.toInt() = if (this) 1 else 0;

@RunWith(AndroidJUnit4::class)
class FoodServiceTest {
    private lateinit var foodService: FoodService;
    private lateinit var context: Context;
    private lateinit var testDao: TestDao;

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext();
        val bd = Room.databaseBuilder(context, TestDb::class.java, "food.db")
            .createFromAsset("food.db")
            .allowMainThreadQueries()
            .build();
        testDao = bd.getTestDao();
        foodDb = bd;
        foodService = FoodService(context);
        remplirRepas();
    }

    @After
    fun cleanUp(){
        context.deleteDatabase("food.db");
        mealDtoInstance.clear();
        foodDb = null;
        foodDao = null;
        profilDao = null;
    }

    private fun remplirRepas(){
        val mealDTO = mealDtoInstance;
        mealDTO.add(MealDTO.FoodMealDTO(3942L, "Tarte aux pommes", 110));
        mealDTO.add(MealDTO.FoodMealDTO(4156L, "Crème glacée", 62));
    }

    @Test
    fun creationAlimentEchecTotalementInvalide(){
        runBlocking {
            val newFoodDTO = NewFoodDTO();
            val nouvelAliment = insertionAliment(newFoodDTO, false)
            Assert.assertEquals(AUCUN_ALIMENT, nouvelAliment);
        }
    }

    @Test
    fun creationAlimentEchecGroupeManquant(){
        runBlocking {
            val newFoodDTO = NewFoodDTO();
            newFoodDTO.mealName = NOM_ALIMENT;
            val nouvelAliment = insertionAliment(newFoodDTO, false);
            Assert.assertEquals(AUCUN_ALIMENT, nouvelAliment);
        }
    }

    @Test
    fun creationAlimentEchecNomManquant(){
        runBlocking {
            val newFoodDTO = NewFoodDTO();
            newFoodDTO.foodGroupId = 18L;
            val nouvelAliment = insertionAliment(newFoodDTO, false);
            Assert.assertEquals(AUCUN_ALIMENT, nouvelAliment);
        }
    }

    private suspend fun insertionAliment(newFoodDTO: NewFoodDTO, alimentInsere: Boolean): Long {
        val nombreAliments = testDao.foodCount();
        val nombreMesures = testDao.measureCount();
        val nombreConversion = testDao.conversionFactorCount();
        val nombreNutrimentsAvecNourriture = testDao.foodNutrientAmountCount();
        val nombreNutrimentAffectes = testDao.nutrientCount(mealDtoInstance.map { it.foodId });

        return runBlocking {
            val nouvelAliment = foodService.createFood(newFoodDTO, mealDtoInstance);

            if (alimentInsere){
                Assert.assertEquals(nombreAliments + 1, testDao.foodCount());
                Assert.assertEquals(nombreMesures + 1, testDao.measureCount());
                Assert.assertEquals(nombreConversion + 1, testDao.conversionFactorCount());
                Assert.assertEquals(nombreNutrimentsAvecNourriture + nombreNutrimentAffectes, testDao.foodNutrientAmountCount());
            } else{
                Assert.assertEquals(nombreAliments, testDao.foodCount());
                Assert.assertEquals(nombreMesures, testDao.measureCount());
                Assert.assertEquals(nombreConversion, testDao.conversionFactorCount());
                Assert.assertEquals(nombreNutrimentsAvecNourriture, testDao.foodNutrientAmountCount());
            }

            return@runBlocking nouvelAliment;
        }
    }

    @Test
    fun creationAlimentReussiteInfosMinimum(){
        val newFoodDTO = NewFoodDTO();
        newFoodDTO.foodGroupId = 18L;
        newFoodDTO.mealName = NOM_ALIMENT;

        runBlocking {
            val nouvelAliment = insertionAliment(newFoodDTO, true);
            Assert.assertNotEquals(AUCUN_ALIMENT, nouvelAliment);
            val details = foodService.getFoodDetails(nouvelAliment);

            val total = mealDtoInstance.sumOf { it.weight };
            Assert.assertEquals(NOM_ALIMENT, details.food.foodName);
            Assert.assertEquals(total.toLong(), details.weight);
            Assert.assertEquals("${total}g", details.portionName);
        }
    }

    @Test
    fun creationAlimentReussitePoidsFinalDifferent(){
        runBlocking {
            val newFoodDTO = NewFoodDTO();
            newFoodDTO.foodGroupId = 18L;
            newFoodDTO.mealName = NOM_ALIMENT;
            newFoodDTO.mealWeight = 200;
            val nouvelAliment = insertionAliment(newFoodDTO, true);
            Assert.assertNotEquals(AUCUN_ALIMENT, nouvelAliment);

            val details = foodService.getFoodDetails(nouvelAliment);

            Assert.assertEquals(NOM_ALIMENT, details.food.foodName);
            Assert.assertEquals(200L, details.weight);
            Assert.assertEquals("200g", details.portionName);
        }
    }

    @Test
    fun creationAlimentReussitePoidsFinalImplicite(){
        runBlocking {
            val newFoodDTO = NewFoodDTO();
            newFoodDTO.foodGroupId = 18L;
            newFoodDTO.mealName = NOM_ALIMENT;
            newFoodDTO.portionName = PORTION;
            val nouvelAliment = insertionAliment(newFoodDTO, true);
            Assert.assertNotEquals(AUCUN_ALIMENT, nouvelAliment);

            val details = foodService.getFoodDetails(nouvelAliment);

            Assert.assertEquals(NOM_ALIMENT, details.food.foodName);
            Assert.assertEquals(mealDtoInstance.sumOf { it.weight }.toLong(), details.weight);
            Assert.assertEquals(PORTION, details.portionName);
        }
    }

    @Test
    fun creationAlimentReussitePortionComplete(){
        val newFoodDTO = NewFoodDTO();
        newFoodDTO.foodGroupId = 18L;
        newFoodDTO.mealName = NOM_ALIMENT;
        newFoodDTO.portionWeight = 200;

        runBlocking {
            val nouvelAliment = insertionAliment(newFoodDTO, true);
            Assert.assertNotEquals(AUCUN_ALIMENT, nouvelAliment);

            val details = foodService.getFoodDetails(nouvelAliment);

            Assert.assertEquals(NOM_ALIMENT, details.food.foodName);
            Assert.assertEquals(200L, details.weight);
            Assert.assertEquals("200g", details.portionName);
        }
    }

    @Test
    fun creationAlimentReussitePortionNomSpecifie(){
        val newFoodDTO = NewFoodDTO();
        newFoodDTO.foodGroupId = 18L;
        newFoodDTO.mealName = NOM_ALIMENT;
        newFoodDTO.portionName = PORTION;

        runBlocking {
            val nouvelAliment = insertionAliment(newFoodDTO, true);
            Assert.assertNotEquals(AUCUN_ALIMENT, nouvelAliment);

            val details = foodService.getFoodDetails(nouvelAliment);

            Assert.assertEquals(NOM_ALIMENT, details.food.foodName);
            Assert.assertEquals(mealDtoInstance.sumOf { it.weight }.toLong(), details.weight);
            Assert.assertEquals(PORTION, details.portionName);
        }
    }

    @Test
    fun creationAlimentReussitePortionNomImplicitePoidsSpecifie(){
        val newFoodDTO = NewFoodDTO();
        newFoodDTO.foodGroupId = 18L;
        newFoodDTO.mealName = NOM_ALIMENT;
        newFoodDTO.portionWeight = 200;

        runBlocking {
            val nouvelAliment = insertionAliment(newFoodDTO, true);
            Assert.assertNotEquals(AUCUN_ALIMENT, nouvelAliment);

            val details = foodService.getFoodDetails(nouvelAliment);

            Assert.assertEquals(NOM_ALIMENT, details.food.foodName);
            Assert.assertEquals(200L, details.weight);
            Assert.assertEquals("200g", details.portionName);
        }
    }
}