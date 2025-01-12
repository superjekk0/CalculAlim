package org.mandziuk.calculalim.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.activities.NutrientAmountDTO
import org.mandziuk.calculalim.db.models.Nutrient
import java.text.NumberFormat
import java.util.Locale


class NewNutrientAdapter(private val context: Context) : Adapter<NewNutrientAdapter.NewNutrientVH>() {
    class NewNutrientVH(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nutriment: TextView = itemView.findViewById(R.id.nutriment);
        val symbole: TextView = itemView.findViewById(R.id.symboleValeur);
        val valeur: EditText = itemView.findViewById(R.id.valeur);
    }

    private class NutrientValue(val nutrient: Nutrient, var value: Float?);
    private val nutrients = ArrayList<NutrientValue>();

    fun add(nutrient: Nutrient){
        nutrients.add(NutrientValue(nutrient, null));
        this.notifyItemInserted(nutrients.size - 1);
    }

    fun getNutrients(): List<NutrientAmountDTO>{
        return nutrients.map { NutrientAmountDTO(it.nutrient.id, it.value ?: 0.0F) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewNutrientVH {
        val view = LayoutInflater.from(parent.context).
            inflate(R.layout.item_nouveau_nutriment, parent, false);
        return NewNutrientVH(view);
    }

    override fun getItemCount(): Int {
        return nutrients.size;
    }

    override fun onBindViewHolder(holder: NewNutrientVH, position: Int) {
        holder.symbole.text = nutrients[position].nutrient.unit;

        if (Locale.getDefault().displayLanguage.contains("fr", true)){
            holder.nutriment.text = nutrients[position].nutrient.nameFr;
        } else {
            holder.nutriment.text = nutrients[position].nutrient.name;
        }

        holder.valeur.doOnTextChanged { text, _, _, _ ->
            if (! text.isNullOrEmpty()){
                if (text.contains('.')){
                    nutrients[position].nutrient.decimals.let {
                        val indexPoint = text.indexOf('.');
                        val decimal = text.substring(indexPoint + 1);
                        if (decimal.length > it){
                            holder.valeur.setText(text.substring(0, indexPoint + 1 + it.toInt()));
                            holder.valeur.setSelection(indexPoint + 1 + it.toInt());
                            nutrients[position].value = text.substring(0, indexPoint + 1 + it.toInt()).toFloat();
                        }
                    }
                } else{
                    nutrients[position].value = text.toString().toFloat();
                }
            }
        }
    }
}