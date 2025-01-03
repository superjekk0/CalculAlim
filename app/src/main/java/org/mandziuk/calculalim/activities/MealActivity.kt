package org.mandziuk.calculalim.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.adapters.MealAdapter
import org.mandziuk.calculalim.databinding.ActivityMealBinding
import org.mandziuk.calculalim.db.dtos.mealDtoInstance
import org.mandziuk.calculalim.db.services.ProfileService

class MealActivity : DrawerEnabledActivity() {

    private lateinit var binding: ActivityMealBinding;
    private lateinit var adapter: MealAdapter;
    private lateinit var profileService: ProfileService;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMealBinding.inflate(layoutInflater);
        setContentView(binding.root);
        initializeDrawer(binding.drawer, binding.navigation);

        profileService = ProfileService(this);
        val repasId = intent.getLongExtra("repasId", -1L);
        adapter = MealAdapter(this, repasId);
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
                binding.newRecipe.isEnabled = mealDtoInstance.isNotEmpty();
                adapter.setItems(mealDtoInstance);
            }
        }

        binding.newMeal.setOnClickListener{
            lifecycle.coroutineScope.launch {
                nouveauRepas();
            }
        };
    }

    private suspend fun nouveauRepas(){
        profileService.addMeal();
        Toast.makeText(this, getString(R.string.repasAjoute), Toast.LENGTH_SHORT).show();
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent);
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged();
    }
}