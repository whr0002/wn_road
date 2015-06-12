package com.map.woodlands.woodlandsroad.Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.map.woodlands.woodlandsroad.Fragments.RoadFormFragment;
import com.map.woodlands.woodlandsroad.R;

import org.apache.http.Header;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jimmy on 3/13/2015.
 * Used for uploading forms to the server
 */
public class RoadUploader {

    public Context mContext;
    private UserInfo user;
    private AsyncHttpClient client;

    private RoadFormController mFormController;
    private String baseStorageUrl;
    private HashMap<String,String> mHashMap;

    private int total;
    private ProgressDialog progressDialog = null;
    private RoadFormFragment mFormFragment;
    private ExecutorService executorService;

    private AtomicInteger counter = new AtomicInteger();
    private AtomicInteger successCounter = new AtomicInteger();


    public RoadUploader(Context context, RoadFormFragment formFragment,
                        ProgressDialog p, int total){

        this.mContext = context;
        this.user = getUserInfo();
        this.client = new AsyncHttpClient();
        this.executorService = Executors.newFixedThreadPool(10);
        this.client.setThreadPool(executorService);


        this.mFormController = new RoadFormController(mContext, formFragment);
        this.baseStorageUrl = mContext.getResources().getString(R.string.storage_url);


        this.total = total;
        this.progressDialog = p;
        this.mFormFragment = formFragment;




    }


    public void execute(RoadForm f, int c){
        if(user != null) {
            this.mHashMap = new HashMap<String, String>();
            upload(f, c);

            if(c == total){
                executorService.shutdown();

            }
        }
    }

    public void upload(final RoadForm mForm, final int c){

        RequestParams params = getParams(mForm);

        if(user != null){

            client.post(mContext.getResources()
                            .getString(R.string.image_post_url)
                    , params,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {

                            int currentC = counter.incrementAndGet();
                            progressDialog.setProgress(currentC);




                            String s = "";

                            try {
                                s = new String(bytes, "UTF-8");
//                                    Log.i("debug", "Response: " + s);
                            }catch (Exception e){}


                            // Delete submitted form
                            if(s.equals("Form Submitted")){
                               successCounter.incrementAndGet();
                                deleteFormIfSuccess(mForm);

                            }

                            String msg = "Submitted "
                                    + successCounter.get()
                                    + "/"
                                    + total;

                            // Send a msg to user when uploading is done
                            if(currentC == total){
                                mFormFragment.setListView();
                                Toast.makeText(mContext, msg ,Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            int currentC = counter.incrementAndGet();
                            int totalSuccess = successCounter.get();
                            progressDialog.setProgress(currentC);


                            String msg = "Submitted "
                                    + totalSuccess
                                    + "/"
                                    + total;
                            try{
                                String s = new String(bytes, "utf-8");
                                Log.i("debug", "Failed: "+ s);
                            }catch (Exception e){

                            }
                            if(currentC == total){

                                mFormFragment.setListView();
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        }
                    });


        }else{
            Toast.makeText(mContext, "Do not have User Information when uploading forms", Toast.LENGTH_SHORT).show();
        }

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

    public RequestParams getParams(RoadForm mForm){
        RequestParams params = new RequestParams();
        params.put("Email", user.getUsername());
        params.put("Password", user.getPassword());
        params.put("Role", user.role);

        params.put("UserName", user.getUsername());
        params.put("Group", user.role);
        if(user.role.equals("super admin")){
            params.put("Client", mForm.Client);
        }else{
            params.put("Client", user.role);
        }

        params.put("InspectorName", mForm.InspectorName);
        params.put("INSP_DATE",mForm.INSP_DATE);
        params.put("Licence", mForm.Licence);
        params.put("RoadName", mForm.RoadName);
        params.put("DLO", mForm.DLO);
        params.put("KmFrom", mForm.KmFrom);
        params.put("KmTo", mForm.KmTo);
        params.put("RoadStatus", mForm.RoadStatus);
        params.put("StatusMatch", mForm.StatusMatch);
        params.put("RS_Condition", mForm.RS_Condition);
        params.put("RS_Notification", mForm.RS_Notification);
        params.put("RS_RoadSurface", mForm.RS_RoadSurface);
        params.put("RS_GravelCondition", mForm.RS_GravelCondition);
        params.put("RS_VegetationCover", mForm.RS_VegetationCover);
        params.put("RS_CoverType", mForm.RS_CoverType);
        params.put("DI_Ditches", mForm.DI_Ditches);
        params.put("DI_VegetationCover", mForm.DI_VegetationCover);
        params.put("DI_CoverType", mForm.DI_CoverType);
        params.put("OT_Signage", mForm.OT_Signage);
        params.put("OT_Crossings", mForm.OT_Crossings);
        params.put("OT_GroundAccess", mForm.OT_GroundAccess);
        params.put("OT_RoadMR", mForm.OT_RoadMR);
        params.put("OT_RoadRIA", mForm.OT_RoadRIA);
        params.put("OT_Comments", mForm.OT_Comments);

        params.put("Locations", Recorder.locationsToString(mForm.Locations));

        // Attach photos

        if(mForm.Photos != null){
            params.put("NumberOfImages", mForm.Photos.size());
            for(int i=0;i<mForm.Photos.size();i++){
                File file = new File(mForm.Photos.get(i).path);
                if(file.exists()){
                    try {

                        Photo photo = mForm.Photos.get(i);
                        params.put("Image"+i, file);
                        params.put("ImageName"+i, file.getName());
                        params.put("ImageFormType"+i, photo.formType);
                        params.put("ImageDesc"+i, photo.description);
                        params.put("ImageClass"+i, photo.classification);


                    }catch (Exception e){}
                }

            }
        }else{
            params.put("NumberOfImages", 0);
        }

        return params;
    }

    private void deleteFormIfSuccess(RoadForm mForm) {
        mFormController.deleteOneFormSync(mForm.ID);
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

}
