package com.map.woodlands.woodlandsroad.Data;

/**
 * Created by Jimmy on 5/19/2015.
 */
public class Photo {
    public static final String COLUMN_FORMTYPE = "FormType";
    public static final String COLUMN_FORMID = "FormID";
    public static final String COLUMN_PATH = "Path";
    public static final String COLUMN_DESC = "Description";
    public static final String COLUMN_CLASS = "Classification";

    public static final String TABLE_NAME = "Photo";

    public static final String TABLE_CREATE = "CREATE TABLE "+ TABLE_NAME
            + " ( "
            + COLUMN_PATH + " TEXT PRIMARY KEY, "
            + COLUMN_FORMTYPE + " TEXT NOT NULL, "
            + COLUMN_FORMID + " INTEGER NOT NULL, "
            + COLUMN_DESC + " TEXT, "
            + COLUMN_CLASS + " TEXT "
            + " );";

    public String formType;
    public int formId;
    public String path;
    public String description;
    public String classification;


}
