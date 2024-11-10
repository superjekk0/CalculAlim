package org.mandziuk.calculalim

import android.app.Activity
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.FileUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mandziuk.calculalim.databinding.ActivityMainBinding
import org.mandziuk.calculalim.db.FoodDb
import org.mandziuk.calculalim.db.dtos.FoodGroupDTO
import org.mandziuk.calculalim.db.models.FoodGroup
import org.mandziuk.calculalim.db.services.FoodService
import java.io.File
import java.io.FileDescriptor
import java.io.InputStream
import java.nio.file.Files
import java.util.ArrayList
import java.util.Locale
import kotlin.io.path.Path

var indexGroup : Long = 0L;

class MainActivity : AppCompatActivity() {

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

        adapter = FoodAdapter();
        binding.list.layoutManager = LinearLayoutManager(this);
        binding.list.adapter = adapter;

        binding.recherche.doOnTextChanged{
            text, _, _, _ ->
            name = text.toString();
            maJListe();
        }

        maJListe();
    }

    fun maJListe(){
        lifecycleScope.launch {
            adapter.setList(service.getFood(name, indexGroup));
        }
    }
}