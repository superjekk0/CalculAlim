package org.mandziuk.calculalim.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.databinding.ItemProfilBinding
import org.mandziuk.calculalim.db.models.Profil

class ProfilAdapter(private val profils: List<Profil>, private val context: Context) : RecyclerView.Adapter<ProfilAdapter.ProfilVH>() {

    class ProfilVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profil: LinearLayout = itemView.findViewById(R.id.profil);
        val imageProfil: ImageView = itemView.findViewById(R.id.imageProfil);
        val nom: TextView = itemView.findViewById(R.id.nom);
        val editer: ImageButton = itemView.findViewById(R.id.editer);
        val editionProfil: LinearLayout = itemView.findViewById(R.id.editionProfil);
        val imageProfilEdition: ImageView = itemView.findViewById(R.id.imageProfilEdition);
        val nomEdition: EditText = itemView.findViewById(R.id.nomEdition);
        val sauvegarder: ImageButton = itemView.findViewById(R.id.sauvegarder);
        val nouveauProfil: LinearLayout = itemView.findViewById(R.id.nouveauProfil);
        val ajoutProfil: Button = itemView.findViewById(R.id.ajoutProfil);
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
            holder.nom.text = profils[position].name;
            holder.nomEdition.setText(profils[position].name);
            afficherProfil(holder);
        }

        holder.nomEdition.doOnTextChanged { text, _, _, _ ->
            Log.i("ProfilAdapter", "Text changed to $text");
        }

        holder.nomEdition.setOnClickListener{
            Log.i("ProfilAdapter", "Câlice");
        }

        holder.nomEdition.setOnContextClickListener { v ->
            Log.i("ProfilAdapter", "Context click");
            true;
        }

        holder.editer.setOnClickListener {
            afficherEdition(holder);
            context.getSystemService(InputMethodManager::class.java);
        }
    }

    private fun afficherEdition(holder: ProfilVH){
        holder.nouveauProfil.visibility = View.GONE;
        holder.profil.visibility = View.GONE;
        holder.editionProfil.visibility = View.VISIBLE;
    }

    private fun afficherProfil(holder: ProfilVH){
        holder.nouveauProfil.visibility = View.GONE;
        holder.profil.visibility = View.VISIBLE;
        holder.editionProfil.visibility = View.GONE;
    }

    private fun ajouterProfil(holder: ProfilVH){
        holder.nouveauProfil.visibility = View.VISIBLE;
        holder.profil.visibility = View.GONE;
        holder.editionProfil.visibility = View.GONE;
    }
}