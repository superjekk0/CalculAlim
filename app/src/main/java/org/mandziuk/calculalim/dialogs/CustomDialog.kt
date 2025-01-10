package org.mandziuk.calculalim.dialogs

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

abstract class CustomDialog(context: Context) : AlertDialog.Builder(context) {
    var annule = false;

    init {
        setOnCancelListener {
            annule = true;
        }
    }

    final override fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener?): AlertDialog.Builder {
        return super.setOnCancelListener(onCancelListener)
    }
}