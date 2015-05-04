package com.map.woodlands.woodlandsmap.Data;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.map.woodlands.woodlandsmap.R;

import java.util.HashMap;

/**
 * Created by Jimmy on 4/14/2015.
 * Used for the integration of Google Analytics
 */
public class MyApplication extends Application{

    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UX-XXXXXXXX-X";

    // Logging Tag
    private static final String TAG = "MyApp";

    public static int GENERAL_TRACKER = 0;

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg:
        // roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a
        // company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public MyApplication(){
        super();
    }

    public synchronized Tracker getTracker(TrackerName trackerId){
        if(!mTrackers.containsKey(trackerId)){
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);

            analytics.setDryRun(false);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);

            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics
                    .newTracker(R.xml.app_tracker)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics
                    .newTracker(PROPERTY_ID) : null;

            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }
}
