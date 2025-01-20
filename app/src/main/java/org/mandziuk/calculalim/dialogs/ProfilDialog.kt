package org.mandziuk.calculalim.dialogs

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.media.ExifInterface
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toFile
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.activities.DrawerEnabledActivity
import org.mandziuk.calculalim.adapters.ProfilAdapter
import org.mandziuk.calculalim.databinding.DialogProfileBinding
import org.mandziuk.calculalim.db.models.Profil
import org.mandziuk.calculalim.db.services.ProfileService
import java.io.File
import java.io.FileDescriptor
import java.io.InputStream
import java.net.URI

interface IndexChangedListener {
    fun indexChanged(index: Long);
    fun editProfile(profil: Profil?);
}

class ProfilDialog(private val context: DrawerEnabledActivity, profils: ArrayList<Profil>, private val launcher: ActivityResultLauncher<String>) : CustomDialog(context), IndexChangedListener {
    private val binding: DialogProfileBinding =
        DialogProfileBinding.inflate(LayoutInflater.from(context));

    private lateinit var dialog: AlertDialog;
    private var selectedProfil: Long = -1L;
    private var nouvelleImage: Bitmap? = null;

    override fun show(): AlertDialog {
        dialog = super.show();
        return dialog;
    }

    init {
        setView(binding.root);

        binding.recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.recycler.adapter = ProfilAdapter(profils, context, launcher, this);

        binding.changement.setOnClickListener {
            val profileService = ProfileService(context);

            context.lifecycle.coroutineScope.launch {
                val profil = profileService.setProfile(selectedProfil);
                context.onProfileChanged(profil);
                dialog.dismiss();
            }
        }
    }

    override fun indexChanged(index: Long) {
        if (index != -1L){
            binding.changement.isEnabled = true;
            selectedProfil = index;
        }
    }

    override fun editProfile(profil: Profil?) {
        val profileService = ProfileService(context);
        binding.recycler.visibility = View.GONE;
        binding.editionProfil.visibility = View.VISIBLE;

        binding.imageProfilEdition.setOnClickListener{
            launcher.launch("image/*")
        }

        binding.nomEdition.setText(profil?.name);

        if (profil?.picture != null){
            binding.imageProfilEdition.setImageURI(profil.picture);
        } else{
            binding.imageProfilEdition.setImageResource(R.drawable.ic_profil);
        }

        binding.nomEdition.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrBlank()){
                binding.nomEdition.error = context.getString(R.string.erreurNomProfilVide);
            } else {
                context.lifecycle.coroutineScope.launch {
                    val available = profileService.availableProfileName(text.toString());
                    if (!available){
                        binding.nomEdition.error = context.getString(R.string.erreurNomProfilPris);
                    } else{
                        binding.nomEdition.error = null;
                    }
                }
            }

        }

        context.setOnImageChosenListener { uri, _ ->
            dessinerImage(uri)
        }

        binding.sauvegarder.setOnClickListener {
            if (binding.nomEdition.error != null){
                return@setOnClickListener;
            }

            context.lifecycle.coroutineScope.launch {
                val name = binding.nomEdition.text.toString();
                val picture = binding.imageProfilEdition.drawable;
                val newProfil = Profil(0L, name, null);
                profileService.createProfile(newProfil);
            }
        }
    }

    private fun dessinerImage(uri: Uri) {
        val inputStream = context.contentResolver.openInputStream(uri);
        val bitmap = BitmapFactory.decodeStream(inputStream);
        inputStream?.close();
        
        val streamExif = context.contentResolver.openInputStream(uri);
        val exifInterface = ExifInterface(streamExif!!);
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        streamExif.close();

        val matrix = Matrix();
        when (orientation){
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.preRotate(90F);
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.preRotate(180F);
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.preRotate(270F);
        }

        if (bitmap.height > bitmap.width){
            nouvelleImage = Bitmap.createScaledBitmap(bitmap, binding.imageProfilEdition.width, bitmap.height * 100 / bitmap.width, true);
        } else{
            nouvelleImage = Bitmap.createScaledBitmap(bitmap, bitmap.width * 100 / bitmap.height, binding.imageProfilEdition.height, true);
        }

        nouvelleImage = Bitmap.createBitmap(nouvelleImage!!, 0, 0, nouvelleImage!!.width, nouvelleImage!!.height, matrix, true);
//        nouvelleImage = Bitmap.createScaledBitmap(bitmap, binding.imageProfilEdition.width, binding.imageProfilEdition.height, true);
        binding.imageProfilEdition.setImageBitmap(nouvelleImage);
    }
}