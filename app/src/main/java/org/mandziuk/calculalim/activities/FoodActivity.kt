package org.mandziuk.calculalim.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.adapters.NutrientAdapter
import org.mandziuk.calculalim.databinding.ActivityFoodBinding
import org.mandziuk.calculalim.db.dtos.FoodDetailDTO
import org.mandziuk.calculalim.db.services.FoodService

class FoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodBinding;
    private lateinit var foodService: FoodService;

    private lateinit var foodDTO : FoodDetailDTO;
    private val nutrientAdapter: NutrientAdapter = NutrientAdapter(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater);
        setContentView(binding.root);

        foodService = FoodService(applicationContext);

        binding.nutrients.layoutManager = LinearLayoutManager(this);
        lifecycleScope.launch {
            val foodId = intent.getLongExtra("id", 0L);
            foodDTO = foodService.getFoodDetails(foodId);
            updateData();
        }
    }

    private fun updateData(){
        binding.foodName.text = foodDTO.food.foodName;
        // TODO : Changer le foodName par la valeur de la quantité (g)
        binding.weight.text = getString(R.string.nutriments, foodDTO.weight);
        binding.foodPortion.text = foodDTO.portionName;
        binding.nutrients.adapter = nutrientAdapter;
        nutrientAdapter.setList(foodDTO.nutrients);
        Log.i("EXEMPLE", "Fin de l'updateData");
    }
}