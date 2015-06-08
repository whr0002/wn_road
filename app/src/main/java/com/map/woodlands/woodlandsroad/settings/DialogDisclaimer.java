package com.map.woodlands.woodlandsroad.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import com.map.woodlands.woodlandsroad.R;

/**
 * Created by Jimmy on 4/8/2015.
 */
public class DialogDisclaimer extends DialogPreference{
    public DialogDisclaimer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.icon_disclaimer);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
        super.onPrepareDialogBuilder(builder);
    }
}
