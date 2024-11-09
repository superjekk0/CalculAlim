package org.mandziuk.calculalim.db.dtos

class FoodDTO() {
    var foodName : String = "";
    var foodGroup : String = "";

    constructor(food : String, group : String) : this(){
        this.foodName = food;
        this.foodGroup = group;
    }
}