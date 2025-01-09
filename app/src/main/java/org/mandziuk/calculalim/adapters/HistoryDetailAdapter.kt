package org.mandziuk.calculalim.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.activities.MealActivity
import org.mandziuk.calculalim.db.models.Repas

class HistoryDetailAdapter(private val context: Context) : Adapter<HistoryDetailAdapter.HistoryDetailVH>() {
    private val repas: ArrayList<Repas> = ArrayList<Repas>();

    class HistoryDetailVH(itemView: View) : ViewHolder(itemView) {
        val details: Button = itemView.findViewById(R.id.details);
        val heure: TextView = itemView.findViewById(R.id.heure);
    }

    fun setList(list: List<Repas>) {
        val ancienneTaille = repas.size;
        repas.clear();
        notifyItemRangeRemoved(0, ancienneTaille);
        repas.addAll(list);
        notifyItemRangeInserted(0, list.size);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDetailVH {
        val view = LayoutInflater.from(parent.context).
            inflate(R.layout.item_history_date_details, parent, false);
        return HistoryDetailVH(view);
    }

    override fun getItemCount(): Int {
        return repas.size;
    }

    override fun onBindViewHolder(holder: HistoryDetailVH, position: Int) {
        holder.heure.text = context.getString(R.string.heureHistorique, repas[position].date);
        holder.details.setOnClickListener {
            // TODO : Implémenter le détail
            val intent = Intent(context, MealActivity::class.java);
            intent.putExtra("repasId", repas[position].id);
            context.startActivity(intent);
        }
    }
}