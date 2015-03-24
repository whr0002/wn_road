package com.map.woodlands.woodlandsmap.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.map.woodlands.woodlandsmap.Data.SAXKML.MapController;
import com.map.woodlands.woodlandsmap.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jimmy on 3/23/2015.
 */
public class PopupController {

    private Context mContext;
    private final MapController mapController;
    private MarkerToggler mt;
    private HashMap hm;

    public PopupController(Context c, final MapController m, MarkerToggler mt){
        mContext = c;
        this.mapController = m;
        this.mt = mt;
        hm = new HashMap();
    }

    public void showKMLPopup(View v){
        this.hm = getKML();

        PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_layers, popupMenu.getMenu());
        Menu menu = popupMenu.getMenu();

        Set set = hm.entrySet();
        Iterator i = set.iterator();
        int counter = 0;
        while(i.hasNext()){
            Map.Entry me = (Map.Entry)i.next();
            menu.add(1, counter, counter, me.getKey().toString());

            counter++;
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mapController.loadKML(""+hm.get(item.getTitle()), ""+item.getTitle());
                return true;
            }
        });

        popupMenu.show();

    }

    public void showControlPopup(View v){
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_layers, popupMenu.getMenu());
        Menu menu = popupMenu.getMenu();
        menu.add(1,0,0,"Show All");
        menu.add(1,1,1,"High Risk");
        menu.add(1,2,2,"Moderate Risk");
        menu.add(1,3,3,"Low Risk");
        menu.add(1,4,4,"No Risk");


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

}
