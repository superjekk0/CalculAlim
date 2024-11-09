package org.mandziuk.calculalim

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.FileUtils
import android.util.Log
import androidx.appcompat.app.AlertDialog
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

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding;
    private lateinit var choixGroupes : List<FoodGroupDTO>;
    private lateinit var service : FoodService;
    private var indexGroup : Long = 0L;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root);

        val db = Room.databaseBuilder(applicationContext,
            FoodDb::class.java,
            "food.db")
            .createFromAsset("food.db")
            .build();

        service = FoodService(db.getFoodDao());
        lifecycleScope.launch{
        choixGroupes = service.getFoodGroups(applicationContext);
        Log.i("EXEMPLE", choixGroupes.toString());
        };
        binding.choix.setOnClickListener {
            val builder = AlertDialog.Builder(this);

            builder.setTitle(R.string.typeNourriture).setSingleChoiceItems(choixGroupes.map { cg -> cg.groupName }.toTypedArray(), indexGroup.toInt()) { dialog, index ->
                run {
                    binding.choix.text = choixGroupes[index].groupName;
                    indexGroup = index.toLong();
                    dialog.dismiss();

                }
            }

            val dialog = builder.create();
            dialog.show();
        };

        binding.list.layoutManager = LinearLayoutManager(this);
        binding.list.adapter = FoodAdapter();
    }
}