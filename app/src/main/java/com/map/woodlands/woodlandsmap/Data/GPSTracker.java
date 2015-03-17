package com.map.woodlands.woodlandsmap.Data;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Jimmy on 3/17/2015.
 */
public class GPSTracker implements LocationListener{
    private Context mContext;

    // Flag for GPS Status
    private boolean isGPSEnabled = false;
     // Flag for network status
    private boolean isNetworkEnabled = false;

    private boolean canGetLocation = false;

    private LocationManager lm;
    private Location location;
    private double latitude, longitude;

    public GPSTracker(Context context){
        this.mContext = context;
        lm = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);

        // Getting GPS status
        isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        setBestProvider();
        setLocation();
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {

        return latitude;
    }

    public void setBestProvider(){
        Criteria c = new Criteria();

        // Satellite -> Internet -> Sim network
        String provider = lm.getBestProvider(c, false);
        location = lm.getLastKnownLocation(provider);
        if(location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }else{
            latitude = 0.0;
            longitude = 0.0;
        }
    }

    public void setLocation(){
        if(isNetworkEnabled){
            // Network is available
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
//                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
        }else if(isGPSEnabled){
            // GPS is available
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
//                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }

        }else{
            latitude = 0.00;

            longitude = 0.00;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void removeUpdate(){
        lm.removeUpdates(this);
    }

}
