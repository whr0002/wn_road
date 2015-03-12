package com.map.woodlands.woodlandsmap.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.map.woodlands.woodlandsmap.Data.Form;
import com.map.woodlands.woodlandsmap.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Jimmy on 3/12/2015.
 */
public class EditFormActivity extends FormActivity{

    private int formID;
    private int formIndex;
    private ArrayList<Form> mForms;
    private Form theForm;
    private TextView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateView = (TextView) findViewById(R.id.dateView);

        this.formID = getIntent().getIntExtra("ID", -1);

        if(formID != -1){
            // Got the form ID, set its data on view
            setForm();
        }

    }

    @Override
    public Form getForm() {
        if(theForm != null){
            theForm.INSP_DATE = dateView.getText().toString();


            return theForm;
        }
        return super.getForm();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                // Discard this form, open confirmation box
                finish();
                return true;

            case R.id.save:
                // Save form

                Form form = getForm();
                if(validateForms(form)){
                    // Form is complete
                    modifyData(form);
                    finish();
                }else{
                    // Form is not complete

                }
                return true;



            default:
                return true;
        }
    }

    private void modifyData(Form form) {
        mForms.set(formIndex, form);
        SharedPreferences sp = getSharedPreferences("Data",0);
        SharedPreferences.Editor spEditor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mForms);

        spEditor.putString("FormData", json);
        spEditor.commit();


    }

    /*
    * Set form data in view
    * */
    private void setForm() {
        theForm = searchForForm();
        if(theForm != null) {
            dateView.setText(theForm.INSP_DATE);
        }
    }

    /*
    * Return a form by form ID
    * */
    private Form searchForForm() {
        SharedPreferences sp = getSharedPreferences("Data", 0);
//        SharedPreferences.Editor spEditor = sp.edit();
        String json = sp.getString("FormData", "");
        if(!json.equals("")){
            Gson gson = new Gson();
            Type listType = new TypeToken< ArrayList<Form>>(){}.getType();
            mForms = gson.fromJson(json, listType);

            for(int i=0;i<mForms.size();i++){
                if(mForms.get(i).ID == formID){
                    formIndex = i;
                    return mForms.get(i);
                }
            }

        }
        return null;
    }
}
