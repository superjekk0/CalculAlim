package org.mandziuk.calculalim.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.databinding.ActivityParametersBinding
import org.mandziuk.calculalim.fragments.SettingsFragment

class ParametersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParametersBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        binding = ActivityParametersBinding.inflate(layoutInflater);
        setContentView(binding.root);
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment, SettingsFragment())
            .commit();
    }
}