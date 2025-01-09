package org.mandziuk.calculalim.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.db.models.Repas
import java.util.Calendar
import java.util.Date

class HistoryDateAdapter(private val context: Context, private val repas: List<Repas>) : RecyclerView.Adapter<HistoryDateAdapter.HistoryDateVH>() {
    private val datesRepas = repas.groupBy { r ->
        val calendar = Calendar.getInstance();
        calendar.time = r.date;
        Triple(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)) }
        .map { (d, x) ->
            return@map Set<Date, List<Repas>>(
                Date.from(Calendar.getInstance().apply {
                    set(Calendar.YEAR, d.first);
                    set(Calendar.MONTH, d.second);
                    set(Calendar.DAY_OF_MONTH, d.third);
                }.toInstant()),
                x);
        };

    private class Set<T, U> (val first: T, val second: U);

    class HistoryDateVH(itemView: View, context: Context) : ViewHolder(itemView){
        val date : TextView = itemView.findViewById(R.id.date);
        private val repasRecycler : RecyclerView = itemView.findViewById(R.id.recycler);
        private val adapter = HistoryDetailAdapter(context);
        init {
            repasRecycler.layoutManager = LinearLayoutManager(context);
            // TODO : Changer l'adapter
            repasRecycler.adapter = adapter;
        }

        fun setSubList(subList: List<Repas>) {
            adapter.setList(subList);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDateVH {
        val view = LayoutInflater.from(parent.context).
            inflate(R.layout.item_history, parent, false);
        return HistoryDateVH(view, context);
    }

    override fun getItemCount(): Int {
        return datesRepas.size;
    }

    override fun onBindViewHolder(holder: HistoryDateVH, position: Int) {
        holder.date.text = context.getString(R.string.dateHistorique, datesRepas[position].first);
        holder.setSubList(datesRepas[position].second);
    }
}