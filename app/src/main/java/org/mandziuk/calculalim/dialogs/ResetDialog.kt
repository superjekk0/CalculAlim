package org.mandziuk.calculalim.dialogs

import android.app.AlertDialog
import android.content.Context
import org.mandziuk.calculalim.R

class ResetDialog(context: Context) : CustomDialog(context) {
    init {
        setTitle(context.getString(R.string.dialogueTitreReset));
        setMessage(context.getString(R.string.dialogueResetDescription));

        setPositiveButton(context.getString(R.string.dialogueResetConfirmer)) { _, _ ->};
        setNegativeButton(context.getString(R.string.dialogueResetAnnuler)) { dialog, _ -> dialog.cancel(); };
    }
}