package org.mandziuk.calculalim.activities

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

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
     * @param openDrawerContentDescRes le texte à afficher pour ouvrir le tiroir
     * @param closeDrawerContentDescRes le texte à afficher pour fermer le tiroir
     */
    protected fun initializeDrawer(drawerLayout: DrawerLayout, navigationView: NavigationView, @StringRes openDrawerContentDescRes: Int, @StringRes closeDrawerContentDescRes: Int) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        abToggle = ActionBarDrawerToggle(this, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        drawerLayout.addDrawerListener(abToggle);
        abToggle.syncState();
        // TODO : Gérer les différents cas de navigation
        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawers();
            return@setNavigationItemSelectedListener true;
        }
    }
}