package com.map.woodlands.woodlandsmap.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.map.woodlands.woodlandsmap.Fragments.FormFragment;
import com.map.woodlands.woodlandsmap.R;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jimmy on 3/13/2015.
 */
public class Uploader {
    public Form mForm;
    public Context mContext;
    private UserInfo user;
    private AsyncHttpClient client;
    private ArrayList<Form> mForms;
    private FormController mFormController;
    private String baseStorageUrl;
    private HashMap<String,String> mHashMap;
    private View loadingView;
    private int current;
    private int total;



    public Uploader(Form form, Context context){
        this.mForm = form;
        this.mContext = context;
        this.user = getUserInfo();
        this.client = new AsyncHttpClient();
        this.mFormController = new FormController(mContext);
//        client.setConnectTimeout(10000);

//        if(user != null){
//            client.setBasicAuth(user.username, user.password);
//        }
    }
    public Uploader(Form form, Context context, FormFragment formFragment, View loadingView, int current, int total){
        this.mForm = form;
        this.mContext = context;
        this.user = getUserInfo();
        this.client = new AsyncHttpClient();
        this.mFormController = new FormController(mContext, formFragment, loadingView);
        this.baseStorageUrl = mContext.getResources().getString(R.string.storage_url);
        this.mHashMap = new HashMap<String, String>();
        this.loadingView = loadingView;
        this.current = current;
        this.total = total;

//        client.setConnectTimeout(10000);

//        if(user != null){
//            client.setBasicAuth(user.username, user.password);
//        }
    }
    public Uploader(ArrayList<Form> forms, Context context){
        this.mForms = forms;
        this.mContext = context;
        this.user = getUserInfo();
        this.client = new AsyncHttpClient();
    }

    public void execute(){
        if(user != null) {
            uploadPhotos();
//            uploadForm();
        }
    }

    public void uploadPhotos(){
        List<String> photoPathes = new ArrayList<String>();
        photoPathes.add(mForm.PHOTO_INUP);
        photoPathes.add(mForm.PHOTO_INDW);
        photoPathes.add(mForm.PHOTO_OTUP);
        photoPathes.add(mForm.PHOTO_OTDW);
        photoPathes.add(mForm.PHOTO_1);
        photoPathes.add(mForm.PHOTO_2);
        String s = mForm.AttachmentPath1;
        if(s != null && !s.equals("")){
//            Log.i("debug","added: "+s);
            photoPathes.add(s);
        }


        RequestParams params = new RequestParams();
        UserInfo ui = getUserInfo();
        if(ui.role != null){
            params.put("Role", ui.role);
        }

        for(int i=0;i<photoPathes.size();i++){
            String path = photoPathes.get(i);
            int j = i+1;
            String paramName = "image"+j;
            if(path != null){
                File mFile = new File(path);
                setRealPhotoUrl(mFile, ui.role, j);
                try{
                    params.put(paramName, mFile);
                }catch (FileNotFoundException e){
                    Toast.makeText(mContext, "Image file not found", Toast.LENGTH_SHORT).show();
                }


            }
        }

        client.post(mContext.getResources().getString(R.string.image_post_url), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String s = "";
                try {
                    s = new String(bytes, "UTF-8");
                }catch (Exception e){}
                if(s.contains("success")){
                    uploadForm();
                }else{
                    // Uploading failed
                    if(current == total){
                        Toast.makeText(mContext,"Photo upload failed, please try later",Toast.LENGTH_SHORT).show();
                        loadingView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                if(current == total){
                    Toast.makeText(mContext,"Network Error when uploading photos",Toast.LENGTH_SHORT).show();
                    loadingView.setVisibility(View.GONE);
                }
            }
        });


    }

    private void setRealPhotoUrl(File file, String role, int index){
        if(file.exists()){
            String filename = file.getName();
            String fullPath = "";
            if(role == null){
                fullPath = baseStorageUrl+"unknown/"+filename;
            }else if(role.toLowerCase().equals("super admin")){
                fullPath = baseStorageUrl+"superadmin/"+filename;
            }else{
                fullPath = baseStorageUrl+role.toLowerCase()+"/"+filename;
            }

//            Log.i("debug", fullPath);
            mHashMap.put(Integer.toString(index), fullPath);

        }
    }

    public void uploadForm(){
        RequestParams params = new RequestParams();
        try{
            params.put("UserName", user.getUsername());
            params.put("Group", user.role);
            params.put("INSP_DATE",mForm.INSP_DATE);
            params.put("INSP_CREW",mForm.INSP_CREW);
            params.put("ACCESS",mForm.ACCESS);
            params.put("CROSS_NM",mForm.CROSS_NM);
            params.put("CROSS_ID",mForm.CROSS_ID);
            params.put("LAT",mForm.LAT);
            params.put("LONG",mForm.LONG);
            params.put("STR_ID",mForm.STR_ID);
            params.put("STR_CLASS",streamMapping(mForm.STR_CLASS));
            params.put("STR_WIDTH",mForm.STR_WIDTH);
            params.put("STR_WIDTHM",mForm.STR_WIDTHM);
            params.put("DISPOSITION_ID",mForm.DISPOSITION_ID);
            params.put("CROSS_TYPE",mForm.CROSS_TYPE);
            params.put("EROSION",mForm.EROSION);
            params.put("EROSION_TY1",mForm.EROSION_TY1);
            params.put("EROSION_TY2",mForm.EROSION_TY2);
            params.put("EROSION_SO",mForm.EROSION_SO);
            params.put("EROSION_DE",mForm.EROSION_DE);
            params.put("EROSION_AR",mForm.EROSION_AR);
            params.put("BLOCKAGE",mForm.BLOCKAGE);
            params.put("BLOC_MATR",mForm.BLOC_MATR);
            params.put("BLOC_CAUS",mForm.BLOC_CAUS);
            params.put("CULV_SUBS",mForm.CULV_SUBS);
            params.put("CULV_SLOPE",mForm.CULV_SLOPE);
            params.put("SCOUR_POOL",mForm.SCOUR_POOL);
            params.put("DELINEATOR",mForm.DELINEATOR);
            params.put("FISH_SAMP",mForm.FISH_SAMP);
            params.put("FISH_SM",mForm.FISH_SM);
            params.put("FISH_SPP",mForm.FISH_SPP);
            params.put("FISH_PCONC",mForm.FISH_PCONC);
            params.put("FISH_SPP2",mForm.FISH_SPP2);
            params.put("FISH_PCONCREASON",combineFishReasons
                    (mForm.FISH_ReasonDropdown,mForm.FISH_PCONCREASON));
            params.put("REMARKS",mForm.REMARKS);
//            params.put("PHOTO_INUP",mForm.PHOTO_INUP);
//            params.put("PHOTO_INDW",mForm.PHOTO_INDW);
//            params.put("PHOTO_OTUP",mForm.PHOTO_OTUP);
//            params.put("PHOTO_OTDW",mForm.PHOTO_OTDW);
//            params.put("PHOTO_1",mForm.PHOTO_1);
//            params.put("PHOTO_2",mForm.PHOTO_2);

            params.put("PHOTO_INUP",mHashMap.get("1"));
            params.put("PHOTO_INDW",mHashMap.get("2"));
            params.put("PHOTO_OTUP",mHashMap.get("3"));
            params.put("PHOTO_OTDW",mHashMap.get("4"));
            params.put("PHOTO_1",mHashMap.get("5"));
            params.put("PHOTO_2",mHashMap.get("6"));
            params.put("CULV_LEN",mForm.CULV_LEN);
            params.put("CULV_SUBSP",mForm.CULV_SUBSP);
            params.put("CULV_SUBSTYPE",mForm.CULV_SUBSTYPE);
            params.put("CULV_SUBSPROPORTION",mForm.CULV_SUBSPROPORTION);
            params.put("CULV_BACKWATERPROPORTION",mForm.CULV_BACKWATERPROPORTION);
            params.put("CULV_OUTLETTYPE",mForm.CULV_OUTLETTYPE);
            params.put("CULV_DIA_1",mForm.CULV_DIA_1);
            params.put("CULV_DIA_2",mForm.CULV_DIA_2);
            params.put("CULV_DIA_3",mForm.CULV_DIA_3);
            params.put("BRDG_LEN",mForm.BRDG_LEN);
            params.put("EMG_REP_RE",mForm.EMG_REP_RE);
            params.put("STU_PROBS",mForm.STU_PROBS);
            params.put("SEDEMENTAT",mForm.SEDEMENTAT);
            params.put("CULV_OPOOD",mForm.CULV_OPOOD);
            params.put("CULV_OPGAP",mForm.CULV_OPGAP);
            params.put("HAZMARKR",mForm.HAZMARKR);
            params.put("APROCHSIGR",mForm.APROCHSIGR);
            params.put("APROCHRAIL",mForm.APROCHRAIL);
            params.put("RDSURFR",mForm.RDSURFR);
            params.put("RDDRAINR",mForm.RDDRAINR);
            params.put("VISIBILITY",mForm.VISIBILITY);
            params.put("WEARSURF",mForm.WEARSURF);
            params.put("RAILCURBR",mForm.RAILCURBR);
            params.put("GIRDEBRACR",mForm.GIRDEBRACR);
            params.put("CAPBEAMR",mForm.CAPBEAMR);
            params.put("PILESR",mForm.PILESR);
            params.put("ABUTWALR",mForm.ABUTWALR);
            params.put("WINGWALR",mForm.WINGWALR);
            params.put("BANKSTABR",mForm.BANKSTABR);
            params.put("SLOPEPROTR",mForm.SLOPEPROTR);
            params.put("CHANNELOPEN",mForm.CHANNELOPEN);
            params.put("OBSTRUCTIO",mForm.OBSTRUCTIO);

        }catch (Exception e){
            e.printStackTrace();
        }
        client.post(mContext.getResources().getString(R.string.form_post_url), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                Log.i("debug", "200 OK");
                String s = "";
                try {
                    s = new String(bytes, "UTF-8");
                }catch (Exception e){}
//                Log.i("debug", s);

                if(s.toLowerCase().equals("form submitted")) {
                    deleteFormIfSuccess();
                }

                if(current == total){
                    Toast.makeText(mContext,s,Toast.LENGTH_SHORT).show();
                    loadingView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                Log.i("debug", "40X Fail");
                String s = "Form upload fail";
                try {
                    s = new String(bytes, "UTF-8");
                }catch (Exception e){}


                if(current == total){
                    Toast.makeText(mContext,s,Toast.LENGTH_SHORT).show();
                    loadingView.setVisibility(View.GONE);
                }
            }
        });

    }

    private void deleteFormIfSuccess() {
        mFormController.deleteOneForm(mForm.ID);
    }

    public void uploadJson(){
        Gson gson = new Gson();
        JsonModel jm= new JsonModel(mForms);
        String json = gson.toJson(jm);
        Log.i("debug", json);
        StringEntity entity = null;
        try {
            entity = new StringEntity(json);
            entity.setContentType("application/json");
        }catch (Exception e){e.printStackTrace();}

        client.post(mContext, "http://woodlandstest.azurewebsites.net/android/jsonpost", entity,
                "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.i("debug", "json 200 OK");
                String response = "";
                try{
                    response = new String(bytes,"UTF-8");
                }catch (Exception e){}
                Log.i("debug", response);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i("debug", "json 40X fail");
                String response = "";
                try{
                    response = new String(bytes,"UTF-8");
                }catch (Exception e){}
                Log.i("debug", response);
            }
        }  );

    }

    public UserInfo getUserInfo(){
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

    private String streamMapping(String s){
        if(s != null) {
            s = s.toLowerCase();
            if (s.contains("ephemeral")) {
                return "ephemeral";
            } else if (s.contains("non")) {
                return "non-fluvial";
            } else if (s.contains("intermittent")) {
                return "fluvial - intermittent";
            } else if (s.contains("small")) {
                return "fluvial - small permanent";
            } else if (s.contains("large")) {
                return "fluvial - large permanent";
            }
        }
        return null;
    }

    private String combineFishReasons(String s1, String s2){
        if(s1 != null){
            if(s2 != null){
                return s1+" "+s2;
            }else{
                return s1;
            }
        }else{
            if(s2 != null){
                return s2;
            }else{
                return "";
            }
        }
    }
}
