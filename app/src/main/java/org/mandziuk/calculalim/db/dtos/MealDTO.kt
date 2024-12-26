package org.mandziuk.calculalim.db.dtos

val mealDtoInstance = MealDTO();

class MealDTO {

    class FoodMealDTO(val foodId: Long, val foodName: String, var weight: Int){

        constructor(foodDTO: FoodDTO, weight: Int) : this(foodDTO.foodId, foodDTO.foodName, weight);

    };

    private val foodMeals = ArrayList<FoodMealDTO>();

    val size: Int
        get() {
            return this.foodMeals.size;
        };

    operator fun get(index: Int): FoodMealDTO {
        return this.foodMeals[index];
    }

    fun add(foodMealDTO: FoodDTO, weight: Int){
        val existingFoodMeal = this.foodMeals.find { it.foodId == foodMealDTO.foodId };
        if (existingFoodMeal != null){
            existingFoodMeal.weight += weight;
            return;
        }

        this.foodMeals.add(FoodMealDTO(foodMealDTO, weight));
    }
}