package com.map.woodlands.woodlandsmap.Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.map.woodlands.woodlandsmap.Data.SAXKML.MapController;
import com.map.woodlands.woodlandsmap.Data.SAXKML.MapService;
import com.map.woodlands.woodlandsmap.Data.SAXKML.NavigationDataSet;

import org.apache.http.Header;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;


/**
 * Created by Jimmy on 3/24/2015.
 */
public class KMLController {

    private MapController mapController;
    private Context mContext;
    private ProgressDialog progressDialog;
    private AsyncHttpClient client;
    public KMLController(MapController mapController, Context context){
        this.mapController = mapController;
        this.mContext = context;
        this.client = new AsyncHttpClient();
    }

    // Unzip KMZ, return KML in bytes
    public byte[] getKMLFromKMZ(byte[] bytes){


        return null;
    }

    public void loadLocalKML(String filename){

        if(filename != null){

            File dir = new File(mContext
                    .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    , "kmls");

            if(dir.exists()) {

                final File file = new File(dir.getAbsolutePath() + "/" + filename);

                if (file.exists()) {
                    progressDialog = ProgressDialog.show(mContext,"", "Loading...", true);
                    KMLReader kmlReader = new KMLReader();
                    kmlReader.execute(file);


                } else {
                    Toast.makeText(mContext
                            , "KML file does not exist in local storage"
                            , Toast.LENGTH_SHORT)
                            .show();
                }
            }else{
                Toast.makeText(mContext
                        , "KML file does not exist in local storage"
                        , Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    // Save KML or KMZ files to local storage
    public void saveFile(byte[] bytes, String filename){
//        Log.i("debug", filename);

//        String storagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/kmls";
        File dir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"kmls");

        if(!dir.exists()){
            dir.mkdir();
        }


        File kmlz = new File(dir.getAbsolutePath() + "/" + filename);

        if(kmlz.exists()){
            kmlz.delete();
        }

        try{
            FileOutputStream fos = new FileOutputStream(kmlz.getPath());
            fos.write(bytes);
            fos.close();
        }
        catch (Exception e){
            Log.i("debug", "Exception in creating kml file");
        }

    }

    public HashMap getLocalKMLsFromFolder(){
        File dir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "kmls");
        HashMap hashMap = new HashMap();
        if(!dir.exists()){
            // Folder does not exist
            Toast.makeText(mContext,"No local KML files", Toast.LENGTH_LONG).show();
            return hashMap;
        }

        if(dir.isDirectory()){

            for(File f : dir.listFiles()){
                if(f.isFile()){
                    // Only accepting KML files in this folder
                    String fileName = f.getName();
                    String ext = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
                    if(ext.equals("kml")){
                        // It's is KML file
                        try {
                            hashMap.put(fileName.substring(0,fileName.length()-4), f.getCanonicalPath());
                        }catch (Exception e){}
                    }
                }
            }
        }

        return hashMap;
    }

    public void loadKML(final String url, final String fileName){
        progressDialog = ProgressDialog.show(mContext, "", "Loading...", true);
        NavigationDataSet navigationDataSet = null;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                Log.i("debug", "Got KML file");

                // Save it to local storage
                String filetype = url.substring(url.length()-3);
                saveFile(bytes, fileName + "." + filetype);

                // Start loading to the map
                if(url.contains("kml")) {
                    // It is a kml file
                    mapController.addDataToMap(MapService.getNavigationDataSet(bytes));
                }else if(url.contains("kmz")){
                    // It is a kmz file
                    mapController.addDataToMap(MapService.getNavigationDataSet(getKMLFromKMZ(bytes)));

                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                Toast.makeText(mContext
                        ,"Network error, try to load KML file from local storage"
                        ,Toast.LENGTH_SHORT)
                        .show();

                String fullFilename = fileName + "." + url.substring(url.length()-3);
                loadLocalKML(fullFilename);

                progressDialog.dismiss();
            }
        });
    }

    class KMLReader extends AsyncTask<File,String, NavigationDataSet> {

        @Override
        protected NavigationDataSet doInBackground(File... files) {
            if(files[0] != null && files[0].exists()) {
                int size = (int) files[0].length();
                byte[] bytes = new byte[size];
                Log.i("debug", "File size: "+size);

                try {
                    BufferedInputStream buf = new BufferedInputStream(new FileInputStream(files[0]));
                    buf.read(bytes, 0, bytes.length);
                    buf.close();

                    return MapService.getNavigationDataSet(bytes);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(NavigationDataSet s) {
            Log.i("debug","back");
            if(s!= null) {
                try {
                    Log.i("debug", "Add data to map");
                    mapController.addDataToMap(s);
                } catch (Exception e) {
                    Log.i("debug", "Exceptions: Add data to map");
                    e.printStackTrace();
                    Toast.makeText(mContext
                            , "Exceptions when parsing KML"
                            , Toast.LENGTH_SHORT)
                            .show();

                }
            }else{
                Log.i("debug", "Data: null");
                Toast.makeText(mContext
                , "Exceptions when loading KML from local storage"
                , Toast.LENGTH_SHORT)
                .show();
            }

            progressDialog.dismiss();
        }
    }
}
