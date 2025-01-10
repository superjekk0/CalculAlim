package org.mandziuk.calculalim.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import org.mandziuk.calculalim.adapters.ProfilAdapter
import org.mandziuk.calculalim.databinding.DialogProfileBinding
import org.mandziuk.calculalim.db.models.Profil

class ProfilDialogBuilder(context: Context, profils: List<Profil>) : CustomDialog(context) {
    private lateinit var binding: DialogProfileBinding;

    init {
        initializeBinding();
        setView(binding.root);

        binding.recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.recycler.adapter = ProfilAdapter(profils, context);
    }

    private fun initializeBinding(){
        binding = DialogProfileBinding.inflate(LayoutInflater.from(context));
    }

    override fun setView(view: View?): AlertDialog.Builder {
        return super.setView(view)
    }

    override fun show(): AlertDialog {
        val dialog = create();
        dialog.show();
        return super.show();
    }

    override fun create(): AlertDialog {
//        super.create()
        // TODO : Trouver une manière d'afficher ProfilDialog
        val profilDialog = ProfilDialog(context);
        profilDialog.setView(binding.root);
        return profilDialog;
    }
}

class ProfilDialog(context: Context) : AlertDialog(context){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }
}