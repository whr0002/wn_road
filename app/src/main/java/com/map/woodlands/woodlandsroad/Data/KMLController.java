package com.map.woodlands.woodlandsroad.Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.map.woodlands.woodlandsroad.Data.SAXKML.MapController;
import com.map.woodlands.woodlandsroad.Data.SAXKML.MapService;
import com.map.woodlands.woodlandsroad.Data.SAXKML.NavigationDataSet;

import org.apache.http.Header;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Created by Jimmy on 3/24/2015.
 * Used for adding kml and overlays
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

    public HashMap getLocalOverlaysFromFolder(){
        File dir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "overlays");
        HashMap hashMap = new HashMap();
        if(!dir.exists()){
            // Folder does not exist
            Toast.makeText(mContext,"No local overlays", Toast.LENGTH_LONG).show();
            return hashMap;
        }

        if(dir.isDirectory()){

            for(File f : dir.listFiles()){
                if(f.isFile()){
                    // Only accepting KML files in this folder
                    String fileName = f.getName();
                    String ext = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
                    if(ext.equals("zip")){
                        // It's is KML file
                        try {
                            hashMap.put(fileName, f.getCanonicalPath());
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
//                Log.i("debug", "File size: "+size);

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
//            Log.i("debug","back");
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

    public void addOverlays(File folder){
        if(folder != null && folder.exists()){
//            Toast.makeText(mContext, "Loading...", Toast.LENGTH_LONG).show();

            File[] files = folder.listFiles();
            for(File f : files){
//                Log.i("debug", f.getAbsolutePath());
                String fName = f.getName();
                String ext = fName.substring(fName.lastIndexOf(".")+1).toLowerCase();
                if(ext.equals("xml")){
                    // Parse .xml file to get bounds
                    LatLngBounds bounds = getBoundsFromXML(f);
                    if(bounds != null){
                        // Have bounds, get bitmap
                        try {
                            String imageName = f.getName().substring(0, f.getName().lastIndexOf("."));
                            String fullPath = folder.getAbsolutePath() + File.separator + imageName;
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                                    .fromPath(fullPath);

//                            Log.i("debug", "Image path: "+fullPath);

                            mapController.addOverlays(bitmapDescriptor, bounds);
                        }catch (Exception e){
                            Toast.makeText(mContext, "Cannot load image", Toast.LENGTH_LONG).show();
                        }

                    }

                }
            }
        }
    }


    private LatLngBounds getBoundsFromXML(File f) {
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(f);

            doc.getDocumentElement().normalize();

//            Log.i("debug", "Root element: " + doc.getDocumentElement().getNodeName());

            NodeList nodeList = doc.getElementsByTagName("GeoBndBox");

            for(int i=0;i<nodeList.getLength();i++){
                Node node = nodeList.item(i);

//                Log.i("debug", "Element: "+node.getNodeName());

                if(node.getNodeType() == node.ELEMENT_NODE){
                    Element element = (Element) node;

                    String southS = element.getElementsByTagName("southBL").item(0).getTextContent();
                    String westS = element.getElementsByTagName("westBL").item(0).getTextContent();
                    String northS = element.getElementsByTagName("northBL").item(0).getTextContent();
                    String eastS = element.getElementsByTagName("eastBL").item(0).getTextContent();

//                    Log.i("debug", "-south: "+ southS);
//                    Log.i("debug", "-westS: "+ westS);
//                    Log.i("debug", "-northS: "+ northS);
//                    Log.i("debug", "-eastS: "+ eastS);

                    double southD = Double.parseDouble(southS);
                    double westD = Double.parseDouble(westS);
                    double northD = Double.parseDouble(northS);
                    double eastD = Double.parseDouble(eastS);

                    return new LatLngBounds(new LatLng(southD,westD), new LatLng(northD,eastD));



                }


            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public File unZip(File file){
        byte[] buffer = new byte[1024];

        try{
            File overlayFolder = new File(mContext
                    .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"overlays");

            if(!overlayFolder.exists()){
                overlayFolder.mkdir();
            }

            File extractionFolder = new File(mContext
                    .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                    "overlays/extractions");

            if(!extractionFolder.exists()){
                extractionFolder.mkdir();
            }


            String fName = file.getName();
            String noExtName = fName.substring(0, fName.lastIndexOf("."));




            File folder = new File(mContext
                    .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                    "overlays/extractions/"+noExtName);

            if(!folder.exists()){
                folder.mkdir();
            }

//            Log.i("debug", "Folder Path: "+ folder.getAbsolutePath());

            // Get zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(file));

            // Get zipped file list entry
            ZipEntry ze = zis.getNextEntry();
            while(ze!=null){
                String fileName = ze.getName();
                File newFile = new File(folder.getAbsolutePath() + File.separator + fileName);
//                Log.i("debug", "Extracted file: " + newFile.getAbsolutePath());
                if(!ze.isDirectory()) {


                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len = 0;
                    while((len = zis.read(buffer)) > 0){
                        fos.write(buffer, 0, len);
                    }

                    fos.close();

                }else{
                    newFile.mkdir();
                }


                zis.closeEntry();
                ze = zis.getNextEntry();
            }

            zis.close();


            return folder;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void getZip(final String fileName, String url){
        progressDialog = ProgressDialog.show(mContext, "", "Loading...", true);

        UserInfo ui = getUserRole();
        if(ui.username != null && ui.password != null){
            RequestParams params = new RequestParams();
            params.put("Email", ui.username);
            params.put("Password", ui.password);
            params.put("fileName", fileName);

            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    File zip = saveZip(bytes, fileName);
                    addOverlays(unZip(zip));

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    // Cannot get zip, check the existence from local device
                    loadLocalZip(fileName);
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void loadLocalZip(String fileName){
        if(fileName != null) {
            File file = new File(mContext
                    .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                    "overlays/" + fileName);

            if(file.exists()){
                // Found the zip, load it.
                addOverlays(unZip(file));
            }else{
                Toast.makeText(mContext, "The overlay does not exist", Toast.LENGTH_LONG).show();
            }
        }
    }

    private File saveZip(byte[] bytes, String fileName){
        try{
            File dir = new File(mContext
                    .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                    "overlays");

            // Create directory
            if(!dir.exists()){
                dir.mkdir();
            }


            File zip = new File(dir.getAbsolutePath()+ File.separator +fileName);
            if(zip.exists()){
                zip.delete();
            }

            FileOutputStream fos = new FileOutputStream(zip);
            fos.write(bytes);
            fos.close();

            return zip;

        }catch (Exception e){

            Toast.makeText(mContext,"Errors when saving overlay", Toast.LENGTH_LONG).show();
        }

        return null;
    }

    private UserInfo getUserRole(){
        SharedPreferences sp = mContext.getSharedPreferences("UserInfo",0);
        String json = sp.getString("json", "");
        Gson gson = new Gson();
        UserInfo ui = null;
        if(!json.equals("")){
            ui = gson.fromJson(json, UserInfo.class);
        }

        return ui;
    }
}
