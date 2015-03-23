package com.map.woodlands.woodlandsmap.Data;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.map.woodlands.woodlandsmap.Data.SAXKML.MapController;

import org.apache.http.Header;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Jimmy on 3/23/2015.
 */
public class DataController {

    private Activity mActivity;
    private AsyncHttpClient client;
    private final String coordsUri = "http://woodlandstest.azurewebsites.net/androiddata/coordinates";
    private final String rowUri = "http://woodlandstest.azurewebsites.net/androiddata/onerow";
    private Type listType;
    private Gson gson;
    private MapController mapController;

    public DataController(Activity a, MapController mapController){
        this.mActivity = a;
        this.client = new AsyncHttpClient();
        this.listType = new TypeToken<ArrayList<Coordinate>>(){}.getType();
        this.gson = new Gson();
        this.mapController = mapController;
    }
    public void loadCoords(){
        UserInfo ui = getUserRole();

        if(ui != null) {
            RequestParams params = new RequestParams();
            params.put("Email", ui.username);
            params.put("Password", ui.password);
            params.put("RememberMe", "false");
            params.put("Role", ui.role);
            client.post(coordsUri, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try{
                        String json = new String(bytes);
                        ArrayList<Coordinate> coords = new ArrayList<Coordinate>();

//                        Log.i("debug", json);

                        coords = gson.fromJson(json, listType);
                        mapController.loadAllMarkers(coords);

                    }catch (Exception e){
//                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                }
            });

        }

    }

    public void loadRow(LatLng ll){
        UserInfo ui = getUserRole();

        if(ui != null) {
            RequestParams params = new RequestParams();
            params.put("Email", ui.username);
            params.put("Password", ui.password);
            params.put("RememberMe", "false");
            params.put("Role", ui.role);
            params.put("Latitude", ll.latitude);
            params.put("Longitude", ll.longitude);
            client.post(rowUri, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try{
                        String json = new String(bytes);
                        Log.i("debug", json);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Toast.makeText(mActivity.getApplicationContext(), "Fail to get data", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private UserInfo getUserRole(){
        SharedPreferences sp = mActivity.getSharedPreferences("UserInfo",0);
        String json = sp.getString("json", "");
        Gson gson = new Gson();
        UserInfo ui = null;
        if(!json.equals("")){
            ui = gson.fromJson(json, UserInfo.class);
        }

        return ui;
    }
}
