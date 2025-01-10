package org.mandziuk.calculalim.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.databinding.DialogAddMealBinding
import org.mandziuk.calculalim.db.dtos.FoodGroupDTO
import org.mandziuk.calculalim.db.dtos.NewFoodDTO
import org.mandziuk.calculalim.db.services.FoodService
import org.mandziuk.calculalim.db.services.TakenStatus

class AddMealDialog(context: AppCompatActivity, private val groupes: List<FoodGroupDTO>) : CustomDialog(context) {
    val newFoodDTO = NewFoodDTO();
    private lateinit var binding : DialogAddMealBinding;
    private lateinit var dialog: AlertDialog;
    private val foodService = FoodService(context);

    init {
        initializeBinding();
        setView(binding.root);

        binding.positif.setOnClickListener {

            if (groupes.find { it.foodGroupId == newFoodDTO.foodGroupId } == null){
                binding.groupeAliment.error = context.getString(R.string.erreurGroupeAliment);
            }

            if (newFoodDTO.mealName == null || newFoodDTO.mealName == ""){
                binding.nomAliment.error = context.getString(R.string.erreurNomAliment);
            }

            val erreurActive = !valid();
            if (!erreurActive){
                dialog.dismiss();
            }
        }

        binding.negatif.setOnClickListener {
            dialog.cancel();
        }

        binding.groupeAliment.setOnClickListener { _  ->
            val groupDialog = AlertDialog.Builder(context);
            groupDialog.setTitle(R.string.typeNourriture).setSingleChoiceItems(groupes.map { cg -> cg.groupName }.toTypedArray(), newFoodDTO.foodGroupId.toInt()) { dialog, index ->
                binding.groupeAliment.text = groupes[index].groupName;
                newFoodDTO.foodGroupId = groupes[index].foodGroupId;
                dialog.dismiss();
                binding.groupeAliment.error = null;
            }

            groupDialog.show();
        }

        binding.quantiteMasse.doOnTextChanged { text, _, _, _ ->
            newFoodDTO.mealWeight = when (text?.length) {
                null -> null;
                0 -> null;
                else ->{
                    if (text == "0")
                        null
                    else
                        text.toString().toInt();
                }
            }
        }

        binding.nomAliment.doOnTextChanged { text, _, _, _ ->
            newFoodDTO.mealName = text?.toString();
            if (newFoodDTO.mealName != null){
                context.lifecycle.coroutineScope.launch {
                    when(foodService.foodNameTaken(newFoodDTO.mealName!!)){
                        TakenStatus.TAKEN_EN -> {
                            binding.nomAliment.error = context.getString(R.string.erreurNomAlimentPrisEn)
                        };
                        TakenStatus.TAKEN_FR -> {
                            binding.nomAliment.error = context.getString(R.string.erreurNomAlimentPrisFr)
                        };
                        else -> {
                            binding.nomAliment.error = null
                        };
                    }
                }
            }
        }

        binding.nomPortion.doOnTextChanged { text, _, _, _ ->
            newFoodDTO.portionName = text?.toString();
        }

        binding.quantitePortion.doOnTextChanged { text, _, _, _ ->
            newFoodDTO.portionWeight = when (text?.length) {
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

    private fun valid(): Boolean{
        var erreurActive = false;

        erreurActive = erreurActive || binding.groupeAliment.error != null;
        erreurActive = erreurActive || binding.nomAliment.error != null;
        erreurActive = erreurActive || binding.quantiteMasse.error != null;
        erreurActive = erreurActive || binding.quantitePortion.error != null;

        return !erreurActive;
    }
}