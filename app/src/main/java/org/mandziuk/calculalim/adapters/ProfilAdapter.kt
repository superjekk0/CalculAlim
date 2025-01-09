package org.mandziuk.calculalim.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.adapters.NutrientAdapter.NutrientViewHolder
import org.mandziuk.calculalim.db.models.Profil

class ProfilAdapter(private val profils: List<Profil>) : RecyclerView.Adapter<ProfilAdapter.ProfilVH>() {

    class ProfilVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nom: TextView = itemView.findViewById(R.id.nom);
        val edition: ImageButton = itemView.findViewById(R.id.editer);
        val editeurNom: EditText = itemView.findViewById(R.id.nomEdition);
        val imageProfil: ImageView = itemView.findViewById(R.id.imageProfil);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilVH {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.item_profil, parent, false);
        return ProfilVH(view);
    }

    override fun getItemCount(): Int {
        return profils.size;
    }

    override fun onBindViewHolder(holder: ProfilVH, position: Int) {
        holder.nom.text = profils[position].name;

        holder.edition.setOnClickListener {
            holder.nom.visibility = View.GONE;
            holder.editeurNom.visibility = View.VISIBLE;
            holder.editeurNom.setText(profils[position].name);
        }
    }
}