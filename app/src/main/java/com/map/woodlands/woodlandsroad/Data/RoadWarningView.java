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

    public RoadWarningView(Context context, RoadForm form){
        super(context);
        this.mRoadForm = form;
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
        if(mRoadForm.messages != null){
            StringBuilder sb = new StringBuilder();
                for(String s : mRoadForm.messages){
//                    Log.i("debug", s);
                    sb.append("- ").append(s).append("\n");
                }
            return sb.toString();
        }

        return "None";
    }
}
