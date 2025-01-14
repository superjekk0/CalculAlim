package org.mandziuk.calculalim.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.db.views.FoodNutrientDetails
import java.util.Locale

class NutrientAdapter(private val context: Context) : Adapter<NutrientAdapter.NutrientViewHolder>() {

    private var nutrients = emptyList<FoodNutrientDetails>().toMutableList();

    fun setList(nutrients : List<FoodNutrientDetails>){
        val ancienneTaille = this.nutrients.size;
        this.nutrients.clear();
        this.notifyItemRangeRemoved(0, ancienneTaille);
        this.nutrients.addAll(nutrients);
        this.notifyItemRangeInserted(0, nutrients.size);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutrientViewHolder {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.item_nutrient, parent, false);
        return NutrientViewHolder(view);
    }

    override fun onBindViewHolder(holder: NutrientViewHolder, position: Int) {
        val locale: String = Locale.getDefault().displayLanguage;
        holder.nutriment.text =
            if (locale.contains("fr", true))
                nutrients[position].nutrientNameFr
            else nutrients[position].nutrientName;
        var texte = context.getString(R.string.quantiteNutriment);
        texte = texte.replace("*", "${nutrients[position].precision}");
        texte = texte.format(nutrients[position].value,
            nutrients[position].unit);
        holder.quantite.text = texte;
    }

    override fun getItemCount(): Int {
        return nutrients.size;
    }

    class NutrientViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView){
        val nutriment: TextView = itemView.findViewById(R.id.nutrient_name);
        val quantite: TextView = itemView.findViewById(R.id.nutrient_value);
    }


}
