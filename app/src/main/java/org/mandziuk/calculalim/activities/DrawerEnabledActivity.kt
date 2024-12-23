package org.mandziuk.calculalim.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import org.mandziuk.calculalim.R

/**
 * Classe de laquelle les activités ayant un tiroir doivent hériter pour avoir le même comportement
 * quel que soit l'activité.
 */
open class DrawerEnabledActivity : AppCompatActivity() {
    protected lateinit var abToggle: ActionBarDrawerToggle;

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

    /**
     * Méthode permettant d'initialiser le tiroir. Nécessaire afin de faire fonctionner le tiroir.
     * @param drawerLayout La vue de navigation
     * @param navigationView Le tiroir de navigation
     */
    protected fun initializeDrawer(drawerLayout: DrawerLayout, navigationView: NavigationView) {
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
            }
            drawerLayout.closeDrawers();
            return@setNavigationItemSelectedListener true;
        }
    }
}