package com.map.woodlands.woodlandsroad.Data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.map.woodlands.woodlandsroad.Fragments.FormFragment;

/**
 * Created by Jimmy on 3/18/2015.
 */
public class DeleteView extends IconView{
    private FormController formController;

    public DeleteView(Context context) {
        super(context);
    }

    public DeleteView(Context context, Form form, FormFragment formFragment){
        super(context);
        this.mForm = form;
        this.formController = new FormController(context,formFragment);
    }

    @Override
    public void showDialog() {
        alertDialogBuilder.setTitle("Delete");
        alertDialogBuilder.setMessage("Are you sure you want to delete this form?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                formController.deleteOneForm(mForm.ID);
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
