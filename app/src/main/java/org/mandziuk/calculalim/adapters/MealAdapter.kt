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
import org.mandziuk.calculalim.db.dtos.FoodDTO
import org.mandziuk.calculalim.db.dtos.MealDTO

class MealAdapter(private val context: Context) : Adapter<MealAdapter.MyVH>() {
    private val aliments : MealDTO = MealDTO();
    class MyVH(itemView: View) : ViewHolder(itemView) {
        val aliment : TextView = itemView.findViewById(R.id.food_name);
        val groupe : TextView = itemView.findViewById(R.id.food_group);
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
        holder.groupe.text = aliments[position].foodDTO.foodGroup;
        holder.aliment.text = aliments[position].foodDTO.foodName;

        holder.aliment.setOnClickListener {
            val intent = Intent(context, FoodActivity::class.java);
            intent.putExtra("id", aliments[position].foodDTO.foodId);
            intent.putExtra("poids", 100);
            context.startActivity(intent);
        }
    }

}