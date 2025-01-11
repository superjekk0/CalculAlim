package org.mandziuk.calculalim.dialogs

import android.content.Context
import org.mandziuk.calculalim.R

class RestoreDialog(context: Context) : CustomDialog(context) {
    init {
        setTitle("Restaurer cet aliment?");
        setPositiveButton(R.string.confirmer) { dialog, _ ->
            dialog.dismiss();
        };

        setNegativeButton(R.string.dialogueResetAnnuler) { dialog, _ ->
            dialog.cancel();
        };
    }
}