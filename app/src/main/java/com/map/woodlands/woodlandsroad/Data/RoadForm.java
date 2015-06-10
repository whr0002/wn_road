package com.map.woodlands.woodlandsroad.Data;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 6/8/2015.
 */
public class RoadForm {
    public static final String FORM_TYPE = "ROAD";
    public static final String CLASS_PHOTO_GENERAL = "GENERAL";

    public int ID;
    public String Client;
    public String InspectorName;
    public String INSP_DATE;
    public String Licence;
    public String RoadName;
    public String DLO;
    public String KmFrom;
    public String KmTo;
    public String RoadStatus;
    public String StatusMatch;
    public String RS_Condition;
    public String RS_Notification;
    public String RS_RoadSurface;
    public String RS_GravelCondition;
    public String RS_VegetationCover;
    public String RS_CoverType;

    public String DI_Ditches;
    public String DI_VegetationCover;
    public String DI_CoverType;

    public String OT_Signage;
    public String OT_Crossings;
    public String OT_GroundAccess;
    public String OT_RoadMR;
    public String OT_RoadRIA;
    public String OT_Comments;

    public ArrayList<Photo> Photos;

    public List<Location> Locations;

    public ArrayList<String> messages;
    public String STATUS;

    public boolean isSelected;
}
