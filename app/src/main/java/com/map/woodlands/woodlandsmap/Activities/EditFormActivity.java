package com.map.woodlands.woodlandsmap.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.map.woodlands.woodlandsmap.Data.Form;
import com.map.woodlands.woodlandsmap.Data.ImageProcessor;
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
    private boolean isInOnce = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!isInOnce && formID != -1){
            // Got the form ID, set its data on view
            setForm();
            isInOnce = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateView = (TextView) findViewById(R.id.dateView);

        this.formID = getIntent().getIntExtra("ID", -1);



    }

    /* Generate a new form */
    @Override
    public Form generateForm() {
        if(theForm != null){
            theForm.INSP_DATE = dateView.getText().toString();
            setPhotoPath(theForm);

            return theForm;
        }
        return super.generateForm();
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

                Form form = generateForm();
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
            setPhotoFromPath();
        }
    }

    private void setPhotoFromPath() {
        if(theForm.PHOTO_INUP != null){
            setPhotoView(photoView1, theForm.PHOTO_INUP);
            mPhotoMap.put(1,theForm.PHOTO_INUP);
        }

        if(theForm.PHOTO_INDW != null){
            setPhotoView(photoView2, theForm.PHOTO_INDW);
            mPhotoMap.put(2,theForm.PHOTO_INDW);
        }

        if(theForm.PHOTO_OTUP != null){
            setPhotoView(photoView3, theForm.PHOTO_OTUP);
            mPhotoMap.put(3, theForm.PHOTO_OTUP);
        }

        if(theForm.PHOTO_OTDW != null){
            setPhotoView(photoView4, theForm.PHOTO_OTDW);
            mPhotoMap.put(4, theForm.PHOTO_OTDW);
        }

        if(theForm.PHOTO_1 != null){
            setPhotoView(photoView5, theForm.PHOTO_1);
            mPhotoMap.put(5,theForm.PHOTO_1);
        }

        if(theForm.PHOTO_2 != null){
            setPhotoView(photoView6, theForm.PHOTO_2);
            mPhotoMap.put(6,theForm.PHOTO_2);
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

    @Override
    public void setPhotoView(ImageView photoView, String path) {
        ImageProcessor imageProcessor = new ImageProcessor(photoView, path, false);
        imageProcessor.setImageView();
    }
}
