package org.mandziuk.calculalim.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.adapters.NutrientAdapter
import org.mandziuk.calculalim.databinding.ActivityFoodBinding
import org.mandziuk.calculalim.db.dtos.FoodDetailDTO
import org.mandziuk.calculalim.db.services.FoodService

class FoodActivity : AppCompatActivity() {
    lateinit var binding: ActivityFoodBinding;
    lateinit var foodService: FoodService;

    lateinit var foodDTO : FoodDetailDTO;
    val nutrientAdapter: NutrientAdapter = NutrientAdapter();

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
        binding.foodPortion.text = "Portion";
        binding.nutrients.adapter = nutrientAdapter;
        nutrientAdapter.setList(foodDTO.nutrients);
        Log.i("EXEMPLE", "Fin de l'updateData");
    }
}