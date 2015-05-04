package com.map.woodlands.woodlandsmap.Data;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Jimmy on 3/18/2015.
 * A custom ImageView with a Click Listener
 */
public class IconView extends ImageView implements View.OnClickListener{

    public Form mForm;
    public AlertDialog.Builder alertDialogBuilder;
    public IconView(Context context) {
        super(context);
        alertDialogBuilder = new AlertDialog.Builder(context);
    }

    @Override
    public void onClick(View v) {
        showDialog();

    }

    public void showDialog(){

    }
}
