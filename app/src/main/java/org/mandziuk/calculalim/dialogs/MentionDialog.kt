package org.mandziuk.calculalim.dialogs

import android.content.Context
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import android.text.util.Linkify
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.text.method.LinkMovementMethodCompat
import org.mandziuk.calculalim.R

class MentionDialog(context: Context) : AlertDialog(context) {
    init {
        val text = TextView(context);
        val spannableString = SpannableString(context.getString(R.string.mentions));
        Linkify.addLinks(spannableString, Linkify.WEB_URLS);
        text.text = spannableString;
        text.setPadding(20, 10, 20, 10);
        text.textSize = 18f;
        text.movementMethod = LinkMovementMethod.getInstance();
        setView(text);
        setTitle(R.string.titreMentionsLegales);
        setButton(BUTTON_POSITIVE, context.getString(R.string.ok)) { dialog, _ -> dialog.dismiss() };
    }
}