package org.mandziuk.calculalim

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mandziuk.calculalim.db.FoodDb
import org.mandziuk.calculalim.db.dtos.MealDTO
import org.mandziuk.calculalim.db.dtos.NewFoodDTO
import org.mandziuk.calculalim.db.dtos.mealDtoInstance
import org.mandziuk.calculalim.db.foodDb
import org.mandziuk.calculalim.db.services.FoodService

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
            .build();
        testDao = bd.getTestDao();
        foodDb = bd;
        foodService = FoodService(context);
        remplirRepas();
    }

    private fun remplirRepas(){
        val mealDTO = mealDtoInstance;
        mealDTO.add(MealDTO.FoodMealDTO(3942L, "Tarte aux pommes", 110));
        mealDTO.add(MealDTO.FoodMealDTO(4156L, "Crème glacée", 62));
    }

    @After
    fun cleanUp(){
        foodDb?.close();
        context.deleteDatabase("food.db");
        mealDtoInstance.clear();
    }

    @Test
    fun creationAlimentTotalementInvalide(){
        runBlocking {
            val newFoodDTO = NewFoodDTO();
            val nombreAliments = testDao.foodCount();
            val nombreMesures = testDao.measureCount();
            val nombreConversion = testDao.conversionFactorCount();
            val nombreNutrimentsAvecNourriture = testDao.foodNutrientAmountCount();

            foodService.createFood(newFoodDTO, mealDtoInstance);

            Assert.assertEquals(nombreAliments, testDao.foodCount());
            Assert.assertEquals(nombreMesures, testDao.measureCount());
            Assert.assertEquals(nombreConversion, testDao.conversionFactorCount());
            Assert.assertEquals(nombreNutrimentsAvecNourriture, testDao.foodNutrientAmountCount());
        }
    }

    @Test
    fun creationAlimentGroupeManquant(){
        runBlocking {
            val newFoodDTO = NewFoodDTO();
            newFoodDTO.mealName = "Tarte aux pommes maison à la mode";
            val nombreAliments = testDao.foodCount();
            val nombreMesures = testDao.measureCount();
            val nombreConversion = testDao.conversionFactorCount();
            val nombreNutrimentsAvecNourriture = testDao.foodNutrientAmountCount();

            foodService.createFood(newFoodDTO, mealDtoInstance);

            Assert.assertEquals(nombreAliments, testDao.foodCount());
            Assert.assertEquals(nombreMesures, testDao.measureCount());
            Assert.assertEquals(nombreConversion, testDao.conversionFactorCount());
            Assert.assertEquals(nombreNutrimentsAvecNourriture, testDao.foodNutrientAmountCount());
        }
    }

    @Test
    fun creationAlimentNomManquant(){
        runBlocking {
            val newFoodDTO = NewFoodDTO();
            newFoodDTO.foodGroupId = 18L;
            val nombreAliments = testDao.foodCount();
            val nombreMesures = testDao.measureCount();
            val nombreConversion = testDao.conversionFactorCount();
            val nombreNutrimentsAvecNourriture = testDao.foodNutrientAmountCount();

            foodService.createFood(newFoodDTO, mealDtoInstance);

            Assert.assertEquals(nombreAliments, testDao.foodCount());
            Assert.assertEquals(nombreMesures, testDao.measureCount());
            Assert.assertEquals(nombreConversion, testDao.conversionFactorCount());
            Assert.assertEquals(nombreNutrimentsAvecNourriture, testDao.foodNutrientAmountCount());
        }
    }
}