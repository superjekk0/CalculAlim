package org.mandziuk.calculalim

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class FoodAdapter : Adapter<FoodAdapter.MyVH>() {

    class MyVH(itemView: View) : ViewHolder(itemView) {
        init {

        }
    }

    override fun getItemCount(): Int {
        return 50;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        var view = LayoutInflater.from(parent.context).
                inflate(R.layout.food_item, parent, false);

        return MyVH(view);
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {

    }

}