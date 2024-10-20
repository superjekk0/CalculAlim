package org.mandziuk.calculalim

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.FileUtils
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import org.mandziuk.calculalim.databinding.ActivityMainBinding
import org.mandziuk.calculalim.db.FoodDb
import java.io.File
import java.io.FileDescriptor
import java.io.InputStream
import java.nio.file.Files
import kotlin.io.path.Path

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding;
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
        Log.i("EXEMPLE", foodDao.getGroups().toString());

        binding.list.layoutManager = LinearLayoutManager(this);
        binding.list.adapter = FoodAdapter();
    }
}