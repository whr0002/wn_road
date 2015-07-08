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

    public void record(Location location, double meters, double accuracy){

        // Record only if accuracy is less than or equal to 10
        if(accuracy > 0 && accuracy <= 10) {
            if (locations.size() > 0) {
                Location lastLocation = locations.get(locations.size() - 1);
                double distance = GPS.calculateDistance(lastLocation, location);

//                Log.i("debug", "Distance: " + distance);
                // Distance between previous location and current location must be within 500 meters
                if (distance > meters && distance < 500) {

                    locations.add(location);
                }
            } else {
                locations.add(location);
            }
        }

    }


    public void print(){
        Log.i("debug", "----Start");
        for(Location location : locations){
            Log.i("debug", "(" + location.getLatitude() + ", " + location.getLongitude() + ")");
        }
        Log.i("debug", "----Stop");
    }

    public static String locationsToString(List<Location> ls){
        String temp = "";

        if(ls != null) {
            for (Location location : ls) {
                temp += location.getLongitude() + "," + location.getLatitude() + ",0 ";
            }
        }

        return temp;
    }

}
