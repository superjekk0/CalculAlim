package org.mandziuk.calculalim.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.databinding.DialogAddMealBinding
import org.mandziuk.calculalim.db.dtos.FoodGroupDTO

class AddMealDialog(context: Context, groupes: List<FoodGroupDTO>) : AlertDialog.Builder(context) {
    var mealName: String? = null;
    var mealWeight: Int? = null;
    var foodGroupId: Long = 0L;
    var portionWeight: Int? = null;
    var portionName: String? = null;
    private lateinit var binding : DialogAddMealBinding;
    private lateinit var dialog: AlertDialog;

    init {
        initializeBinding();
        setView(binding.root);

        binding.positif.setOnClickListener {
            var erreurActive = false;
            if (groupes.find { it.foodGroupId == foodGroupId } == null){
                binding.groupeAliment.error = context.getString(R.string.erreurGroupeAliment);
                erreurActive = true;
            }

            if (mealName == null || mealName == ""){
                binding.nomAliment.error = context.getString(R.string.erreurNomAliment);
                erreurActive = true;
            }

            if (!erreurActive){
                dialog.dismiss();
            }
        }

        binding.negatif.setOnClickListener {
            dialog.cancel();
        }

        binding.groupeAliment.setOnClickListener { _  ->
            val groupDialog = AlertDialog.Builder(context);
            groupDialog.setTitle(R.string.typeNourriture).setSingleChoiceItems(groupes.map { cg -> cg.groupName }.toTypedArray(), foodGroupId.toInt()) { dialog, index ->
                binding.groupeAliment.text = groupes[index].groupName;
                foodGroupId = groupes[index].foodGroupId;
                dialog.dismiss();
                binding.groupeAliment.error = null;
            }

            groupDialog.show();
        }

        binding.quantiteMasse.doOnTextChanged { text, _, _, _ ->
            mealWeight = when (text?.length) {
                null -> null;
                0 -> null;
                else ->
                    text.toString().toInt();
            }
        }

        binding.nomAliment.doOnTextChanged { text, _, _, _ ->
            mealName = text?.toString();
        }

        binding.nomPortion.doOnTextChanged { text, _, _, _ ->
            portionName = text?.toString();
        }

        binding.quantitePortion.doOnTextChanged { text, _, _, _ ->
            portionWeight = when (text?.length) {
                null -> null;
                0 -> null;
                else ->
                    text.toString().toInt();
            }
        }
    }

    override fun show(): AlertDialog {
        this.dialog = super.show();
        return dialog;
    }

    private fun initializeBinding(){
        binding = DialogAddMealBinding.inflate(LayoutInflater.from(context));
    }
}