package com.map.woodlands.woodlandsroad.Activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.map.woodlands.woodlandsroad.Data.GPS;
import com.map.woodlands.woodlandsroad.Data.LayoutBuilder;
import com.map.woodlands.woodlandsroad.Data.Recorder;
import com.map.woodlands.woodlandsroad.R;

/**
 * Created by Jimmy on 6/5/2015.
 */
public class RoadInspectionActivity extends ActionBarActivity implements
        View.OnClickListener{

    private LinearLayout roadInfoLayout, ri_roadStatus, ri_statusMatch, rs_condition, rs_notification,
    rs_roadSurface, rs_gravelCondition, vc_roadSurface, vc_coverType, di_ditches, di_vc, di_vc_type,
            last_signage, last_crossings, last_groundAccess, last_roadMaintenanceRequired,
            last_roadImmediateAction, last_photos, last_comment;

    private ImageButton recordBtn;
    private Recorder recorder;
    private GPS gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_inspection);


        recordBtn = (ImageButton) findViewById(R.id.record_btn);
        recordBtn.setOnClickListener(this);
        recorder = new Recorder(this, recordBtn);
        gps = new GPS(this);
        gps.mRecorder = recorder;
        buildUI();


    }

    private void buildUI(){
        LinearLayout wrapper = (LinearLayout) findViewById(R.id.wrapper);
        roadInfoLayout = (LinearLayout) findViewById(R.id.roadInfoLayout);

        LayoutBuilder layoutBuilder = new LayoutBuilder(this);

        // Road Information / Location section
        ri_roadStatus = layoutBuilder.buildDropDown("Road Status", R.array.road_status);
        ri_statusMatch = layoutBuilder.buildDropDown("Status matches GIS System?", R.array.yes_no);
        roadInfoLayout.addView(ri_roadStatus);
        roadInfoLayout.addView(ri_statusMatch);

        // Road Surface section
        LinearLayout roadSurface = layoutBuilder.buildWrapper("Road Surface");
        roadSurface.setBackground(this.getResources().getDrawable(R.drawable.box2));
        wrapper.addView(roadSurface);
        rs_condition = layoutBuilder.buildDropDown("Condition (overall)", R.array.road_surface_condition);
        rs_notification = layoutBuilder.buildDropDown("Notification Required?", R.array.road_surface_notification);
        rs_roadSurface = layoutBuilder.buildDropDown("Road Surface", R.array.road_surface);
        rs_gravelCondition = layoutBuilder.buildDropDown("Gravel Condition", R.array.road_surface_gravel);
        vc_roadSurface = layoutBuilder.buildDropDown("Vegetation Cover", R.array.vc_road_surface);
        vc_coverType = layoutBuilder.buildDropDown("Cover Type", R.array.vc_cover_type);
        roadSurface.addView(rs_condition);
        roadSurface.addView(rs_notification);
        roadSurface.addView(rs_roadSurface);
        roadSurface.addView(rs_gravelCondition);
        roadSurface.addView(vc_roadSurface);
        roadSurface.addView(vc_coverType);

        // Ditches
        LinearLayout dichesSection = layoutBuilder.buildWrapper("Ditches");
        dichesSection.setBackground(this.getResources().getDrawable(R.drawable.box3));
        wrapper.addView(dichesSection);
        di_ditches = layoutBuilder.buildDropDown("Ditches", R.array.ditches);
        di_vc = layoutBuilder.buildDropDown("Vegetation Cover", R.array.vc_road_surface);
        di_vc_type = layoutBuilder.buildDropDown("Cover Type", R.array.vc_cover_type);
        dichesSection.addView(di_ditches);
        dichesSection.addView(di_vc);
        dichesSection.addView(di_vc_type);


        // Last Section
        LinearLayout lastSection = layoutBuilder.buildWrapper("Other Details");
        wrapper.addView(lastSection);

        last_signage = layoutBuilder.buildDropDown("Signage", R.array.last_signage);
        last_crossings = layoutBuilder.buildDropDown("Crossings", R.array.last_crossings);
        last_groundAccess = layoutBuilder.buildDropDown("Ground Access", R.array.last_ground_access);
        last_roadMaintenanceRequired = layoutBuilder.buildDropDown("Road maintainance required?", R.array.yes_no);
        last_roadImmediateAction = layoutBuilder.buildDropDown("Road requires immediate action*?", R.array.yes_no);

        lastSection.addView(last_signage);
        lastSection.addView(last_crossings);
        lastSection.addView(last_groundAccess);
        lastSection.addView(last_roadMaintenanceRequired);
        lastSection.addView(last_roadImmediateAction);




    }



    @Override
    protected void onResume() {
        super.onResume();
        gps.mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gps.mGoogleApiClient.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_btn:
                recorder.toggleRecordButton();
                break;

        }
    }
}
