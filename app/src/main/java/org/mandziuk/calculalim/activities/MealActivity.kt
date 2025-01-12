package org.mandziuk.calculalim.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.adapters.MealAdapter
import org.mandziuk.calculalim.databinding.ActivityMealBinding
import org.mandziuk.calculalim.db.dtos.mealDtoInstance
import org.mandziuk.calculalim.db.services.FoodService
import org.mandziuk.calculalim.db.services.ProfileService
import org.mandziuk.calculalim.dialogs.AddMealDialog

class MealActivity : DrawerEnabledActivity() {

    private lateinit var binding: ActivityMealBinding;
    private lateinit var adapter: MealAdapter;
    private lateinit var profileService: ProfileService;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMealBinding.inflate(layoutInflater);
        setContentView(binding.root);
        initializeDrawer(binding.drawer, binding.navigation);

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data;
                val foodId = data?.getLongExtra("deletedFoodId", -1);
                if (foodId != null && foodId != -1L){
                    adapter.removeFood(foodId);
                    mealDtoInstance.remove(foodId);
                }
            }
        }

        profileService = ProfileService(this);
        val repasId = intent.getLongExtra("repasId", -1L);
        adapter = MealAdapter(this, repasId, resultLauncher);
        binding.foods.adapter = adapter;
        binding.foods.layoutManager = LinearLayoutManager(this);


        lifecycle.coroutineScope.launch {
            if (repasId != -1L){
                val mealDTO = profileService.getFoodRepas(repasId);
                binding.newMeal.visibility = View.GONE;
                binding.newRecipe.visibility = View.GONE;
                adapter.setItems(mealDTO);
            }
            else {
                binding.newMeal.isEnabled = mealDtoInstance.isNotEmpty();
                binding.newRecipe.isEnabled = mealDtoInstance.size > 1;
                adapter.setItems(mealDtoInstance);
            }
        }

        binding.newMeal.setOnClickListener{
            lifecycle.coroutineScope.launch {
                nouveauRepas();
            }
        };

        binding.newRecipe.setOnClickListener {
            lifecycle.coroutineScope.launch {
                dialogueNouvelleRecette();
            }
        }
    }

    private suspend fun nouveauRepas(){
        profileService.addMeal();
        Toast.makeText(this, getString(R.string.repasAjoute), Toast.LENGTH_SHORT).show();
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent);
    }

    private suspend fun dialogueNouvelleRecette(){
        val foodService = FoodService(this);
        val groupes = foodService.getFoodGroups();
        val vraisGroupes = groupes.drop(1);
        val dialog = AddMealDialog(this@MealActivity, vraisGroupes);

        dialog.setOnDismissListener {
            if (dialog.annule){
                return@setOnDismissListener;
            }
            lifecycle.coroutineScope.launch {
                foodService.createFood(dialog.newFoodDTO, mealDtoInstance);
                Toast.makeText(this@MealActivity, R.string.alimentComposeAjouteListe, Toast.LENGTH_LONG).show();
                val intent = Intent(this@MealActivity, MainActivity::class.java);
                startActivity(intent);
            }
        }

        dialog.show();
    }
}