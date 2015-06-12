package com.map.woodlands.woodlandsroad.Data;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Jimmy on 6/1/2015.
 */
public class SubmitDialog {

    private Activity mActivity;
    private List<RoadForm> mForms;
    private String mMessage;
    private RoadFormController mRoadFormController;
    private ArrayAdapter<RoadForm> mAdapter;

    public SubmitDialog(Activity formActivity, List<RoadForm> forms,
                        String message, RoadFormController roadFormController, ArrayAdapter<RoadForm> adapter){
        mActivity = formActivity;
        mForms = forms;
        mMessage = message;
        mRoadFormController = roadFormController;
        mAdapter = adapter;

    }

    public void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        builder.setTitle("Submit");
        builder.setMessage(mMessage);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mRoadFormController.submitForms(mForms);

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });

        builder.show();
    }
}
