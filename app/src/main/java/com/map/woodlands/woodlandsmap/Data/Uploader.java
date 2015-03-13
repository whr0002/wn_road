package com.map.woodlands.woodlandsmap.Data;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Jimmy on 3/13/2015.
 */
public class Uploader {
    public Form mForm;
    public Uploader(Form form){
        this.mForm = form;
    }

    public void execute(){

        AsyncHttpClient client = new AsyncHttpClient();
        if(mForm.PHOTO_INUP != null){
            File mFile = new File(mForm.PHOTO_INUP);
            RequestParams params = new RequestParams();
            try{
                params.put("image1", mFile);
            }catch (FileNotFoundException e){}

            client.post("http://woodlandstest.azurewebsites.net/android/filepost", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    Log.i("debug", "File upload success!");
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.i("debug", "File upload failed!");
                }
            });
        }
    }
}
