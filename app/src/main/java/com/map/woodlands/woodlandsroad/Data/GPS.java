package com.map.woodlands.woodlandsroad.Data;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Jimmy on 6/8/2015.
 */
public class GPS implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    public Location mPreviouLocation;
    public GoogleApiClient mGoogleApiClient;
    public Recorder mRecorder;
    protected static final long timeInterval = 5000;
    protected static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(timeInterval)
            .setFastestInterval(16)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    public GPS(Activity activity){
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public static double calculateSpeed(Location one, Location two, long timeInterval){
        return calculateDistance(one, two) / timeInterval;
    }

    public static double calculateDistance(Location one, Location two){
        int R = 6371000;
        Double dLat = toRad(two.getLatitude() - one.getLatitude());
        Double dLon = toRad(two.getLongitude() - one.getLongitude());
        Double lat1 = toRad(one.getLatitude());
        Double lat2 = toRad(two.getLatitude());
        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double d = R * c;
        return d;

    }

    private static double toRad(Double d) {
        return d * Math.PI / 180;
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                REQUEST,
                this);  // LocationListener
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(mRecorder.isRecording){
            mRecorder.record(location);
        }else{
            mRecorder.print();
        }

        if(mPreviouLocation == null){
            // Initialize
            mPreviouLocation = new Location(location);

        }

        // Calculate
        double speed = calculateSpeed(mPreviouLocation, location, timeInterval);

        // update previous location to current location
        mPreviouLocation = new Location(location);

//        locationView.setText("(" + location.getLatitude() + ", " + location.getLongitude() + ") " + speed);
        if(speed > 0) {
            Log.i("debug", "(" + location.getLatitude() + ", " + location.getLongitude() + ") " + speed);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
