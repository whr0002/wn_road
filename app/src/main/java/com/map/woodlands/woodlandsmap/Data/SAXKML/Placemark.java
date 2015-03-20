package com.map.woodlands.woodlandsmap.Data.SAXKML;

/**
 * Created by Jimmy on 3/20/2015.
 */
public class Placemark {
    String title;
    String description;
    String coordinates;
    String address;
    public String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

}
