package org.mandziuk.calculalim.dialogs

import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.activities.DrawerEnabledActivity
import org.mandziuk.calculalim.adapters.ProfilAdapter
import org.mandziuk.calculalim.databinding.DialogProfileBinding
import org.mandziuk.calculalim.db.models.Profil
import org.mandziuk.calculalim.db.services.ProfileService

interface IndexChangedListener {
    fun indexChanged(index: Long);
}

class ProfilDialog(context: DrawerEnabledActivity, profils: ArrayList<Profil>/*, launcher: ActivityResultLauncher<String>*/) : CustomDialog(context), IndexChangedListener {
    private val binding: DialogProfileBinding =
        DialogProfileBinding.inflate(LayoutInflater.from(context));

    private lateinit var dialog: AlertDialog;
    private var selectedProfil: Long = -1L;

    override fun show(): AlertDialog {
        dialog = super.show();
        return dialog;
    }

    init {
        setView(binding.root);

        binding.recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.recycler.adapter = ProfilAdapter(profils, context/*, launcher*/, this);

        binding.changement.setOnClickListener {
            val profileService = ProfileService(context);

            context.lifecycle.coroutineScope.launch {
                val profil = profileService.setProfile(selectedProfil);
                context.onProfileChanged(profil);
                dialog.dismiss();
            }
        }
    }

    override fun indexChanged(index: Long) {
        if (index != -1L){
            binding.changement.isEnabled = true;
            selectedProfil = index;
        }
    }
}