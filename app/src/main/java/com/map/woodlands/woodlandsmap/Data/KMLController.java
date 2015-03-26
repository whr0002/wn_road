package com.map.woodlands.woodlandsmap.Data;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Jimmy on 3/24/2015.
 */
public class KMLController {


    // Unzip KMZ, return KML in bytes
    public byte[] getKMLFromKMZ(byte[] bytes){


        return null;
    }

    public void loadFile(String filename){

    }
    // Save KML or KMZ files to local storage
    public void saveFile(byte[] bytes, String filename){
        Log.i("debug", filename);

        String storagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/kmls";
        File dir = new File(storagePath);

        if(!dir.exists()){
            dir.mkdir();
        }


        File kmlz = new File(storagePath + "/" + filename);

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
