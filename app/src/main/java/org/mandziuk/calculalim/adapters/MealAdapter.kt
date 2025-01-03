package org.mandziuk.calculalim.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.activities.FoodActivity
import org.mandziuk.calculalim.db.dtos.MealDTO

class MealAdapter(private val context: Context, private val repadId: Long, private val removeResult: ActivityResultLauncher<Intent>) : Adapter<MealAdapter.MyVH>() {
    private val aliments : MealDTO = MealDTO();
    class MyVH(itemView: View) : ViewHolder(itemView) {
        val aliment : TextView = itemView.findViewById(R.id.food_name);
        val poids : TextView = itemView.findViewById(R.id.food_weight);
    }

    fun setItems(items: MealDTO){
        val ancienneTaille = aliments.size;
        aliments.clear();
        notifyItemRangeRemoved(0, ancienneTaille);
        aliments.addAll(items);
        notifyItemRangeInserted(0, items.size);
    }

    fun removeFood(foodId: Long) {
        val index = aliments.indexOfFirst { it.foodId == foodId };
        notifyItemRemoved(index);
        aliments.removeAt(index);
        val alimentsAffectes = aliments.subList(index, aliments.size);
        notifyItemRangeChanged(index, alimentsAffectes.size);
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
            intent.putExtra("repasId", repadId);
            removeResult.launch(intent);
        }
    }
}