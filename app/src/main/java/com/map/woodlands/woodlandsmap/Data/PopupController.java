package com.map.woodlands.woodlandsmap.Data;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.map.woodlands.woodlandsmap.Data.SAXKML.MapController;
import com.map.woodlands.woodlandsmap.R;

/**
 * Created by Jimmy on 3/23/2015.
 */
public class PopupController {

    private Context mContext;
    private final MapController mapController;
    private MarkerToggler mt;
    public PopupController(Context c, final MapController m, MarkerToggler mt){
        mContext = c;
        this.mapController = m;
        this.mt = mt;
    }

    public void showKMLPopup(View v){
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_layers, popupMenu.getMenu());
        Menu menu = popupMenu.getMenu();
        menu.add(1,0,0,"Crossings");
        menu.add(1,1,1,"Dispositions");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 0:
                        mapController.loadKML("http://woodlandsnorth.azurewebsites.net/Content/KML/Crossings.kml");
                        return true;

                    case 1:
                        mapController.loadKML("http://woodlandstest.azurewebsites.net/Content/KML/dispositions_kml.kml");
                        return true;
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
                        mapController.toggleAllMarkers(mt.isAll);
                        return true;

                    case 1:
                        mt.isHigh = !mt.isHigh;
                        mapController.toggleHighMarkers(mt.isHigh);
                        return true;

                    case 2:
                        mt.isMod = !mt.isMod;
                        mapController.toggleModMarkers(mt.isMod);
                        return true;


                    case 3:
                        mt.isLow = !mt.isLow;
                        mapController.toggleLowMarkers(mt.isLow);
                        return true;

                    case 4:
                        mt.isNo = !mt.isNo;
                        mapController.toggleNoMarkers(mt.isNo);
                        return true;
                }
                return true;
            }
        });

        popupMenu.show();

    }

}
