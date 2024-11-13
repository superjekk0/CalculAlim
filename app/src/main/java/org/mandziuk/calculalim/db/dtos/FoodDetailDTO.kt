package org.mandziuk.calculalim.db.dtos

import org.mandziuk.calculalim.db.models.Food
import org.mandziuk.calculalim.db.models.Nutrient
import org.mandziuk.calculalim.db.views.FoodNutrientDetails

class FoodDetailDTO() {
    lateinit var food : FoodDTO
    lateinit var nutrients : List<FoodNutrientDetails>;

    constructor(food: Food, nutrients : List<FoodNutrientDetails>, locale : String) : this() {
        if (locale.contains("Fr", true)){
            this.food = FoodDTO(food.descriptionFr, food.groupId.toString(), food.id);
        } else{
            this.food = FoodDTO(food.description, food.groupId.toString(), food.id);
        }
        this.nutrients = nutrients;
    }
}