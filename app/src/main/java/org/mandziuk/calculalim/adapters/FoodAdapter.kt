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

class FoodAdapter(val context: Context) : Adapter<FoodAdapter.MyVH>() {
//
//    constructor() : super();

    private val aliments : ArrayList<FoodDTO> = ArrayList();
    class MyVH(itemView: View) : ViewHolder(itemView) {
        val aliment : TextView = itemView.findViewById(R.id.food_name);
        val groupe : TextView = itemView.findViewById(R.id.food_group);
    }

    fun setList(aliments : List<FoodDTO>){
        this.aliments.clear();
        this.aliments.addAll(aliments);
        notifyDataSetChanged();
    }

    override fun getItemCount(): Int {
        return aliments.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        val view = LayoutInflater.from(parent.context).
                inflate(R.layout.food_item, parent, false);
        return MyVH(view);
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {
        holder.groupe.text = aliments[position].foodGroup;
        holder.aliment.text = aliments[position].foodName;

        holder.aliment.setOnClickListener {
            val intent: Intent = Intent(context, FoodActivity::class.java);
            intent.putExtra("id", aliments[position].foodId);
            context.startActivity(intent);
        }
    }

}