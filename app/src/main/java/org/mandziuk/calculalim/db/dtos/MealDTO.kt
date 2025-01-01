package org.mandziuk.calculalim.db.dtos

val mealDtoInstance = MealDTO();

class MealDTO : ArrayList<MealDTO.FoodMealDTO>(){
    class FoodMealDTO(val foodId: Long, val foodName: String, var weight: Int){
        constructor(foodDTO: FoodDTO, weight: Int) : this(foodDTO.foodId, foodDTO.foodName, weight);

    };

    fun add(foodMealDTO: FoodDTO, weight: Int){
        val existingFoodMeal = this.find { it.foodId == foodMealDTO.foodId };
        if (existingFoodMeal != null){
            existingFoodMeal.weight += weight;
            return;
        }
        this.add(FoodMealDTO(foodMealDTO, weight));
    }

    fun remove(foodId: Long){
        val index = this.indexOfFirst { it.foodId == foodId };
        if (index != -1){
            this.removeAt(index);
        }
    }
}