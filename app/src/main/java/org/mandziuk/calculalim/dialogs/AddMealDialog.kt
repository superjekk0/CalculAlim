package org.mandziuk.calculalim.dialogs

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.databinding.DialogAddMealBinding

class AddMealDialog(context: Context) : AlertDialog(context) {
    var mealName: String = "";
    var mealWeight: Int = 0;
    private lateinit var dialogAddMealBinding : DialogAddMealBinding;

    init {
        setButton(BUTTON_NEGATIVE, "HELP"){
                dialog, _ -> dialog.dismiss();
        };

        setButton(BUTTON_POSITIVE, "OK"){
                dialog, _ -> dialog.dismiss();
        }
    }

    private fun initializeBinding(){
        dialogAddMealBinding = DialogAddMealBinding.inflate(layoutInflater);
    }

    override fun onStart() {
        super.onStart();
        dialogAddMealBinding.nomAliment.setText(mealName);
        dialogAddMealBinding.quantiteMasse.setText(mealWeight.toString());
        setView(dialogAddMealBinding.root);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        initializeBinding();
        setContentView(dialogAddMealBinding.root);
    }
}