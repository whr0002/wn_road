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
import com.map.woodlands.woodlandsroad.Fragments.FormFragment;
import com.map.woodlands.woodlandsroad.R;

import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jimmy on 3/13/2015.
 * Used for uploading forms to the server
 */
public class Uploader {

    public Context mContext;
    private UserInfo user;
    private AsyncHttpClient client;

    private FormController mFormController;
    private String baseStorageUrl;
    private HashMap<String,String> mHashMap;

    private int total;
    private ProgressDialog progressDialog = null;
    private FormFragment mFormFragment;
    private ExecutorService executorService;

    private AtomicInteger counter = new AtomicInteger();
    private AtomicInteger successCounter = new AtomicInteger();


    public Uploader(Context context, FormFragment formFragment,
                    ProgressDialog p, int total){

        this.mContext = context;
        this.user = getUserInfo();
        this.client = new AsyncHttpClient();
        this.executorService = Executors.newFixedThreadPool(10);
        this.client.setThreadPool(executorService);


        this.mFormController = new FormController(mContext, formFragment);
        this.baseStorageUrl = mContext.getResources().getString(R.string.storage_url);


        this.total = total;
        this.progressDialog = p;
        this.mFormFragment = formFragment;




    }


    public void execute(Form f, int c){
        if(user != null) {
            this.mHashMap = new HashMap<String, String>();
            uploadPhotos(f, c);

            if(c == total){
                executorService.shutdown();

            }
        }
    }

    public void uploadPhotos(final Form mForm, final int c){
        List<String> photoPathes = new ArrayList<String>();
        photoPathes.add(mForm.PHOTO_INUP);
        photoPathes.add(mForm.PHOTO_INDW);
        photoPathes.add(mForm.PHOTO_OTUP);
        photoPathes.add(mForm.PHOTO_OTDW);
        photoPathes.add(mForm.PHOTO_1);
        photoPathes.add(mForm.PHOTO_2);
        photoPathes.add(mForm.PHOTO_ROAD_LEFT);
        photoPathes.add(mForm.PHOTO_ROAD_RIGHT);


        String s = mForm.AttachmentPath1;
        if(s != null && !s.equals("")){
//            Log.i("debug","added: "+s);
            photoPathes.add(s);
        }


        RequestParams params = new RequestParams();
        UserInfo ui = getUserInfo();

        if(ui != null){
            params.put("Email", ui.getUsername());
            params.put("Password", ui.getPassword());
            params.put("Role", ui.role);


            boolean hasFile = true;
            for(int i=0;i<photoPathes.size();i++){

                String path = photoPathes.get(i);
                int j = i+1;
                String paramName = "image"+j;

                if(path != null){
                    try{

                        File mFile = new File(path);
                        setRealPhotoUrl(mFile, ui.role, j);
                        params.put(paramName, mFile);

                    }catch (Exception e){
                        hasFile = false;

                    }
                }
            }
            if(!hasFile){
                progressDialog.dismiss();
                Toast.makeText(mContext, "Image file not found", Toast.LENGTH_SHORT).show();
            }else{

                setFormParams(params, mForm);

                client.post(mContext.getResources()
                                .getString(R.string.image_post_url)
                        , params,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                                int currentC = counter.incrementAndGet();
                                int totalSuccess = successCounter.incrementAndGet();

                                String msg = "Submitted "
                                        + totalSuccess
                                        + "/"
                                        + total;

                                String s = "";

                                try {
                                    s = new String(bytes, "UTF-8");
//                                    Log.i("debug", "Response: " + s);
                                }catch (Exception e){}


                                // Delete submitted form
                                if(s.contains("submitted")){

                                    deleteFormIfSuccess(mForm);

                                }

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
            }

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

    public void setFormParams(RequestParams params, Form mForm){
//        RequestParams params = new RequestParams();
        try{
            params.put("UserName", user.getUsername());
            params.put("Group", user.role);
            if(user.role.equals("super admin")){
                params.put("Client", mForm.Client);
            }else{
                params.put("Client", user.role);
            }

            params.put("INSP_DATE",mForm.INSP_DATE);
            params.put("TimeStamp", mForm.timestamp);
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

            params.put("PHOTO_INUP",mHashMap.get("1"));
            params.put("PHOTO_INDW",mHashMap.get("2"));
            params.put("PHOTO_OTUP",mHashMap.get("3"));
            params.put("PHOTO_OTDW",mHashMap.get("4"));
            params.put("PHOTO_1",mHashMap.get("5"));
            params.put("PHOTO_2",mHashMap.get("6"));
            params.put("PHOTO_ROAD_LEFT",mHashMap.get("7"));
            params.put("PHOTO_ROAD_RIGHT",mHashMap.get("8"));

            params.put("CULV_LEN",mForm.CULV_LEN);

            params.put("CULV_BACKWATERPROPORTION",mForm.CULV_BACKWATERPROPORTION);
            params.put("CULV_OUTLETTYPE",mForm.CULV_OUTLETTYPE);
            params.put("CULV_DIA_1",mForm.CULV_DIA_1_M);
            params.put("CULV_DIA_2",mForm.CULV_DIA_2_M);
            params.put("CULV_DIA_3",mForm.CULV_DIA_3_M);
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


            // addition 7 fields cst 1-3, csp 1-3, outlet score
            params.put("CULV_SUBSTYPE1", mForm.CULV_SUBSTYPE1);
            params.put("CULV_SUBSTYPE2", mForm.CULV_SUBSTYPE2);
            params.put("CULV_SUBSTYPE3", mForm.CULV_SUBSTYPE3);
            params.put("CULV_SUBSPROPORTION1", mForm.CULV_SUBSPROPORTION1);
            params.put("CULV_SUBSPROPORTION2", mForm.CULV_SUBSPROPORTION2);
            params.put("CULV_SUBSPROPORTION3", mForm.CULV_SUBSPROPORTION3);
            params.put("OUTLET_SCORE", mForm.outlet_score);

            // Put attachment Url
            params.put("ATTACHMENT",mHashMap.get("9"));

            params.put("CHANNEL_CREEK_DEPTH_LEFT", mForm.ChannelCreekDepthLeft);
            params.put("CHANNEL_CREEK_DEPTH_RIGHT", mForm.ChannelCreekDepthRight);
            params.put("CHANNEL_CREEK_DEPTH_CENTER", mForm.ChannelCreekDepthCenter);
            params.put("FIRST_RIFFLE_DISTANCE", mForm.FirstRiffleDistance);
            params.put("ROAD_FILL_ABOVE_CULVERT", mForm.RoadFillAboveCulvert);

        }catch (Exception e){
//            e.printStackTrace();
        }

    }

    private void deleteFormIfSuccess(Form mForm) {
        mFormController.deleteOneFormAsync(mForm.ID);
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
                return "Ephemeral";
            } else if (s.contains("non")) {
                return "Non-fluvial";
            } else if (s.contains("intermittent")) {
                return "Fluvial - intermittent";
            } else if (s.contains("small")) {
                return "Fluvial - small permanent";
            } else if (s.contains("large")) {
                return "Fluvial - large permanent";
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
