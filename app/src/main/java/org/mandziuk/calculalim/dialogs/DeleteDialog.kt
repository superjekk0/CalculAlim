package org.mandziuk.calculalim.dialogs

import android.content.Context
import org.mandziuk.calculalim.R

class DeleteDialog(context: Context) : CustomDialog(context) {
    init {
        setTitle(context.getString(R.string.supprimerAlimentTitre));
        setMessage(context.getString(R.string.messageSuppressionAliment));
        setPositiveButton(context.getString(R.string.confirmer)) { dialog, _ ->
            dialog.dismiss();
        };
        setNegativeButton(context.getString(R.string.dialogueResetAnnuler)) { dialog, _ ->
            dialog.cancel();
        };
    }
}