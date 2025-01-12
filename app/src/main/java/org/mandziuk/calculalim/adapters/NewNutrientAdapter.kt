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
                val formatteur = NumberFormat.getInstance(Locale.getDefault());
                try{
                    nutrients[position].value = formatteur.parse(text.toString())?.toFloat();
                    holder.valeur.error = null;
                } catch (e: NumberFormatException){
                    holder.valeur.error = context.getString(R.string.chiffreInvalide);
                }
            }
        }
    }
}