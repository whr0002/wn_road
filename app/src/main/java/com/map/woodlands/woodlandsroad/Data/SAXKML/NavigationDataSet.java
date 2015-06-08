package com.map.woodlands.woodlandsroad.Data.SAXKML;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Jimmy on 3/20/2015.
 */
public class NavigationDataSet {
    private ArrayList<Placemark> placemarks = new ArrayList<Placemark>();
    private Placemark currentPlacemark;
    private Placemark routePlacemark;


    public String toString() {

        String s= "\n";
        for (Iterator<Placemark> iter=placemarks.iterator();iter.hasNext();) {
            Placemark p = (Placemark)iter.next();
            s += p.getTitle() + "||" + p.getCoordinates() + "\n\n";
        }
        return s;
    }

    public void addCurrentPlacemark() {
        placemarks.add(currentPlacemark);
    }

    public ArrayList<Placemark> getPlacemarks() {
        return placemarks;
    }

    public void setPlacemarks(ArrayList<Placemark> placemarks) {
        this.placemarks = placemarks;
    }

    public Placemark getCurrentPlacemark() {
        return currentPlacemark;
    }

    public void setCurrentPlacemark(Placemark currentPlacemark) {
        this.currentPlacemark = currentPlacemark;
    }

    public Placemark getRoutePlacemark() {
        return routePlacemark;
    }

    public void setRoutePlacemark(Placemark routePlacemark) {
        this.routePlacemark = routePlacemark;
    }
}
