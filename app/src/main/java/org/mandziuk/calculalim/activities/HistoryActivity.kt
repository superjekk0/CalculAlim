package org.mandziuk.calculalim.activities

import android.os.Bundle
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.adapters.HistoryDateAdapter
import org.mandziuk.calculalim.databinding.ActivityHistoryBinding
import org.mandziuk.calculalim.db.services.ProfileService

class HistoryActivity : DrawerEnabledActivity() {
    private lateinit var binding: ActivityHistoryBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(layoutInflater);
        setContentView(binding.root);

        initializeDrawer(binding.drawer, binding.navigation);

        lifecycle.coroutineScope.launch {
            val context = this@HistoryActivity;
            val profileService = ProfileService(context);
            val repas = profileService.getMeals();

            binding.recycler.adapter = HistoryDateAdapter(context, repas);
            binding.recycler.layoutManager = LinearLayoutManager(context);
        }

    }
}