package org.mandziuk.calculalim.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.activities.indexGroup
import org.mandziuk.calculalim.databinding.DialogAddMealBinding
import org.mandziuk.calculalim.db.dtos.FoodGroupDTO

class AddMealDialog(context: Context, groupes: List<FoodGroupDTO>) : AlertDialog.Builder(context) {
    var mealName: String? = "";
    var mealWeight: Int? = 0;
    var indexGroup: Long = 0L;
    private lateinit var binding : DialogAddMealBinding;

    init {
        initializeBinding();
        setView(binding.root);

        setNegativeButton( "HELP"){
            dialog, _ -> dialog.dismiss();
        };

        setPositiveButton("OK"){
            dialog, _ -> dialog.dismiss();
        }


        binding.groupeAliment.setOnClickListener { _  ->
            val groupDialog = AlertDialog.Builder(context);
            groupDialog.setTitle(R.string.typeNourriture).setSingleChoiceItems(groupes.map { cg -> cg.groupName }.toTypedArray(), indexGroup.toInt()) { dialog, index ->
                binding.groupeAliment.text = groupes[index].groupName;
                indexGroup = index.toLong();
                dialog.dismiss();
            }

            groupDialog.show();
        }

        //dialogAddMealBinding.groupeAliment.getWindowVisibleDisplayFrame(Rect())

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
    }

    private fun initializeBinding(){
        binding = DialogAddMealBinding.inflate(LayoutInflater.from(context));
    }
}