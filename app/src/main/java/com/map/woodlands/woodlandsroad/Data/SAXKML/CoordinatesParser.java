package com.map.woodlands.woodlandsroad.Data.SAXKML;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Jimmy on 3/20/2015.
 */
public class CoordinatesParser {


    public ArrayList<LatLng> getLatLngs(Placemark p){
        String cs = p.getCoordinates();
        ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
        if(cs != null && cs.length()>0){

            String[] coordinates = cs.trim().split(",|\\s+");
            String output = "";
            double lat = 0;
            double longitude = 0;


            for(int i=0;i<coordinates.length;i++){

                int j = i+1;
                if((j % 3) == 1){
                    // This is latitude
                    longitude = Double.parseDouble(coordinates[i]);
                }else if((j % 3) == 2){
                    // This is longitude
                    lat = Double.parseDouble(coordinates[i]);
                }else if((j % 3) ==  0){
                    // add lat and long
                    latLngs.add(new LatLng(lat, longitude));
                }
            }

//            Log.i("debug", "Lat: " + lat + " Long: " + longitude);

        }
        return latLngs;
    }
}
