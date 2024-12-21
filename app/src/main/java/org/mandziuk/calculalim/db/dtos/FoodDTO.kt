package org.mandziuk.calculalim.db.dtos

import androidx.room.DatabaseView

class FoodDTO() {
    var foodName : String = "";
    var foodGroup : String = "";
    var foodId : Long = 0L;

    // TODO : Changer le type de la variable pour permettre d'ajouter la masse de référence
    constructor(food : String, group : String, foodId : Long) : this(){
        this.foodName = food;
        this.foodGroup = group;
        this.foodId = foodId;
    }
}