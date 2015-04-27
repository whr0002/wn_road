package com.map.woodlands.woodlandsmap.Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.map.woodlands.woodlandsmap.Fragments.FormFragment;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Jimmy on 3/18/2015.
 */
public class FormController {

    private Type mType;
    private ArrayList<Form> mForms;
    private Gson gson;
    private String json;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    private Context mContext;
    private FormFragment mFormFragment;
    private View loadingView;
    private ProgressDialog progressDialog = null;

    public FormController(Context context){
        mContext = context;
        this.mType = new TypeToken<ArrayList<Form>>(){}.getType();
        this.mForms = new ArrayList<Form>();
        this.gson = new Gson();
        this.sp = mContext.getSharedPreferences("Data", 0);
        this.spEditor = sp.edit();
    }

//    public FormController(Context context, FormFragment formFragment, View loadingView){
//        mContext = context;
//        this.mType = new TypeToken<ArrayList<Form>>(){}.getType();
//        this.mForms = new ArrayList<Form>();
//        this.gson = new Gson();
//        this.sp = mContext.getSharedPreferences("Data", 0);
//        this.spEditor = sp.edit();
//        this.mFormFragment = formFragment;
//        this.loadingView = loadingView;
//    }
    public FormController(Context context, FormFragment formFragment){
        mContext = context;
        this.mType = new TypeToken<ArrayList<Form>>(){}.getType();
        this.mForms = new ArrayList<Form>();
        this.gson = new Gson();
        this.sp = mContext.getSharedPreferences("Data", 0);
        this.spEditor = sp.edit();
        this.mFormFragment = formFragment;
    }

    /* Get all ready-to-submit forms */
    public ArrayList<Form> getReadyToSubmitForms(){
        ArrayList<Form> tempForms = new ArrayList<Form>();
        mForms.clear();
        json = sp.getString("FormData","");
        if(!json.equals("")){
            mForms = gson.fromJson(json, mType);
            for(Form f : mForms){
                if(f.STATUS.toLowerCase().contains("ready")){
                    tempForms.add(f);
                }
            }
            return tempForms;
        }

        return null;
    }

    /* Get all forms */
    public ArrayList<Form> getAllForms(){
        mForms.clear();
        json = sp.getString("FormData","");
        if(!json.equals("")){
            mForms = gson.fromJson(json, mType);

        }

        return mForms;
    }

    public void deleteOneForm(int formID){
        mForms.clear();
        mForms = getAllForms();
        IndexForm mif = getIndexForm(formID);
        if(mif != null){
            mForms.remove(mif.index);
            deleteImageFileIfExists(mif.form);
        }

        json = gson.toJson(mForms);
        spEditor.putString("FormData", json);
        spEditor.commit();

        mFormFragment.setListView();

    }

    public void deleteAllForms(){
        mForms.clear();
        json = sp.getString("FormData", "");
        if(!json.equals("")){
            mForms = gson.fromJson(json, mType);
            for(Form f : mForms){
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
                mForms.clear();
                mForms = getReadyToSubmitForms();
                if (mForms != null) {
                    int counter = 1;
                    int total = mForms.size();
                    if (total > 0) {

                        progressDialog = ProgressDialog.show(mContext,"","Uploading...", true);

                        if(total < 10) {
                            for (Form form : mForms) {
                                Uploader uploader = new Uploader(form,
                                        mContext, mFormFragment, progressDialog, counter, total);
                                uploader.execute();


                                counter++;
                            }
                        }else{
                            for(int i = 0;i<10;i++){
                                Form form = mForms.get(i);
                                Uploader uploader = new Uploader(form,
                                        mContext, mFormFragment, progressDialog, counter, 10);
                                uploader.execute();


                                counter++;
                            }
                        }

                    }
                }

//                mFormFragment.setListView();
            }else{
                Toast.makeText(mContext,
                        "You are not authorized to upload any forms",
                        Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(mContext, "Please login first", Toast.LENGTH_LONG).show();
        }
    }

    public void saveForm(Form form){
        mForms = getAllForms();
        mForms.add(form);
        json = gson.toJson(mForms);
        spEditor.putString("FormData", json);
        spEditor.commit();

//        Log.i("debug", "json2: " + json);

    }

    public void saveForm(int index, Form form){
        mForms.clear();
        mForms = getAllForms();
        mForms.set(index, form);
        json = gson.toJson(mForms);
        spEditor.putString("FormData", json);
        spEditor.commit();
    }

    public int getNextFormID(){
        int i = sp.getInt("ID",0);
        spEditor.putInt("ID", i+1);
        spEditor.commit();
        return i;
    }

    public  IndexForm getIndexForm(int formID){

        mForms.clear();
        mForms = getAllForms();
        for(int i=0;i<mForms.size();i++){
            if(mForms.get(i).ID == formID){
                return new IndexForm(i, mForms.get(i));
            }
        }

        return null;
    }

    private void deleteImageFileIfExists(Form form) {
        ArrayList<String> paths = new ArrayList<String>();
        paths.add(form.PHOTO_INUP);
        paths.add(form.PHOTO_INDW);
        paths.add(form.PHOTO_OTUP);
        paths.add(form.PHOTO_OTDW);
        paths.add(form.PHOTO_1);
        paths.add(form.PHOTO_2);

        for(String path : paths){
            if(path != null) {
                File file = new File(path);
                if (file.exists()) {
                    // File exists, delete it
                    file.delete();
                }
            }
        }

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
        ArrayList<Form> forms = new ArrayList<Form>();
        for(int i=0;i<20;i++){
            Form f = new Form();
            f.ID = i;
            f.INSP_DATE = "4/22/2015";
            f.INSP_CREW = "Tester";
            f.STR_CLASS = "Non - fluvial";
            f.CROSS_TYPE = "Culvert - multiple";
            f.EROSION = "No";
            f.FISH_PCONC = "No Concerns";
            f.BLOCKAGE = "No";
            f.EMG_REP_RE = "No";
            f.STU_PROBS = "No";
            f.STATUS = "Ready to summit";
//            f.PHOTO_INUP = "/storage/emulated/0/Android/data/com.map.woodlands.woodlandsmap/files/Pictures/picupload/JPEG_20150421_122128.jpg";
//            f.PHOTO_INDW = "/storage/emulated/0/Android/data/com.map.woodlands.woodlandsmap/files/Pictures/picupload/JPEG_20150421_122128.jpg";
//            f.PHOTO_OTUP = "/storage/emulated/0/Android/data/com.map.woodlands.woodlandsmap/files/Pictures/picupload/JPEG_20150421_122128.jpg";
//            f.PHOTO_OTDW = "/storage/emulated/0/Android/data/com.map.woodlands.woodlandsmap/files/Pictures/picupload/JPEG_20150421_122128.jpg";
//            f.PHOTO_1 = "/storage/emulated/0/Android/data/com.map.woodlands.woodlandsmap/files/Pictures/picupload/JPEG_20150421_122128.jpg";
//            f.PHOTO_2 = "/storage/emulated/0/Android/data/com.map.woodlands.woodlandsmap/files/Pictures/picupload/JPEG_20150421_122128.jpg";

            f.CULV_DIA_1 = "300";
            f.CULV_DIA_1_M = "3";

            f.CULV_OPOOD = "300";
            f.CULV_OPGAP = "20";
            f.LAT = "0";
            f.LONG = "0";

            f.CULV_SUBSTYPE1 = "Sand";
            f.CULV_SUBSPROPORTION1 = "0 - 25";

            f.CULV_SUBSTYPE2 = "Gravel";
            f.CULV_SUBSPROPORTION2 = "26 - 50";

            f.CULV_SUBSTYPE3 = "Cobble";
            f.CULV_SUBSPROPORTION3 = "51 - 75";


            forms.add(f);
        }
        String json = gson.toJson(forms);
        spEditor.putString("FormData", json);
        spEditor.commit();
    }
}
