package org.mandziuk.calculalim.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.adapters.FoodAdapter
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.databinding.ActivityMainBinding
import org.mandziuk.calculalim.db.dtos.FoodGroupDTO
import org.mandziuk.calculalim.db.services.FoodService

var indexGroup : Long = 0L;

class MainActivity : DrawerEnabledActivity() {

    private lateinit var binding : ActivityMainBinding;
    private lateinit var choixGroupes : List<FoodGroupDTO>;
    private lateinit var service : FoodService;

    private var name: String = "";
    private lateinit var adapter: FoodAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root);

        service = FoodService(applicationContext);
        initializeDrawer(binding.drawer, binding.navigation, R.string.symbole_gramme, R.string.symbole_gramme);

        abToggle.syncState();
        lifecycleScope.launch{
            choixGroupes = service.getFoodGroups(applicationContext);
            binding.choix.text = choixGroupes[indexGroup.toInt()].groupName;
            Log.i("EXEMPLE", choixGroupes.toString());
        };
        binding.choix.setOnClickListener {
            val builder = AlertDialog.Builder(this);

            builder.setTitle(R.string.typeNourriture).setSingleChoiceItems(choixGroupes.map { cg -> cg.groupName }.toTypedArray(), indexGroup.toInt()) { dialog, index ->
                run {
                    binding.choix.text = choixGroupes[index].groupName;
                    indexGroup = index.toLong();
                    maJListe();
                    dialog.dismiss();
                }
            }

            val dialog = builder.create();
            dialog.show();
        };

        adapter = FoodAdapter(this);
        binding.list.layoutManager = LinearLayoutManager(this);
        binding.list.adapter = adapter;

        binding.recherche.doOnTextChanged{
            text, _, _, _ ->
            name = text.toString();
            maJListe();
        }

        maJListe();
    }

    private fun maJListe(){
        lifecycleScope.launch {
            adapter.setList(service.getFood(name, indexGroup));
        }
    }
}