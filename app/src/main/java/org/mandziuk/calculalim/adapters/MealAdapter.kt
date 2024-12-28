package org.mandziuk.calculalim.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.activities.FoodActivity
import org.mandziuk.calculalim.db.dtos.MealDTO
import org.mandziuk.calculalim.db.dtos.mealDtoInstance

class MealAdapter(private val context: Context) : Adapter<MealAdapter.MyVH>() {
    private val aliments : MealDTO = mealDtoInstance;
    class MyVH(itemView: View) : ViewHolder(itemView) {
        val aliment : TextView = itemView.findViewById(R.id.food_name);
        val poids : TextView = itemView.findViewById(R.id.food_weight);
    }

    override fun getItemCount(): Int {
        return aliments.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        val view = LayoutInflater.from(parent.context).
                inflate(R.layout.meal_item, parent, false);
        return MyVH(view);
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {
        holder.poids.text = aliments[position].weight.toString();
        holder.aliment.text = aliments[position].foodName;

        holder.aliment.setOnClickListener {
            val intent = Intent(context, FoodActivity::class.java);
            intent.putExtra("id", aliments[position].foodId);
            intent.putExtra("poids", aliments[position].weight);
            context.startActivity(intent);
        }
    }
}