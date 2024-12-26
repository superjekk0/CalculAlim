package org.mandziuk.calculalim.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.adapters.NutrientAdapter
import org.mandziuk.calculalim.databinding.ActivityFoodBinding
import org.mandziuk.calculalim.db.dtos.FoodDetailDTO
import org.mandziuk.calculalim.db.dtos.mealDtoInstance
import org.mandziuk.calculalim.db.services.FoodService

class FoodActivity : DrawerEnabledActivity() {
    private lateinit var binding: ActivityFoodBinding;
    private lateinit var foodService: FoodService;

    private lateinit var foodDetailDTO : FoodDetailDTO;
    private val nutrientAdapter: NutrientAdapter = NutrientAdapter(this);
    private var foodWeight: Int = 0;
    private var mealWeight: Int = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater);
        setContentView(binding.root);

        foodService = FoodService(applicationContext);
        binding.nutrients.layoutManager = LinearLayoutManager(this);

        initializeDrawer(binding.drawer, binding.navigation);
        mealWeight = intent.getIntExtra("poids", -1);

        setUi();

        lifecycleScope.launch {
            val foodId = intent.getLongExtra("id", 0L);
            foodDetailDTO =
                if (mealWeight == -1)
                    foodService.getFoodDetails(foodId)
                else
                    foodService.getFoodDetails(foodId, mealWeight.toLong());
            updateData();
        }

        binding.foodWeight.doOnTextChanged { text, _, _, _ ->
            foodWeight = (text ?: "0").toString().toInt();
        }

        binding.addToMeal.setOnClickListener {
            mealDtoInstance.add(foodDetailDTO.food, foodWeight);
            Toast.makeText(this, getString(R.string.messageAlimentAjoute, foodDetailDTO.food.foodName), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private fun updateData(){
        binding.foodName.text = foodDetailDTO.food.foodName;
        binding.weight.text = getString(R.string.nutriments, foodDetailDTO.weight);
        binding.foodPortion.text = foodDetailDTO.portionName;
        binding.nutrients.adapter = nutrientAdapter;
        nutrientAdapter.setList(foodDetailDTO.nutrients);
    }

    private fun setUi(){
        if (mealWeight != -1){
            binding.portionLayout.visibility = View.GONE;
            binding.setWeightLayout.visibility = View.GONE;
            binding.addToMeal.setText(R.string.supprimerAliment);
        } else{
            binding.portionLayout.visibility = View.VISIBLE;
            binding.setWeightLayout.visibility = View.VISIBLE;
            binding.addToMeal.setText(R.string.ajoutRepas);
        }
    }
}