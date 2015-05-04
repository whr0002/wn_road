package com.map.woodlands.woodlandsmap.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.map.woodlands.woodlandsmap.Data.SAXKML.MapController;
import com.map.woodlands.woodlandsmap.R;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jimmy on 3/23/2015.
 * Used for handling the popups in Map Section
 */
public class PopupController {

    private Context mContext;
    private final MapController mapController;
    private MarkerToggler mt;
    private HashMap hm;
    private DataController dataController;
    private KMLController kmlController;

    public PopupController(Context c, final MapController m,
                           MarkerToggler mt, DataController d,
                           KMLController kmlController){
        mContext = c;
        this.mapController = m;
        this.mt = mt;
        hm = new HashMap();
        this.dataController = d;
        this.kmlController = kmlController;
    }

    public void showKMLPopup(View v){
        if(isOnline()) {
            this.hm = getKML();
        }else{
//            this.hm = kmlController.getLocalKMLsFromFolder();
            this.hm = kmlController.getLocalOverlaysFromFolder();
        }


        PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_layers, popupMenu.getMenu());
        Menu menu = popupMenu.getMenu();

        Set set = hm.entrySet();
        Iterator i = set.iterator();



        int counter = 0;
        menu.add(1, counter, counter, "Choose...");
        counter++;

        while(i.hasNext()){
            Map.Entry me = (Map.Entry)i.next();
            menu.add(1, counter, counter, me.getKey().toString());

            counter++;
        }

        menu.add(1, counter, counter, "Processed Data");
        counter++;
        menu.add(1, counter, counter, "Raw Data");
        counter++;
        menu.add(1, counter, counter, "Clear");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String title = item.getTitle().toString();
                if(title.equals("Processed Data")){
                    // Get processed data
                    String url = mContext.getString(R.string.coords_url);
                    if(url != null){
                        mt.isAll = true;
                        mt.isHigh = true;
                        mt.isMod = true;
                        mt.isLow = true;
                        mt.isNo = true;
                        dataController.loadCoords(url, 0);
                    }
                }else if(title.equals("Raw Data")){
                    // Get raw data
                    String url = mContext.getString(R.string.raw_data_url);
                    if(url != null) {
                        dataController.loadCoords(url, 1);
                    }
                }else if(title.equals("Clear")){
                    mapController.clear();
                }else if(title.equals("Choose...")){
                    // Popup a file chooser
                    openFileChooser();
                }else if(title.contains(".zip")){

                    kmlController.getZip(item.getTitle().toString(), hm.get(item.getTitle()).toString());
                }
                else{
                    kmlController.loadKML(""+hm.get(item.getTitle()), ""+item.getTitle());
                }
                return true;

            }
        });

        popupMenu.show();

    }

    public void showControlPopup(View v){
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_layers, popupMenu.getMenu());
        Menu menu = popupMenu.getMenu();
        menu.add(1,0,0,"Show All").setCheckable(true).setChecked(mt.isAll);
        menu.add(1,1,1,"High Risk").setCheckable(true).setChecked(mt.isHigh);
        menu.add(1,2,2,"Moderate Risk").setCheckable(true).setChecked(mt.isMod);
        menu.add(1,3,3,"Low Risk").setCheckable(true).setChecked(mt.isLow);
        menu.add(1,4,4,"No Risk").setCheckable(true).setChecked(mt.isNo);
//        menu.add(1,5,5,"Raw Data").setCheckable(true);


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 0:
                        mt.isAll = !mt.isAll;
                        if(!mt.isAll){
                            mt.isHigh = false;
                            mt.isLow = false;
                            mt.isMod = false;
                            mt.isNo = false;

                        }else{
                            mt.isHigh = true;
                            mt.isLow = true;
                            mt.isMod = true;
                            mt.isNo = true;

                        }

                        mapController.toggleMarkers(mt.isAll, " ");
                        return true;

                    case 1:
                        mt.isHigh = !mt.isHigh;
                        mapController.toggleMarkers(mt.isHigh, "high");
                        return true;

                    case 2:
                        mt.isMod = !mt.isMod;
                        mapController.toggleMarkers(mt.isMod, "mod");
                        return true;


                    case 3:
                        mt.isLow = !mt.isLow;
                        mapController.toggleMarkers(mt.isLow, "low");
                        return true;

                    case 4:
                        mt.isNo = !mt.isNo;
                        mapController.toggleMarkers(mt.isNo, "no");
                        return true;

                }
                return true;
            }
        });

        popupMenu.show();

    }

    public HashMap getKML(){
        HashMap h = new HashMap();
        SharedPreferences sp = mContext.getSharedPreferences("KMLData",0);
        String json = sp.getString("json","");
        if(!json.equals("")){
            Gson gson = new Gson();
            Type kmlListType = new TypeToken<ArrayList<KML>>(){}.getType();
            ArrayList<KML> kmls = new ArrayList<KML>();
            kmls = gson.fromJson(json, kmlListType);

            for(KML k : kmls){
                h.put(k.Name, k.Url);
//                Log.i("debug", k.Name + ": " + k.Url);
            }
        }

        return h;
    }

    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



    protected void openFileChooser() {
        String m_chosenDir = "";
        File f = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"");
        if(f.exists()){
            m_chosenDir = f.getAbsolutePath();
        }
        KMLFileChooser directoryChooserDialog =
                new KMLFileChooser(mContext,
                        new DirectoryChooserDialog.ChosenDirectoryListener(){
                            @Override
                            public void onChosenDir(String chosenDir) {
                                File file = new File(chosenDir);
                                if(file.exists()){
                                    String fName = file.getName().toLowerCase();
                                    String ext = fName.substring(fName.lastIndexOf("."));
                                    if(ext.contains(".kml")) {
                                        kmlController.loadLocalKML(file.getName());
                                    }else if(ext.contains(".zip")){
                                        // This is zip overlay, unzip it first
                                        kmlController.addOverlays(kmlController.unZip(file));
                                    }

                                }
                            }
                        });
        directoryChooserDialog.setNewFolderEnabled(false);
        directoryChooserDialog.chooseDirectory(m_chosenDir);
    }


}
