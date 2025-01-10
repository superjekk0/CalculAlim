package org.mandziuk.calculalim.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import org.mandziuk.calculalim.adapters.ProfilAdapter
import org.mandziuk.calculalim.databinding.DialogProfileBinding
import org.mandziuk.calculalim.db.models.Profil

class ProfilDialog(context: Context, profils: List<Profil>) : CustomDialog(context) {
    private val binding =
        DialogProfileBinding.inflate(LayoutInflater.from(context));

    init {
        setView(binding.root);

        binding.recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.recycler.adapter = ProfilAdapter(profils);
    }
}