package com.map.woodlands.woodlandsroad.Data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.map.woodlands.woodlandsroad.Fragments.FormFragment;

/**
 * Created by Jimmy on 5/14/2015.
 */
public class SingleSubmitView extends IconView {
    private Form mForm;
    private FormController formController;

    public SingleSubmitView(Context context) {
        super(context);
    }

    public SingleSubmitView(Context context, Form form, FormFragment formFragment){
        super(context);
        mForm = form;

        formController = new FormController(context, formFragment);


    }

    @Override
    public void showDialog() {
        alertDialogBuilder.setTitle("Submit");
        alertDialogBuilder.setMessage("Are you sure you want to submit this form?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                formController.submitSingleForm(mForm);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
