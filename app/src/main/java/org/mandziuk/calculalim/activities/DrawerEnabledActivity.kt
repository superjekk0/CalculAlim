package org.mandziuk.calculalim.activities

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.db.models.Profil
import org.mandziuk.calculalim.db.services.ProfileService
import org.mandziuk.calculalim.dialogs.ProfilDialog

/**
 * Classe de laquelle les activités ayant un tiroir doivent hériter pour avoir le même comportement
 * quel que soit l'activité.
 */

var newPhotoUri = Uri.EMPTY;

interface ProfileChangedListener {
    fun onProfileChanged(profil: Profil);
}

abstract class DrawerEnabledActivity : AppCompatActivity(), ProfileChangedListener {
    protected lateinit var abToggle: ActionBarDrawerToggle;
    private lateinit var photoPickerLauncher: ActivityResultLauncher<String>;
    private lateinit var navigation: NavigationView;

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (abToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState);
        abToggle.syncState();
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        abToggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        photoPickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent(), ActivityResultCallback { result ->
            if (result == null){
                return@ActivityResultCallback;
            }

            Log.i("Profil", "Bravo! Tu as choisi une image");
            Log.i("Profil", result.toString());
            newPhotoUri = result;
        });
    }

    /**
     * Méthode permettant d'initialiser le tiroir. Nécessaire afin de faire fonctionner le tiroir.
     * @param drawerLayout La vue de navigation
     * @param navigationView Le tiroir de navigation
     */
    protected fun initializeDrawer(drawerLayout: DrawerLayout, navigationView: NavigationView) {
        navigation = navigationView;
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        abToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.ouvrirTiroir, R.string.fermerTiroir);
        drawerLayout.addDrawerListener(abToggle);
        abToggle.syncState();
        // TODO : Gérer les différents cas de navigation
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                // TODO : Ajouter les cas de navigation
                R.id.recherche -> {
                    val intent = Intent(this, MainActivity::class.java);
                    startActivity(intent);
                }
                R.id.repas ->{
                    val intent = Intent(this, MealActivity::class.java);
                    startActivity(intent);
                }
                R.id.historique ->{
                    val intent = Intent(this, HistoryActivity::class.java);
                    startActivity(intent);
                }
                R.id.parametres ->{
                    val intent = Intent(this, ParametersActivity::class.java);
                    startActivity(intent);
                }
                R.id.profil->{
                    lifecycle.coroutineScope.launch {
                        val profileService = ProfileService(this@DrawerEnabledActivity);
                        val profils = profileService.getProfiles();
                        val profilDialog = ProfilDialog(this@DrawerEnabledActivity, profils, photoPickerLauncher);

                        profilDialog.show();
                    }
                }
            }
            drawerLayout.closeDrawers();
            return@setNavigationItemSelectedListener true;
        }

        lifecycleScope.launch{
            val context = this@DrawerEnabledActivity;
            val profil = ProfileService(context).getProfile();
            val header = navigation.getHeaderView(0);
            header.findViewById<TextView>(R.id.nomProfil).text =
                getString(R.string.salutation, profil.name);
        }
    }

    override fun onProfileChanged(profil: Profil) {
        val header = navigation.getHeaderView(0);
        header.findViewById<TextView>(R.id.nomProfil).text = getString(R.string.salutation, profil.name);
    }
}