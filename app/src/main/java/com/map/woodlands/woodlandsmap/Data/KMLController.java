package com.map.woodlands.woodlandsmap.Data;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.map.woodlands.woodlandsmap.Data.SAXKML.MapController;
import com.map.woodlands.woodlandsmap.Data.SAXKML.MapService;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


/**
 * Created by Jimmy on 3/24/2015.
 */
public class KMLController {

    private MapController mapController;
    private Context mContext;
    public KMLController(MapController mapController, Context context){
        this.mapController = mapController;
        this.mContext = context;
    }

    // Unzip KMZ, return KML in bytes
    public byte[] getKMLFromKMZ(byte[] bytes){


        return null;
    }

    public void loadLocalKML(String filename){
        if(filename != null){
//            String filePath = Environment
//                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    +"/kmls/"+filename;

            File dir = new File(mContext
                    .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    , "kmls");

            if(dir.exists()) {
                File file = new File(dir.getAbsolutePath() + "/" + filename);

                if (file.exists()) {
                    int size = (int) file.length();
                    byte[] bytes = new byte[size];

                    try {
                        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                        buf.read(bytes, 0, bytes.length);
                        buf.close();
                        mapController.addDataToMap(MapService.getNavigationDataSet(bytes));
                    } catch (Exception e) {
                        Toast.makeText(mContext
                                , "Exceptions when loading KML from local storage"
                                , Toast.LENGTH_SHORT)
                                .show();
                    }

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
}
