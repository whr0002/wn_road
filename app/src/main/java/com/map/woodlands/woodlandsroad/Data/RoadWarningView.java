package com.map.woodlands.woodlandsroad.Data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Jimmy on 3/18/2015.
 */
public class RoadWarningView extends IconView {
    public RoadWarningView(Context context) {
        super(context);
    }

    public RoadWarningView(Context context, Form form){
        super(context);
        this.mForm = form;
    }

    @Override
    public void showDialog() {
        alertDialogBuilder.setTitle("Errors");
        alertDialogBuilder.setMessage(getMessage());
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public String getMessage(){
        if(mForm.messages != null){
            StringBuilder sb = new StringBuilder();
                for(String s : mForm.messages){
//                    Log.i("debug", s);
                    sb.append("- ").append(s).append("\n");
                }
            return sb.toString();
        }

        return "None";
    }
}
