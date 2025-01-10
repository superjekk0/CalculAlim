package org.mandziuk.calculalim.adapters

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.activities.newPhotoUri
import org.mandziuk.calculalim.db.models.Profil
import org.mandziuk.calculalim.db.services.ProfileService

class ProfilAdapter(private val profils: ArrayList<Profil>, private val context: AppCompatActivity, private val launcher: ActivityResultLauncher<String>) : RecyclerView.Adapter<ProfilAdapter.ProfilVH>() {
    private val profileService = ProfileService(context);

    class ProfilVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: View = itemView.findViewById(R.id.item);
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
            if (text.isNullOrBlank()){
                holder.nomEdition.error = context.getString(R.string.erreurNomProfilVide);
            } else{
                context.lifecycle.coroutineScope.launch {
                    val available = profileService.availableProfileName(text.toString());
                    if (!available){
                        holder.nomEdition.error = context.getString(R.string.erreurNomProfilPris);
                    } else{
                        holder.nomEdition.error = null;
                    }
                }
            }

        }

        holder.editer.setOnClickListener {
            afficherEdition(holder);
        }

        holder.sauvegarder.setOnClickListener {
            if (holder.nomEdition.error != null){
                return@setOnClickListener;
            }

            context.lifecycle.coroutineScope.launch {
                if (position == profils.size) {
                    val profil = Profil(0L, holder.nomEdition.text.toString());
                    val nouveauProfil = profileService.createProfile(profil);
                    profils.add(nouveauProfil);
                    notifyItemInserted(profils.size);
                    notifyItemMoved(profils.size, profils.size + 1);
                } else{
                    val profil =  profils[position];
                    val nouveauProfil = Profil(
                        id = profil.id,
                        name = holder.nomEdition.text.toString()
                    );
                    profileService.updateProfileName(nouveauProfil);
                    profils[position] = nouveauProfil;
                }
                holder.nom.text = holder.nomEdition.text.toString();
                afficherProfil(holder);
            }
        }

        // TODO : Gérer les images
        holder.imageProfilEdition.setOnClickListener{
            launcher.launch("image/*")
        }

        holder.ajoutProfil.setOnClickListener {
            afficherEdition(holder);
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