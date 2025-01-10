package org.mandziuk.calculalim.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.activities.newPhotoUri
import org.mandziuk.calculalim.db.models.Profil
import org.mandziuk.calculalim.db.services.ProfileService
import org.mandziuk.calculalim.dialogs.IndexChangedListener

class ProfilAdapter(private val profils: ArrayList<Profil>, private val context: AppCompatActivity, private val launcher: ActivityResultLauncher<String>, private val listener: IndexChangedListener) : RecyclerView.Adapter<ProfilAdapter.ProfilVH>() {
    private val profileService = ProfileService(context);
    private val selectedVHManager = SelectedVHManager(context);

//    val choixProfil: SharedFlow<Long> = profilChoisi(-1);
//
//    private fun profilChoisi(position: Int) : SharedFlow<Long>{
//        return profils[position].id;
//    }

    private class SelectedVHManager(private val context: AppCompatActivity){
        private var selectedVH: ProfilVH? = null;
        private var position: Int = -1;

        fun setSelectedVH(vh: ProfilVH, position: Int){
            this.position = position;
            selectedVH?.item?.background = null;
            selectedVH = vh;
            selectedVH?.item?.background = AppCompatResources.getDrawable(context, R.color.itemSelected);
        }

        fun drawIfSelected(vh: ProfilVH, position: Int){
            if (position == this.position){
                selectedVH?.item?.background = null;
                selectedVH = vh;
                selectedVH?.item?.background = AppCompatResources.getDrawable(context, R.color.itemSelected);
            }
        }
    }

    class ProfilVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: LinearLayout = itemView.findViewById(R.id.item);
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

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ProfilVH, position: Int) {
        selectedVHManager.drawIfSelected(holder, position);

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

        holder.item.setOnClickListener {
            selectedVHManager.setSelectedVH(holder, position);
            listener.indexChanged(profils[position].id);
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