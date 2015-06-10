package com.map.woodlands.woodlandsroad.Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.map.woodlands.woodlandsroad.Fragments.RoadFormFragment;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Jimmy on 3/18/2015.
 * Used for get, save, and remove forms stored in the mobile device.
 */
public class RoadFormController {

    private Type mType;
    private Gson gson;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    private Context mContext;
    private RoadFormFragment mFormFragment;
    private ProgressDialog progressDialog = null;

    public RoadFormController(Context context){
        mContext = context;
        this.mType = new TypeToken<ArrayList<RoadForm>>(){}.getType();
        this.gson = new Gson();
        this.sp = mContext.getSharedPreferences("Data", 0);
        this.spEditor = sp.edit();
    }

    public RoadFormController(Context context, RoadFormFragment formFragment){
        mContext = context;
        this.mType = new TypeToken<ArrayList<RoadForm>>(){}.getType();
        this.gson = new Gson();
        this.sp = mContext.getSharedPreferences("Data", 0);
        this.spEditor = sp.edit();
        this.mFormFragment = formFragment;
    }

    /* Get all ready-to-submit forms */
    public ArrayList<RoadForm> getReadyToSubmitForms(){
        ArrayList<RoadForm> tempForms = new ArrayList<RoadForm>();
        ArrayList<RoadForm> returnForms = new ArrayList<RoadForm>();
//        mForms.clear();
        String json = sp.getString("FormData","");
        if(!json.equals("")){
            returnForms = gson.fromJson(json, mType);
            for(RoadForm f : returnForms){
                if(f.STATUS.toLowerCase().contains("ready")){
                    tempForms.add(f);
                }
            }
            return tempForms;
        }

        return null;
    }

    /* Get all forms */
    public ArrayList<RoadForm> getAllForms(){
        ArrayList<RoadForm> forms = new ArrayList<RoadForm>();
        String json = sp.getString("FormData", "");
        if(!json.equals("")){
            forms = gson.fromJson(json, mType);
        }

        return forms;
    }

    public synchronized void deleteOneForm(int formID){

        ArrayList<RoadForm> temp = getAllForms();
        IndexForm mif = getIndexForm(formID);
        if(mif != null){
            temp.remove(mif.index);
            deleteImageFileIfExists(mif.roadForm);
        }

        String json = gson.toJson(temp);
        spEditor.putString("FormData", json);
        spEditor.commit();

        mFormFragment.setListView();

    }

    public synchronized void deleteOneFormSync(int formID){
        ArrayList<RoadForm> forms = getAllForms();

        IndexForm mif = getIndexForm(formID);
        if(mif != null){
            forms.remove(mif.index);
            deleteImageFileIfExists(mif.roadForm);
        }

        String json = gson.toJson(forms);
        spEditor.putString("FormData", json);
        spEditor.commit();
    }

    private void deleteImageFileIfExists(RoadForm form) {
        ArrayList<Photo> photos = form.Photos;

        if(photos != null) {
            for (Photo photo : photos) {

                File file = new File(photo.path);
                if (file.exists()) {
                    file.delete();
                }

            }
        }
    }

    public synchronized void deleteAllForms(){
        ArrayList<RoadForm> forms = getAllForms();
        String json = sp.getString("FormData", "");
        if(!json.equals("")){
            forms = gson.fromJson(json, mType);
            for(RoadForm f : forms){
                deleteImageFileIfExists(f);
            }
        }
        spEditor.putString("FormData","");
        spEditor.commit();

        // Refresh view
        mFormFragment.setListView();
    }

    public void submitForms() {

        UserInfo ui = getUserInfo();
        if(ui != null) {
            if(ui.getRole() != null && !ui.getRole().equals("null")) {

                ArrayList<RoadForm> mForms = getReadyToSubmitForms();
                if (mForms != null) {
                    int counter = 1;
                    int total = mForms.size();
                    if (total > 0) {

//                        progressDialog = ProgressDialog.show(mContext,"","Uploading...", true);
                        progressDialog = new ProgressDialog(mContext);
                        progressDialog.setProgress(0);
                        progressDialog.setMax(total);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setTitle("Uploading");
                        progressDialog.show();

                        RoadUploader uploader = new RoadUploader(mContext, mFormFragment, progressDialog, total);
                        for (RoadForm form : mForms) {
                            uploader.execute(form,counter);
                            counter++;
                        }
                    }
                }
            }else{
                Toast.makeText(mContext,
                        "You are not authorized to upload any forms",
                        Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(mContext, "Please login first", Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     * Single form submission
     * @param f
     */
    public void submitSingleForm(Form f){
        UserInfo ui = getUserInfo();
        if(ui != null) {
            if(ui.getRole() != null && !ui.getRole().equals("null")) {

                if (f != null) {
                    int counter = 1;
                    int total = 1;
                    if (total > 0) {

                        progressDialog = ProgressDialog.show(mContext,"","Uploading...", true);

//                        Uploader uploader = new Uploader(mContext, mFormFragment, progressDialog, total);
//                        uploader.execute(f,counter);
                    }
                }
            }else{
                Toast.makeText(mContext,
                        "You are not authorized to upload any forms",
                        Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(mContext, "Please login first", Toast.LENGTH_LONG).show();
        }
    }

    public synchronized void saveForm(RoadForm form){
        ArrayList<RoadForm> mForms = getAllForms();
        mForms.add(form);
        String json = gson.toJson(mForms);
        spEditor.putString("FormData", json);
        spEditor.commit();

//        Log.i("debug", "json2: " + json);

    }

    public synchronized void saveForm(int index, RoadForm form){

        ArrayList<RoadForm> mForms = getAllForms();
        mForms.set(index, form);
        String json = gson.toJson(mForms);
        spEditor.putString("FormData", json);
        spEditor.commit();

//        Log.i("debug", json);
    }

    public synchronized int getNextFormID(){
        int i = sp.getInt("ID", 0);
        spEditor.putInt("ID", i+1);
        spEditor.commit();
        return i;
    }

    public synchronized IndexForm getIndexForm(int formID){

        ArrayList<RoadForm> mForms = getAllForms();
        for(int i=0;i<mForms.size();i++){
            if(mForms.get(i).ID == formID){
                return new IndexForm(i, mForms.get(i));
            }
        }

        return null;
    }



    private UserInfo getUserInfo(){
        SharedPreferences sp = mContext.getSharedPreferences("UserInfo", 0);
        String json = sp.getString("json","");

        if(!json.equals("")){
            Gson gson = new Gson();
            UserInfo user;
            user = gson.fromJson(json, UserInfo.class);
            return user;
        }
        return null;
    }

    public void addTestData(){
        ArrayList<RoadForm> forms = new ArrayList<RoadForm>();
        for(int i=0;i<1;i++){
            RoadForm f = new RoadForm();



            forms.add(f);
        }
        String json = gson.toJson(forms);
        spEditor.putString("FormData", json);
        spEditor.commit();
    }
}
