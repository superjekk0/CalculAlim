package org.mandziuk.calculalim.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        val boutonAjoutProfil: Button = itemView.findViewById(R.id.ajoutProfil);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilVH {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.item_profil, parent, false);
        return ProfilVH(view);
    }

    override fun getItemCount(): Int {
        return profils.size + 1;
    }

    override fun onBindViewHolder(holder: ProfilVH, position: Int) {
        if (position == profils.size){
            ajouterProfil(holder);
        } else{
            afficherProfil(holder, position);
        }
    }

    private fun afficherProfil(holder: ProfilVH, position: Int){
        holder.nom.text = profils[position].name;

        holder.edition.setOnClickListener {
            holder.nom.visibility = View.GONE;
            holder.editeurNom.visibility = View.VISIBLE;
            holder.editeurNom.setText(profils[position].name);
        }
    }

    private fun ajouterProfil(holder: ProfilVH){
        holder.imageProfil.setImageResource(R.drawable.ic_ajouter);
        holder.boutonAjoutProfil.visibility = View.VISIBLE;
        holder.nom.visibility = View.GONE;
        holder.edition.visibility = View.GONE;
        holder.editeurNom.visibility = View.GONE;
    }
}