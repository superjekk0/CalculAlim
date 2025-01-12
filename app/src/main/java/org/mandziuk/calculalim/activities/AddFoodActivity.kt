package org.mandziuk.calculalim.activities

import java.util.Locale
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.adapters.NewNutrientAdapter
import org.mandziuk.calculalim.databinding.ActivityAddFoodBinding
import org.mandziuk.calculalim.db.dtos.FoodGroupDTO
import org.mandziuk.calculalim.db.models.Nutrient
import org.mandziuk.calculalim.db.services.FoodService
import org.mandziuk.calculalim.db.services.TakenStatus

class AddFoodActivity : DrawerEnabledActivity() {
    private lateinit var binding: ActivityAddFoodBinding;
    private lateinit var foodService: FoodService;
    private lateinit var nutriments: ArrayList<Nutrient>;
    private lateinit var groupes: List<FoodGroupDTO>;
    private lateinit var adapter: NewNutrientAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddFoodBinding.inflate(layoutInflater)
        setContentView(binding.root);
        initializeDrawer(binding.drawer, binding.navigation);

        foodService = FoodService(this);

        lifecycle.coroutineScope.launch {
            nutriments = foodService.getNutrients();
            groupes = foodService.getFoodGroups();
        }

        binding.nomAliment.doOnTextChanged { text, _, _, _ ->
            when (text?.length){
                null, 0 ->{
                    binding.nomAliment.error = getString(R.string.erreurNomAliment);
                }
                else ->{
                    verificationNomPris(text.toString());
                }
            }
        }

        binding.ajouterNutriment.setOnClickListener {
            val groupDialog = AlertDialog.Builder(this);
            groupDialog.setTitle(getString(R.string.choisirNutriment));

            groupDialog.setSingleChoiceItems(nutrientNames().toTypedArray(), 0) { dialog, index ->
                adapter.add(nutriments[index]);
                nutriments.removeAt(index);
                dialog.dismiss();
            }

            groupDialog.show();
        }

        binding.groupeAliment.setOnClickListener {
            val groupDialog = AlertDialog.Builder(this);
            groupDialog.setTitle(getString(R.string.choisirNutriment));

            groupDialog.setSingleChoiceItems(groupes.map { n -> n.groupName }.toTypedArray(), -1){dialog, index ->
                binding.groupeAliment.text = groupes[index].groupName;
                binding.groupeAliment.error = null;
                dialog.dismiss();
            }

            groupDialog.show();
        }

        binding.poidsAliment.doOnTextChanged { text, _, _, _ ->
            when (text?.length){
                null, 0 ->{
                    binding.nomAliment.error = getString(R.string.erreurPoidsRequis);
                }
                else ->{
                    binding.nomAliment.error = null;
                }
            }
        }

        binding.recycler.layoutManager = LinearLayoutManager(this);
        adapter = NewNutrientAdapter(this);
        binding.recycler.adapter = adapter;
    }

    private fun nutrientNames() : List<String> {
        return if (Locale.getDefault().displayLanguage.contains("fr", true))
            nutriments.map { n -> n.nameFr }
        else nutriments.map { n -> n.name };
    }

    private fun verificationNomPris(nom: String) {
        lifecycleScope.launch {
            val pris = foodService.foodNameTaken(nom);
            when (pris){
                TakenStatus.NOT_TAKEN -> binding.nomAliment.error = null;
                TakenStatus.TAKEN_FR -> binding.nomAliment.error = getString(R.string.erreurNomAlimentPrisFr);
                TakenStatus.TAKEN_EN -> binding.nomAliment.error = getString(R.string.erreurNomAlimentPrisEn);
            }
        }
    }
}