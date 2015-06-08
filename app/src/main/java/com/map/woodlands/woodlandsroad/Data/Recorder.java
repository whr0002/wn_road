package com.map.woodlands.woodlandsroad.Data;

import android.app.Activity;
import android.location.Location;
import android.util.Log;
import android.widget.ImageButton;

import com.map.woodlands.woodlandsroad.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 6/8/2015.
 */
public class Recorder {

    public List<Location> locations;
    private Activity mActivity;
    private ImageButton mImageBtn;
    public boolean isRecording = false;

    public Recorder (Activity activity, ImageButton recordBtn){
        mActivity = activity;
        mImageBtn = recordBtn;
        locations = new ArrayList<Location>();
    }

    public void toggleRecordButton(){
        if(isRecording){
            mImageBtn.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.file_submit));
            isRecording = false;
        }else{
            mImageBtn.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.record_stop));
            isRecording = true;
        }
    }

    public void record(Location location){
        if(locations.size()>0){
            Location lastLocation = locations.get(locations.size()-1);
            if(lastLocation.getLatitude() != location.getLatitude() ||
                    lastLocation.getLongitude() != location.getLongitude()
                    ){

                locations.add(location);
            }
        }else{
            locations.add(location);
        }

    }


    public void print(){
        Log.i("debug", "----Start");
        for(Location location : locations){
            Log.i("debug", "(" + location.getLatitude() + ", " + location.getLongitude() + ")");
        }
        Log.i("debug", "----Stop");
    }

}
