package org.mandziuk.calculalim.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import kotlinx.coroutines.NonDisposableHandle.parent
import org.mandziuk.calculalim.db.models.Nutrient

class NutrientAdapter() : Adapter<NutrientAdapter.NutrientViewHolder>() {

    private var nutrients = emptyList<Nutrient>();
    fun emptyList(){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutrientAdapter.NutrientViewHolder {
        TODO("Not yet implemented")
    }


    fun ViewHolder() {
//        val view: `val` =
//            LayoutInflater.from(parent.context).inflate(R.layout.nutrient_item, parent, false)
//        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NutrientAdapter.NutrientViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return nutrients.size;
    }

    class NutrientViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView){

    }


}
