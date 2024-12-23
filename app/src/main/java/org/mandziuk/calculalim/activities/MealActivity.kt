package org.mandziuk.calculalim.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.databinding.ActivityMealBinding

class MealActivity : DrawerEnabledActivity() {

    private lateinit var binding: ActivityMealBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityMealBinding.inflate(layoutInflater)
     setContentView(binding.root)

        initializeDrawer(binding.drawer, binding.navigation);

    }
}