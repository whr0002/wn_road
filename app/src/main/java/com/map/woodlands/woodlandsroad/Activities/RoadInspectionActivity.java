package com.map.woodlands.woodlandsroad.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.LinearLayout;

import com.map.woodlands.woodlandsroad.Data.LayoutBuilder;
import com.map.woodlands.woodlandsroad.R;

/**
 * Created by Jimmy on 6/5/2015.
 */
public class RoadInspectionActivity extends ActionBarActivity {

    private LinearLayout roadInfoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_inspection);

        LinearLayout wrapper = (LinearLayout) findViewById(R.id.wrapper);
        roadInfoLayout = (LinearLayout) findViewById(R.id.roadInfoLayout);

        LayoutBuilder layoutBuilder = new LayoutBuilder(this);

        LinearLayout roadStatus = layoutBuilder.buildDropDown("Road Status", R.array.road_status_test);
        roadInfoLayout.addView(roadStatus);

        // Add view Road Surface
        LinearLayout roadSurface = layoutBuilder.buildWrapper("Road Surface");
        wrapper.addView(roadSurface);


    }

}
