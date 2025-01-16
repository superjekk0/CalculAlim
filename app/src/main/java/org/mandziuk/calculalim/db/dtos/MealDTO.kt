package org.mandziuk.calculalim.db.dtos

val mealDtoInstance = MealDTO();

private const val maximumPoidsAliment = 999999

class MealDTO : ArrayList<MealDTO.FoodMealDTO>(){
    class FoodMealDTO(val foodId: Long, val foodName: String, var weight: Int){
        var displayed: Boolean = true;
        var index: Int = -1;
        constructor(foodDTO: FoodDTO, weight: Int) : this(foodDTO.foodId, foodDTO.foodName, weight);

    };

    fun add(foodMealDTO: FoodDTO, weight: Int){
        val existingFoodMeal = this.find { it.foodId == foodMealDTO.foodId };
        if (existingFoodMeal != null){
            existingFoodMeal.weight = maximumPoidsAliment.coerceAtMost(existingFoodMeal.weight + weight);
            return;
        }
        val newFoodMealDTO = FoodMealDTO(foodMealDTO, weight);
        newFoodMealDTO.index = this.size;
        this.add(newFoodMealDTO);
    }

    fun remove(foodId: Long){
        val index = this.indexOfFirst { it.foodId == foodId };
        if (index != -1){
            this.removeAt(index);
        }
    }
}