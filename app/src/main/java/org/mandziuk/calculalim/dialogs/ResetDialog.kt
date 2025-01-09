package org.mandziuk.calculalim.dialogs

import android.app.AlertDialog
import android.content.Context

class ResetDialog(context: Context) : AlertDialog.Builder(context) {
    var annule = false;

    init {
        setTitle("Réinitialisation de l'application");
        setMessage("Voulez-vous vraiment réinitialiser l'application? Cette action est irréversible, sans possibilité de restaurer les données précédemment enregistrées.");

        setPositiveButton("Réinitialiser") { _, _ ->};
        setNegativeButton("Annuler") { dialog, _ -> dialog.cancel(); };

        setOnCancelListener { annule = true; };
    }
}