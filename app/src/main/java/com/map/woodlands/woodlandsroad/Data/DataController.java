package com.map.woodlands.woodlandsroad.Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.map.woodlands.woodlandsroad.Activities.ViewCrossingDataActivity;
import com.map.woodlands.woodlandsroad.Data.SAXKML.MapController;

import org.apache.http.Header;
import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Jimmy on 3/23/2015.
 * Used for getting data from server.
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


    private ProgressDialog progressDialog;

    public DataController(Context c, MapController mapController){
        this.context = c;
        this.client = new AsyncHttpClient();
        this.listType = new TypeToken<ArrayList<Coordinate>>(){}.getType();
        this.gson = new Gson();
        this.mapController = mapController;


        coordsUri = "http://scari.azurewebsites.net/androiddata/coordinates";
        rowUri = "http://scari.azurewebsites.net/androiddata/onerow";
        kmlUri = "http://scari.azurewebsites.net/androiddata/KMLs";
    }

    public void loadCoords(String dataUrl, final int type){
        UserInfo ui = getUserRole();

        if(ui != null) {
            progressDialog = ProgressDialog.show(context, "", "Loading...", true);
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
                    progressDialog.dismiss();
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
                    progressDialog.dismiss();

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

    public void loadRow(String ID){
        UserInfo ui = getUserRole();

        if(ui != null) {
            progressDialog = ProgressDialog.show(context, "", "Loading...", true);
            RequestParams params = new RequestParams();
            params.put("Email", ui.username);
            params.put("Password", ui.password);
            params.put("RememberMe", "false");
            params.put("Role", ui.role);
            params.put("ID", ID);
//            params.put("Latitude", ll.latitude);
//            params.put("Longitude", ll.longitude);
            client.post(rowUri, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try{
                        String json = new String(bytes);
//                        Log.i("debug", json);
                        Intent intent = new Intent(context, ViewCrossingDataActivity.class);
                        intent.putExtra("json", json);
                        context.startActivity(intent);

                    }catch (Exception e){
//                        e.printStackTrace();
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    progressDialog.dismiss();
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
//                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                    Toast.makeText(context, "Fail to get KML", Toast.LENGTH_SHORT).show();
                    Log.i("debug", "Getting KML failed");
                }
            });

        }
    }

    public void getClients(){
        String url = "http://scari.azurewebsites.net/androiddata/clients";
        UserInfo ui = getUserRole();

        if(ui != null){
            RequestParams params = new RequestParams();
            params.put("Email", ui.getUsername());
            params.put("Password", ui.getPassword());

            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try{
                        String json = new String(bytes);
                        JSONArray jsonArray = new JSONArray(json);

                        if(jsonArray != null){
                            SharedPreferences sp = context.getSharedPreferences("Data", 0);
                            SharedPreferences.Editor spEditor = sp.edit();

                            spEditor.putString("Clients", json);
                            spEditor.commit();

//                           for(int j=0; j<jsonArray.length();j++) {
//                               Log.i("debug", jsonArray.getString(j));
//
//                           }
                        }
                    }catch (Exception e){

                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

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
