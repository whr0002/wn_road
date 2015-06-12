package com.map.woodlands.woodlandsroad.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.map.woodlands.woodlandsroad.Data.CameraHandler;
import com.map.woodlands.woodlandsroad.Data.GPS;
import com.map.woodlands.woodlandsroad.Data.ImageProcessor;
import com.map.woodlands.woodlandsroad.Data.IndexForm;
import com.map.woodlands.woodlandsroad.Data.LayoutBuilder;
import com.map.woodlands.woodlandsroad.Data.Photo;
import com.map.woodlands.woodlandsroad.Data.Recorder;
import com.map.woodlands.woodlandsroad.Data.RoadForm;
import com.map.woodlands.woodlandsroad.Data.RoadFormController;
import com.map.woodlands.woodlandsroad.Data.RoadFormValidator;
import com.map.woodlands.woodlandsroad.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jimmy on 6/5/2015.
 */
public class RoadInspectionActivity extends ActionBarActivity implements
        View.OnClickListener{

    private LinearLayout roadInfoLayout, ri_roadStatus, ri_statusMatch, rs_condition, rs_notification,
    rs_roadSurface, rs_gravelCondition, rs_vc, rs_vc_ct, di_ditches, di_vc, di_vc_type,
            last_signage, last_crossings, last_groundAccess, last_roadMaintenanceRequired,
            last_roadImmediateAction, last_photos, last_comment, imageGallery;
    private TextView dateView, latitudeView, longitudeView;
    private EditText inspectorEdit, licenceEdit, roadNameEdit, DLOEdit, kmFromEdit, kmToEdit, commentEdit;

    private CameraHandler cameraHandler;
    private ImageProcessor imageProcessor;
    private RoadFormController mFormController;

    private ImageButton recordBtn, addImageBtn;
    private Recorder recorder;
    private GPS gps;
    private int formID;
    private int formIndex;
    private RoadForm f;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_inspection);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        dateView = (TextView) findViewById(R.id.dateView);
        latitudeView = (TextView) findViewById(R.id.latitudeView);
        longitudeView = (TextView) findViewById(R.id.longitudeView);

        inspectorEdit = (EditText)findViewById(R.id.nameEdit);
        licenceEdit = (EditText)findViewById(R.id.licenceEdit);
        roadNameEdit = (EditText)findViewById(R.id.roadNameEdit);
        DLOEdit = (EditText)findViewById(R.id.dloEdit);
        kmFromEdit = (EditText)findViewById(R.id.kmFromEdit);
        kmToEdit = (EditText)findViewById(R.id.kmToEdit);
        commentEdit = (EditText) findViewById(R.id.commentEdit);

        addImageBtn = (ImageButton) findViewById(R.id.ad_image_add);
        addImageBtn.setOnClickListener(this);
        recordBtn = (ImageButton) findViewById(R.id.record_btn);
        recordBtn.setOnClickListener(this);
        recorder = new Recorder(this, recordBtn);
        gps = new GPS(this);
        gps.mRecorder = recorder;
        gps.latitudeView = latitudeView;
        gps.longitudeView = longitudeView;
        cameraHandler = new CameraHandler(this);
        imageProcessor = new ImageProcessor(gps.currentLocation);
        mFormController = new RoadFormController(this);

        imageGallery = (LinearLayout) findViewById(R.id.ad_image_gallery);

        buildUI();


        formID = getIntent().getIntExtra("ID", -1);

        if(formID == -1){
            // Create mode
            actionBar.setTitle("Create a form");
            setDateview();
        }else{
            // Edit mode
            actionBar.setTitle("Edit a form");
            setForm(formID);
        }

    }





    private void setDateview(){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        dateView.setText(date);
    }

    private void buildUI(){
        LinearLayout wrapper = (LinearLayout) findViewById(R.id.wrapper);
        roadInfoLayout = (LinearLayout) findViewById(R.id.roadInfoLayout);

        LayoutBuilder layoutBuilder = new LayoutBuilder(this);

        // Road Information / Location section
        ri_roadStatus = layoutBuilder.buildDropDown("Road Status", R.array.road_status, true);
        ri_statusMatch = layoutBuilder.buildDropDown("Status matches GIS System?", R.array.yes_no, false);
        roadInfoLayout.addView(ri_roadStatus);
        roadInfoLayout.addView(ri_statusMatch);

        // Road Surface section
        LinearLayout roadSurface = layoutBuilder.buildWrapper("Road Surface");
        roadSurface.setBackground(this.getResources().getDrawable(R.drawable.box2));
        wrapper.addView(roadSurface);
        rs_condition = layoutBuilder.buildDropDown("Condition (overall)", R.array.road_surface_condition, true);
        rs_notification = layoutBuilder.buildDropDown("Notification Required?", R.array.road_surface_notification, false);
        rs_roadSurface = layoutBuilder.buildDropDown("Road Surface", R.array.road_surface, true);
        rs_gravelCondition = layoutBuilder.buildDropDown("Gravel Condition", R.array.road_surface_gravel, false);
        rs_vc = layoutBuilder.buildDropDown("Vegetation Cover", R.array.vc_road_surface, true);
        rs_vc_ct = layoutBuilder.buildDropDown("Cover Type", R.array.vc_cover_type, false);
        roadSurface.addView(rs_condition);
        roadSurface.addView(rs_notification);
        roadSurface.addView(rs_roadSurface);
        roadSurface.addView(rs_gravelCondition);
        roadSurface.addView(rs_vc);
        roadSurface.addView(rs_vc_ct);

        // Ditches
        LinearLayout dichesSection = layoutBuilder.buildWrapper("Ditches");
        dichesSection.setBackground(this.getResources().getDrawable(R.drawable.box3));
        wrapper.addView(dichesSection);
        di_ditches = layoutBuilder.buildDropDown("Ditches", R.array.ditches, true);
        di_vc = layoutBuilder.buildDropDown("Vegetation Cover", R.array.vc_road_surface, true);
        di_vc_type = layoutBuilder.buildDropDown("Cover Type", R.array.vc_cover_type, false);
        dichesSection.addView(di_ditches);
        dichesSection.addView(di_vc);
        dichesSection.addView(di_vc_type);


        // Last Section
        LinearLayout lastSection = layoutBuilder.buildWrapper("Other Details");
        wrapper.addView(lastSection);

        last_signage = layoutBuilder.buildDropDown("Signage", R.array.last_signage, true);
        last_crossings = layoutBuilder.buildDropDown("Crossings", R.array.last_crossings, false);
        last_groundAccess = layoutBuilder.buildDropDown("Ground Access", R.array.last_ground_access, false);
        last_roadMaintenanceRequired = layoutBuilder.buildDropDown("Road maintainance required?", R.array.yes_no, true);
        last_roadImmediateAction = layoutBuilder.buildDropDown("Road requires immediate action*?", R.array.yes_no, false);

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

            case R.id.ad_image_add:
                cameraHandler.openCamera(RoadForm.FORM_TYPE, RoadForm.CLASS_PHOTO_GENERAL, imageGallery, CameraHandler.CAMERA_REQUEST_CODE, new ImageProcessor(gps.currentLocation));
                break;

        }
    }

    protected void showExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Discard");
        builder.setMessage("This form is not saved, are you sure you want to discard it?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cameraHandler.clearTempImages(cameraHandler.newCreatedPhotos);
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:

                showExitDialog();

                break;

            case R.id.save:
                Log.i("debug", "save");
                // Save form
                RoadForm form = generateForm();

                RoadFormValidator validator = new RoadFormValidator(form);
                validator.validateForm();

                if(formID == -1) {
                    mFormController.saveForm(form);
                }else{
                    mFormController.saveForm(formIndex, form);

                }

                finish();

                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        cameraHandler.onTakenPhoto(requestCode, resultCode, data, imageGallery, new ImageProcessor(gps.currentLocation));
    }

    /* Generate a form */
    public RoadForm generateForm(){
        int id = -1;
        if(formID == -1){
            id = mFormController.getNextFormID();
        }else{
            id = formID;
        }

        RoadForm f = new RoadForm();
        f.ID = id;
        f.InspectorName = inspectorEdit.getText().toString();
        f.INSP_DATE = dateView.getText().toString();
        f.Licence = licenceEdit.getText().toString();
        f.RoadName = roadNameEdit.getText().toString();
        f.DLO = DLOEdit.getText().toString();
        f.KmFrom = kmFromEdit.getText().toString();
        f.KmTo = kmToEdit.getText().toString();
        f.RoadStatus = getSpinnerItemFromLayout(ri_roadStatus);
        f.StatusMatch = getSpinnerItemFromLayout(ri_statusMatch);
        f.RS_Condition = getSpinnerItemFromLayout(rs_condition);
        f.RS_Notification = getSpinnerItemFromLayout(rs_notification);
        f.RS_RoadSurface = getSpinnerItemFromLayout(rs_roadSurface);
        f.RS_GravelCondition = getSpinnerItemFromLayout(rs_gravelCondition);
        f.RS_VegetationCover = getSpinnerItemFromLayout(rs_vc);
        f.RS_CoverType = getSpinnerItemFromLayout(rs_vc_ct);
        f.DI_Ditches = getSpinnerItemFromLayout(di_ditches);
        f.DI_VegetationCover = getSpinnerItemFromLayout(di_vc);
        f.DI_CoverType = getSpinnerItemFromLayout(di_vc_type);
        f.OT_Signage = getSpinnerItemFromLayout(last_signage);
        f.OT_Crossings = getSpinnerItemFromLayout(last_crossings);
        f.OT_GroundAccess = getSpinnerItemFromLayout(last_groundAccess);
        f.OT_RoadMR = getSpinnerItemFromLayout(last_roadMaintenanceRequired);
        f.OT_RoadRIA = getSpinnerItemFromLayout(last_roadImmediateAction);
        f.OT_Comments = commentEdit.getText().toString();
        f.Photos = cameraHandler.getCurrentPhotos();
        f.Locations = recorder.locations;



        return f;

    }

    private void setForm(int formID){
        IndexForm mif = mFormController.getIndexForm(formID);
        formIndex = mif.index;
        f = mif.roadForm;

        inspectorEdit.setText(f.InspectorName);
        dateView.setText(f.INSP_DATE);
        licenceEdit.setText(f.Licence);
        roadNameEdit.setText(f.RoadName);
        DLOEdit.setText(f.DLO);
        kmFromEdit.setText(f.KmFrom);
        kmToEdit.setText(f.KmTo);

        setSpinnerItemFromLayout(ri_roadStatus, f.RoadStatus);
        setSpinnerItemFromLayout(ri_statusMatch, f.StatusMatch);
        setSpinnerItemFromLayout(rs_condition, f.RS_Condition);
        setSpinnerItemFromLayout(rs_notification, f.RS_Notification);
        setSpinnerItemFromLayout(rs_roadSurface, f.RS_RoadSurface);
        setSpinnerItemFromLayout(rs_gravelCondition, f.RS_GravelCondition);
        setSpinnerItemFromLayout(rs_vc, f.RS_VegetationCover);
        setSpinnerItemFromLayout(rs_vc_ct, f.RS_CoverType);
        setSpinnerItemFromLayout(di_ditches, f.DI_Ditches);
        setSpinnerItemFromLayout(di_vc, f.DI_VegetationCover);
        setSpinnerItemFromLayout(di_vc_type, f.DI_CoverType);
        setSpinnerItemFromLayout(last_signage, f.OT_Signage);
        setSpinnerItemFromLayout(last_crossings, f.OT_Crossings);
        setSpinnerItemFromLayout(last_groundAccess, f.OT_GroundAccess);
        setSpinnerItemFromLayout(last_roadMaintenanceRequired, f.OT_RoadMR);
        setSpinnerItemFromLayout(last_roadImmediateAction, f.OT_RoadRIA);

        commentEdit.setText(f.OT_Comments);
        recorder.locations = f.Locations;

        if(f.Photos != null) {
            setGallery(f.Photos);
        }





    }

    private void setGallery(ArrayList<Photo> allPhotos) {

        cameraHandler.allPhotos = allPhotos;

        for(int i=0;i< allPhotos.size();i++){
            Photo photo = allPhotos.get(i);

            if(photo.classification.equals(RoadForm.CLASS_PHOTO_GENERAL)) {

                // Photos belong to NLF
                cameraHandler.addGalleryItem(i, allPhotos.get(i), imageGallery, cameraHandler.allPhotos, cameraHandler.removedPhotos);


            }
        }

    }

    private void setSpinnerItemFromLayout(LinearLayout layout, String value){
        for(int i=0;i<layout.getChildCount();i++){
            View v = layout.getChildAt(i);
            if(v instanceof Spinner){
                Spinner spinner = (Spinner)v;
                spinner.setSelection(getSpinnerIndexByName(spinner, value));
            }
        }


    }

    private String getSpinnerItemFromLayout(LinearLayout layout){

        for(int i=0;i<layout.getChildCount();i++){
            View v = layout.getChildAt(i);
            if(v instanceof Spinner){
                Spinner spinner = (Spinner)v;
                return spinner.getSelectedItem().toString();
            }
        }

        return null;
    }

    private int getSpinnerIndexByName(Spinner spinner, String value){
        for(int i=0;i<spinner.getCount();i++){
            if(spinner.getItemAtPosition(i).toString().equals(value)){
                return i;
            }
        }

        return 0;
    }
}
