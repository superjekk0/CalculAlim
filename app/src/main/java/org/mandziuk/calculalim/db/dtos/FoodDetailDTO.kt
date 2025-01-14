package org.mandziuk.calculalim.db.dtos

import org.mandziuk.calculalim.db.models.Food
import org.mandziuk.calculalim.db.views.ConversionDetails
import org.mandziuk.calculalim.db.views.FoodNutrientDetails
import kotlin.math.pow
import kotlin.math.roundToLong

class FoodDetailDTO() {
    lateinit var food : FoodDTO;
    lateinit var nutrients : List<FoodNutrientDetails>;
    var weight: Long = 100;
    var portionName: String = "";
    var deleted: Boolean = false;

    constructor(food: Food, nutrients : List<FoodNutrientDetails>, locale : String) : this() {
        if (locale.contains("Fr", true)){
            this.food = FoodDTO(food.descriptionFr, food.groupId.toString(), food.id);
        } else{
            this.food = FoodDTO(food.description, food.groupId.toString(), food.id);
        }
        this.nutrients = nutrients;
        deleted = food.deleted;
    }

    fun multiplyByFactor(conversionFactor: ConversionDetails, locale: String){
        weight = (weight * conversionFactor.conversionFactor).roundToLong();
        portionName =
            if (locale.contains("fr", true))
                conversionFactor.measureNameFr
            else
                conversionFactor.measureName;
        nutrients.forEach{
            it.value *= conversionFactor.conversionFactor;
            val precision = 10F.pow(it.precision.toInt());
            it.value = (it.value * precision).toInt() / precision;
        };
    }

    fun multiplyByWeight(weight: Long){
        this.weight = weight;
        nutrients.forEach{
            it.value *= weight / 100F;
            val precision = 10F.pow(it.precision.toInt());
            it.value = (it.value * precision).toInt() / precision;
        };
    }
}