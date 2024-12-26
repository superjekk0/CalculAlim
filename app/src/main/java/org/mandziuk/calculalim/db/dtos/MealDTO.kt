package org.mandziuk.calculalim.db.dtos

class MealDTO {

    data class FoodMeal(
        public val foodDTO: FoodDTO,
        public var weight: Int,
    ){

    };

    private val foodMeals = ArrayList<FoodMeal>();

    val size: Int
        get() {
            return this.foodMeals.size;
        };

    operator fun get(index: Int): FoodMeal {
        return this.foodMeals[index];
    }
}