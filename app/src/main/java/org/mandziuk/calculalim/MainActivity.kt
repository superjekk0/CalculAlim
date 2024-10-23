package org.mandziuk.calculalim

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.FileUtils
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import org.mandziuk.calculalim.databinding.ActivityMainBinding
import org.mandziuk.calculalim.db.FoodDb
import org.mandziuk.calculalim.db.models.FoodGroup
import java.io.File
import java.io.FileDescriptor
import java.io.InputStream
import java.nio.file.Files
import java.util.ArrayList
import java.util.Locale
import kotlin.io.path.Path

class MainActivity : Activity() {

    private lateinit var binding : ActivityMainBinding;
    private lateinit var choixGroupes : List<FoodGroup>;
    private var indexGroup : Long = 0L;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root);

        val db = Room.databaseBuilder(applicationContext,
            FoodDb::class.java,
            "food.db")
            .createFromAsset("food.db")
            .allowMainThreadQueries()
            .build();

        val foodDao = db.getFoodDao();
        choixGroupes = foodDao.getGroups();
        Log.i("EXEMPLE", foodDao.getGroups().toString());

        binding.choix.setOnClickListener {
            val choix = choixGroupes.map { g ->
                if (Locale.getDefault().displayLanguage.contains("fr", true))
                    g.groupNameFr
                else
                    g.groupName }.toMutableList();
            choix.add(0, getString(R.string.tousGroupes));
            val builder = AlertDialog.Builder(this);

            builder.setTitle("Choix").setSingleChoiceItems(choix.toTypedArray(), 0) { dialog, index ->
                run {
                    binding.choix.text = choix[index];
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