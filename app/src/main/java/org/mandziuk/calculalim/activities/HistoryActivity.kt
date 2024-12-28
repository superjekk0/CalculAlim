package org.mandziuk.calculalim.activities

import android.os.Bundle
import org.mandziuk.calculalim.databinding.ActivityHistoryBinding

class HistoryActivity : DrawerEnabledActivity() {
    private lateinit var binding: ActivityHistoryBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(layoutInflater);
        setContentView(binding.root);

        initializeDrawer(binding.drawer, binding.navigation);
    }
}