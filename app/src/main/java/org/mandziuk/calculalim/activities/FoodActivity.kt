package org.mandziuk.calculalim.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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
import org.mandziuk.calculalim.dialogs.DeleteDialog
import org.mandziuk.calculalim.dialogs.RestoreDialog

class FoodActivity : DrawerEnabledActivity() {
    private lateinit var binding: ActivityFoodBinding;
    private lateinit var foodService: FoodService;

    private lateinit var foodDetailDTO : FoodDetailDTO;
    private val nutrientAdapter: NutrientAdapter = NutrientAdapter(this);
    private var foodWeight: Int = 0;
    private var mealWeight: Int = -1;
    private var afficherMenuSuppression: Boolean = false;

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.i("FoodActivity", "CREATION MENU");
        if (afficherMenuSuppression)
            menuInflater.inflate(R.menu.food, menu);
        else
            menuInflater.inflate(R.menu.food_deleted, menu);

        return super.onCreateOptionsMenu(menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.supprimer ->{
                val dialog = DeleteDialog(this);

                dialog.setOnDismissListener {
                    if (! dialog.annule){
                        lifecycleScope.launch {
                            foodService.deleteFood(foodDetailDTO.food.foodId);
                            Toast.makeText(this@FoodActivity, getString(R.string.messageAlimentSupprime, foodDetailDTO.food.foodName), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }

                dialog.show();
                true;
            }
            R.id.restaurer ->{
                val dialog = RestoreDialog(this);

                dialog.setOnDismissListener {
                    if (! dialog.annule){
                        lifecycleScope.launch {
                            foodService.restoreFood(foodDetailDTO.food.foodId);
                            Toast.makeText(this@FoodActivity,
                                getString(R.string.alimentRestaure), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }

                dialog.show();
                true;
            }
            else ->{
                super.onOptionsItemSelected(item);
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        Log.i("FoodActivity", "CREATION");
        binding = ActivityFoodBinding.inflate(layoutInflater);
        setContentView(binding.root);
        Log.i("FoodActivity", "CREATION 2");

        foodService = FoodService(applicationContext);
        binding.nutrients.layoutManager = LinearLayoutManager(this);

        initializeDrawer(binding.drawer, binding.navigation);
        mealWeight = intent.getIntExtra("poids", -1);
        val repasId = intent.getLongExtra("repasId", -1L);
//        val position = intent.getIntExtra("position", -1);

        setUi(repasId);

        lifecycleScope.launch {
            val foodId = intent.getLongExtra("id", 0L);
            foodDetailDTO =
                if (mealWeight == -1)
                    foodService.getFoodDetails(foodId)
                else
                    foodService.getFoodDetails(foodId, mealWeight.toLong());
            afficherMenuSuppression = ! foodDetailDTO.deleted;
            invalidateOptionsMenu();
            updateData();
        }

        binding.foodWeight.doOnTextChanged { text, _, _, _ ->
            foodWeight = when (text?.length) {
                null -> 0;
                0 -> 0;
                else ->
                    text.toString().toInt();
            }
        }

        binding.addToMeal.setOnClickListener {
            if (mealWeight != -1){
                val resultIntent = Intent();
                resultIntent.putExtra("deletedFoodId", foodDetailDTO.food.foodId);
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(this, getString(R.string.messageAlimentSupprime, foodDetailDTO.food.foodName), Toast.LENGTH_LONG).show();
            } else{
                mealDtoInstance.add(foodDetailDTO.food, foodWeight);
                Toast.makeText(this, getString(R.string.messageAlimentAjoute, foodDetailDTO.food.foodName), Toast.LENGTH_LONG).show();
            }
            finish();
        }

        Log.i("FoodActivity", "FIN CREATION");
    }

    private fun updateData(){
        binding.foodName.text = foodDetailDTO.food.foodName;
        binding.weight.text = getString(R.string.nutriments, foodDetailDTO.weight);
        binding.foodPortion.text = foodDetailDTO.portionName;
        binding.nutrients.adapter = nutrientAdapter;
        nutrientAdapter.setList(foodDetailDTO.nutrients);
    }

    private fun setUi(repasId: Long){
        if (mealWeight != -1){
            binding.portionLayout.visibility = View.GONE;
            binding.setWeightLayout.visibility = View.GONE;
            binding.addToMeal.setText(R.string.supprimerAlimentRepas);
            binding.addToMeal.visibility = if (repasId == -1L) View.VISIBLE else View.GONE;
        } else{
            binding.portionLayout.visibility = View.VISIBLE;
            binding.setWeightLayout.visibility = View.VISIBLE;
            binding.addToMeal.setText(R.string.ajoutRepas);
        }
    }
}