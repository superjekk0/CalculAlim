package org.mandziuk.calculalim.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.databinding.ActivityFoodBinding
import org.mandziuk.calculalim.db.dtos.FoodDetailDTO
import org.mandziuk.calculalim.db.services.FoodService

class FoodActivity : AppCompatActivity() {
    lateinit var binding: ActivityFoodBinding;
    lateinit var foodService: FoodService;

    lateinit var foodDTO : FoodDetailDTO;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater);
        setContentView(binding.root);

        foodService = FoodService(applicationContext);
        lifecycleScope.launch {
            val foodId = intent.getLongExtra("id", 0L);
            foodDTO = foodService.getFoodDetails(foodId);
            updateDatas();
        }
    }

    private fun updateDatas(){
        binding.foodName.text = foodDTO.food.foodName;
    }
}