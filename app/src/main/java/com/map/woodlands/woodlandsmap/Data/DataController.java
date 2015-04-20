package com.map.woodlands.woodlandsmap.Data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.map.woodlands.woodlandsmap.Activities.ViewCrossingDataActivity;
import com.map.woodlands.woodlandsmap.Data.SAXKML.MapController;

import org.apache.http.Header;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Jimmy on 3/23/2015.
 */
public class DataController {

    private Context context;
    private AsyncHttpClient client;
    private String coordsUri;
    private String rowUri;
    private String kmlUri;
    private Type listType;

    private Gson gson;
    private MapController mapController;
    private ViewToggler mViewToggler;

    public DataController(Context c, MapController mapController, ViewToggler viewToggler){
        this.context = c;
        this.client = new AsyncHttpClient();
        this.listType = new TypeToken<ArrayList<Coordinate>>(){}.getType();
        this.gson = new Gson();
        this.mapController = mapController;
        this.mViewToggler = viewToggler;


//        Resources resources = context.getResources();
//        coordsUri = resources.getString(R.string.coords_url);
//        rowUri = resources.getString(R.string.row_url);
//        kmlUri = resources.getString(R.string.kml_url);
        coordsUri = "http://scari.azurewebsites.net/androiddata/coordinates";
        rowUri = "http://scari.azurewebsites.net/androiddata/onerow";
        kmlUri = "http://scari.azurewebsites.net/androiddata/KMLs";
    }

    public void loadCoords(String dataUrl, final int type){
        UserInfo ui = getUserRole();

        if(ui != null) {
            RequestParams params = new RequestParams();
            params.put("Email", ui.username);
            params.put("Password", ui.password);
            params.put("RememberMe", "false");
            params.put("Role", ui.role);
            client.post(dataUrl, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try{
                        String json = new String(bytes);
                        saveCoords(json, type);
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
//                    Log.i("debug", "failed");
                    Toast.makeText(context
                            , "Loading markers from local storage"
                            , Toast.LENGTH_SHORT).show();

                    String json = getCoordsFromLocal(type);
                    if(!json.equals("")){
                        ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
                        coords = gson.fromJson(json, listType);
                        mapController.loadAllMarkers(coords);
                    }else{
                        Toast.makeText(context
                                , "No available data in local storage"
                                ,Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }

    private void saveCoords(String json, int type){
        SharedPreferences sp = context.getSharedPreferences("Markers", 0);
        SharedPreferences.Editor spEditor = sp.edit();
        if(type == 0){
            spEditor.putString("processed", json);
            spEditor.commit();

        }else if(type == 1){
            spEditor.putString("raw", json);
            spEditor.commit();
        }
    }

    private String getCoordsFromLocal(int type){
        SharedPreferences sp = context.getSharedPreferences("Markers", 0);
        SharedPreferences.Editor spEditor = sp.edit();
        String json = "";
        if(type == 0){
            // get processed data
            json = sp.getString("processed", "");

        }else if(type == 1){
            // get raw data
            json = sp.getString("raw", "");
        }
        return json;
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
                        mViewToggler.toggleLoadingView();
                        String json = new String(bytes);
//                        Log.i("debug", json);
                        Intent intent = new Intent(context, ViewCrossingDataActivity.class);
                        intent.putExtra("json", json);
                        context.startActivity(intent);

                    }catch (Exception e){
//                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    mViewToggler.toggleLoadingView();
                    Toast.makeText(context, "Fail to get data", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void getKML(){
        UserInfo ui = getUserRole();

        if(ui != null) {
            RequestParams params = new RequestParams();
            params.put("Email", ui.username);
            params.put("Password", ui.password);
            params.put("RememberMe", "false");
            params.put("Role", ui.role);
            client.post(kmlUri, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try{
                        String json = new String(bytes);
//                        Log.i("debug", json);
                        // Parse JSON and Save it
                        SharedPreferences sp = context.getSharedPreferences("KMLData", 0);
                        SharedPreferences.Editor spEditor = sp.edit();
                        spEditor.putString("json", json);
                        spEditor.commit();


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                    Toast.makeText(mActivity.getApplicationContext(), "Fail to get KML", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private UserInfo getUserRole(){
        SharedPreferences sp = context.getSharedPreferences("UserInfo",0);
        String json = sp.getString("json", "");
        Gson gson = new Gson();
        UserInfo ui = null;
        if(!json.equals("")){
            ui = gson.fromJson(json, UserInfo.class);
        }

        return ui;
    }
}
