package org.mandziuk.calculalim.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Environment
import android.os.Handler
import android.os.Looper
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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.activities.DrawerEnabledActivity
import org.mandziuk.calculalim.db.models.Profil
import org.mandziuk.calculalim.db.services.ProfileService
import org.mandziuk.calculalim.dialogs.IndexChangedListener
import java.io.File
import java.io.OutputStream

class ProfilAdapter(private val profils: ArrayList<Profil>, private val context: DrawerEnabledActivity, private val launcher: ActivityResultLauncher<String>, private val listener: IndexChangedListener) : Adapter<ProfilAdapter.ProfilVH>() {
    private val selectedVHManager = SelectedVHManager(context);

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
        selectedVHManager.drawIfSelected(holder, position);

        if (position == profils.size){
            ajouterProfil(holder);
        } else{
            holder.nom.text = profils[position].name;
            afficherProfil(holder, position);
        }

        holder.editer.setOnClickListener {
            listener.editProfile(profils[position]);
        }

        holder.ajoutProfil.setOnClickListener {
            listener.editProfile(null);
        }

        holder.item.setOnClickListener {
            selectedVHManager.setSelectedVH(holder, position);
            listener.indexChanged(profils[position].id);
        }
    }

    private fun afficherProfil(holder: ProfilVH, position: Int){
        holder.nouveauProfil.visibility = View.GONE;
        holder.profil.visibility = View.VISIBLE;

        if (profils[position].picture != null){
            holder.imageProfil.setImageURI(profils[position].picture);
        } else{
            holder.imageProfil.setImageResource(R.drawable.ic_profil);
        }
    }

    private fun ajouterProfil(holder: ProfilVH){
        holder.nouveauProfil.visibility = View.VISIBLE;
        holder.profil.visibility = View.GONE;
    }
}