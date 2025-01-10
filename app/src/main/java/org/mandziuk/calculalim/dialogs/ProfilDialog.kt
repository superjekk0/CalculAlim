package org.mandziuk.calculalim.dialogs

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.mandziuk.calculalim.adapters.ProfilAdapter
import org.mandziuk.calculalim.databinding.DialogProfileBinding
import org.mandziuk.calculalim.db.models.Profil

class ProfilDialog(context: AppCompatActivity, profils: ArrayList<Profil>) : CustomDialog(context) {
    private val binding: DialogProfileBinding =
        DialogProfileBinding.inflate(LayoutInflater.from(context));

    init {
        setView(binding.root);

        binding.recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.recycler.adapter = ProfilAdapter(profils, context);
    }
}