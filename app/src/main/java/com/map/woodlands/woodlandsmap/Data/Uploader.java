package com.map.woodlands.woodlandsmap.Data;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 3/13/2015.
 */
public class Uploader {
    public Form mForm;
    public Uploader(Form form){
        this.mForm = form;
    }

    public void execute(){
        uploadForm();
        uploadPhotos();

    }

    public void uploadPhotos(){
        List<String> photoPathes = new ArrayList<String>();
        photoPathes.add(mForm.PHOTO_INUP);
        photoPathes.add(mForm.PHOTO_INDW);
        photoPathes.add(mForm.PHOTO_OTUP);
        photoPathes.add(mForm.PHOTO_OTDW);
        photoPathes.add(mForm.PHOTO_1);
        photoPathes.add(mForm.PHOTO_2);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        for(int i=0;i<photoPathes.size();i++){
            String path = photoPathes.get(i);
            int j = i+1;
            String paramName = "image"+j;
            if(path != null){
                File mFile = new File(path);

                try{
                    params.put(paramName, mFile);
                }catch (FileNotFoundException e){}


            }
        }

        client.post("http://woodlandstest.azurewebsites.net/android/filepost", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.i("debug", "Photo upload success!");
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i("debug", "Photo upload failed!");
            }
        });


    }

    public void uploadForm(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try{
            params.put("INSP_DATE",mForm.INSP_DATE);
            params.put("INSP_CREW",mForm.INSP_CREW);
            params.put("ACCESS",mForm.ACCESS);
            params.put("CROSS_NM",mForm.CROSS_NM);
            params.put("CROSS_ID",mForm.CROSS_ID);
            params.put("LAT",mForm.LAT);
            params.put("LONG",mForm.LONG);
            params.put("STR_ID",mForm.STR_ID);
            params.put("STR_CLASS",mForm.STR_CLASS);
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
            params.put("FISH_PCONCREASON",mForm.FISH_PCONCREASON);
            params.put("REMARKS",mForm.REMARKS);
            params.put("PHOTO_INUP",mForm.PHOTO_INUP);
            params.put("PHOTO_INDW",mForm.PHOTO_INDW);
            params.put("PHOTO_OTUP",mForm.PHOTO_OTUP);
            params.put("PHOTO_OTDW",mForm.PHOTO_OTDW);
            params.put("PHOTO_1",mForm.PHOTO_1);
            params.put("PHOTO_2",mForm.PHOTO_2);
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
        client.post("http://woodlandstest.azurewebsites.net/android/formpost", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                Log.i("debug", "200 OK");
                String s = "Nothing";
                try {
                    s = new String(bytes, "UTF-8");
                }catch (Exception e){}
                Log.i("debug", s);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                Log.i("debug", "40X Fail");
                String s = "Nothing";
                try {
                    s = new String(bytes, "UTF-8");
                }catch (Exception e){}
            }
        });

    }


}
