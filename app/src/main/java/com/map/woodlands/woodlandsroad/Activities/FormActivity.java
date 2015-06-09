package com.map.woodlands.woodlandsroad.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.map.woodlands.woodlandsroad.Data.DirectoryChooserDialog;
import com.map.woodlands.woodlandsroad.Data.Form;
import com.map.woodlands.woodlandsroad.Data.FormController;
import com.map.woodlands.woodlandsroad.Data.FormValidator;
import com.map.woodlands.woodlandsroad.Data.ImageProcessor_Old;
import com.map.woodlands.woodlandsroad.Data.MyApplication;
import com.map.woodlands.woodlandsroad.Data.UserInfo;
import com.map.woodlands.woodlandsroad.Data.ViewToggler;
import com.map.woodlands.woodlandsroad.R;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jimmy on 3/11/2015.
 * Used for creating a form
 */
public class FormActivity extends ActionBarActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    static final int DATE_DIALOG_ID = 999;

    public GoogleApiClient mGoogleApiClient;
    public Location location;
    protected static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)
            .setFastestInterval(16)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    public int year;
    public int month;
    public int day;
    public String formCreatedTime;
    public String mCurrentPhotoPath;
    public int mCurrentRequestCode;
    public HashMap<Integer, String> mPhotoMap;
    public HashMap<Integer, String> mTempPhotoMap;

    public FormController mFormController;

    public ImageButton dateButton;

    public TextView dateView,latitudeView,longitudeView,attachmentName;

    public EditText inspectionCrewView,crossingNumberView,crossingIDView,streamIDView,dispositionIDView,
            streamWidthView,erosionAreaView,culvertLengthView,culvertDiameter1View,
            culvertDiameter2View,culvertDiameter3View,culvertPoolDepthView,culvertOutletGapView,
            bridgeLengthView,fishPassageConvernsReasonView,blockageMaterialView,
            blockageCauseView,remarksView, channelCreekDepthLeftView, channelCreekDepthRightView,
            channelCreekDepthCenterView, firstRiffleDistanceView, roadFillAboveCulvertView;


    public Spinner accessSpinner,streamClassificationSpinner,streamWidthMeasuredSpinner,crossingTypeSpinner,erosionSpinner,
            erosionType1Spinner,erosionSourceSpinner,erosionDegreeSpinner,culvertSubstrateSpinner,
            culvertBackWaterProportionSpinner,culvertSlopeSpinner,culvertOutletTypeSpinner,scourPoolPresentSpinner,
            delineatorSpinner,hazardMarkersSpinner,approachSignageSpinner,roadSurfaceSpinner,approachRailsSpinner,
            roadDrainageSpinner,visibilitySpinner,wearingSurfaceSpinner,railCurbSpinner,girdersBracingSpinner,
            capBeamSpinner,pilesSpinner,abutmentWallSpinner,wingWallSpinner,bankStabilitySpinner,slopeProtectionSpinner,
            channelOpeningSpinner,obstructionsSpinner,fishSamplingSpinner,fishSamplingMethod,fishSamplingSpecies1Spinner,
            fishSamplingSpecies2Spinner,fishPassageConcernsSpinner,emergencyRepairRequiredSpinner,structuralProblemsSpinner,
            sedimentationSpinner, blockageSpinner, fishReasonSpinner, cst1Spinner, cst2Spinner, cst3Spinner, csp1Spinner,
            csp2Spinner, csp3Spinner, clientSpinner;

    public ImageView photoView1,photoView2,photoView3,photoView4,photoView5,photoView6, photoView7, photoView8;

    public LinearLayout culvertBlock,bridgeBlock,erosionBlock,fishSamplingBlock, blockageBlock,
            culvertDiameter2Block, culvertDiameter3Block, fishReasonBlock, clientBlock ;

    public ImageButton attachmentButton, cancelAttachmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        this.mFormController = new FormController(this.getApplicationContext());
        this.mPhotoMap = new HashMap<Integer, String>();
        this.mTempPhotoMap = new HashMap<Integer, String>();

        setView();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    protected void setView() {
        setCurrentDateView();
        setDatePicker();
        setTextViews();
        setSpinners();
        setLayouts();
        setImageViews();
        setLatLong();
        setImageButtons();
    }

    protected void setImageButtons() {
        this.attachmentButton = (ImageButton) findViewById(R.id.attachmentButton);
        this.cancelAttachmentButton = (ImageButton) findViewById(R.id.cancel_attachment);

        attachmentButton.setOnClickListener(this);
        cancelAttachmentButton.setOnClickListener(this);
    }

    protected void setLatLong() {
//        GPSTracker gpsTracker = new GPSTracker(this.getApplicationContext());
//        latitudeView.setText(""+gpsTracker.getLatitude());
//        longitudeView.setText(""+gpsTracker.getLongitude());
    }

    public void setTextViews() {
        latitudeView = (TextView)findViewById(R.id.latText);
        longitudeView = (TextView)findViewById(R.id.longText);
        attachmentName = (TextView)findViewById(R.id.attachmentName);

        inspectionCrewView = (EditText)findViewById(R.id.crewText);
        crossingNumberView = (EditText)findViewById(R.id.crossingNumberText);
        crossingIDView = (EditText)findViewById(R.id.crossingIDText);
        streamIDView = (EditText)findViewById(R.id.streamIDText);
        dispositionIDView = (EditText)findViewById(R.id.dispositionIDText);
        streamWidthView = (EditText)findViewById(R.id.streamWidthText);
        erosionAreaView = (EditText)findViewById(R.id.erosionAreaText);
        culvertLengthView = (EditText)findViewById(R.id.culvertLengthText);
        culvertDiameter1View = (EditText)findViewById(R.id.culvertDiameter1Text);
        culvertDiameter2View = (EditText)findViewById(R.id.culvertDiameter2Text);
        culvertDiameter3View = (EditText)findViewById(R.id.culvertDiameter3Text);
        culvertPoolDepthView = (EditText)findViewById(R.id.culvertPoolDepthText);
        culvertOutletGapView = (EditText)findViewById(R.id.culvertOutletGapText);
        bridgeLengthView = (EditText)findViewById(R.id.bridgeLengthText);
        fishPassageConvernsReasonView = (EditText)findViewById(R.id.fishPassageConcernsReasonText);
        blockageMaterialView = (EditText)findViewById(R.id.blockageMaterialText);
        blockageCauseView = (EditText)findViewById(R.id.blockageCauseText);
        remarksView = (EditText)findViewById(R.id.remarksText);

        channelCreekDepthLeftView = (EditText)findViewById(R.id.channelCreekDepthLeftText);
        channelCreekDepthRightView = (EditText)findViewById(R.id.channelCreekDepthRightText);
        channelCreekDepthCenterView = (EditText)findViewById(R.id.channelCreekDepthCenterText);
        firstRiffleDistanceView = (EditText)findViewById(R.id.firstRiffleDistanceText);
        roadFillAboveCulvertView = (EditText)findViewById(R.id.roadFillAboveCulvertText);


        attachmentName.setOnClickListener(this);
    }

    public void setSpinners() {
        accessSpinner = (Spinner)findViewById(R.id.accessDropdown);
        streamClassificationSpinner = (Spinner)findViewById(R.id.streamClassDropdown);
        streamWidthMeasuredSpinner = (Spinner)findViewById(R.id.streamMeasuredDropdown);
        crossingTypeSpinner = (Spinner)findViewById(R.id.crossingTypeDropdown);
        erosionSpinner = (Spinner)findViewById(R.id.erosionDropdown);
        erosionType1Spinner = (Spinner)findViewById(R.id.erosionType1Dropdown);
        erosionSourceSpinner = (Spinner)findViewById(R.id.erosionSourceDropdown);
        erosionDegreeSpinner = (Spinner)findViewById(R.id.erosionDegreeDropdown);
        culvertSubstrateSpinner = (Spinner)findViewById(R.id.culvertSubstrateDropdown);

        culvertBackWaterProportionSpinner = (Spinner)findViewById(R.id.culvertBWProportionDropdown);
        culvertSlopeSpinner = (Spinner)findViewById(R.id.culvertSlopeDropdown);
        culvertOutletTypeSpinner = (Spinner)findViewById(R.id.culvertOutletTypeDropdown);
        scourPoolPresentSpinner = (Spinner)findViewById(R.id.scourPoolPresentDropdown);
        delineatorSpinner = (Spinner)findViewById(R.id.delineatorDropdown);
        hazardMarkersSpinner = (Spinner)findViewById(R.id.hazardMarkersDropdown);
        approachSignageSpinner = (Spinner)findViewById(R.id.approachSignageDropdown);
        roadSurfaceSpinner = (Spinner)findViewById(R.id.roadSurfaceDropdown);
        approachRailsSpinner = (Spinner)findViewById(R.id.approachRailsDropdown);
        roadDrainageSpinner = (Spinner)findViewById(R.id.roadDrainageDropdown);
        visibilitySpinner = (Spinner)findViewById(R.id.visibilityDropdown);
        wearingSurfaceSpinner = (Spinner)findViewById(R.id.wearingSurfaceDropdown);
        railCurbSpinner = (Spinner)findViewById(R.id.railCurbDropdown);
        girdersBracingSpinner = (Spinner)findViewById(R.id.girdersBracingDropdown);
        capBeamSpinner = (Spinner)findViewById(R.id.capBeamDropdown);
        pilesSpinner = (Spinner)findViewById(R.id.pilesDropdown);
        abutmentWallSpinner = (Spinner)findViewById(R.id.abutmentWallDropdown);
        wingWallSpinner = (Spinner)findViewById(R.id.wingWallDropdown);
        bankStabilitySpinner = (Spinner)findViewById(R.id.bankStabilityDropdown);
        slopeProtectionSpinner = (Spinner)findViewById(R.id.slopeProtectionDropdown);
        channelOpeningSpinner = (Spinner)findViewById(R.id.channelOpeningDropdown);
        obstructionsSpinner = (Spinner)findViewById(R.id.obstructionsDropdown);
        fishSamplingSpinner = (Spinner)findViewById(R.id.fishSamplingDropdown);
        fishSamplingMethod = (Spinner)findViewById(R.id.fishSamplingMethodDropdown);
        fishSamplingSpecies1Spinner = (Spinner)findViewById(R.id.fishSamplingSpecies1Dropdown);
        fishSamplingSpecies2Spinner = (Spinner)findViewById(R.id.fishSamplingSpecies2Dropdown);
        fishPassageConcernsSpinner = (Spinner)findViewById(R.id.fishPassageConcernsDropdown);
        emergencyRepairRequiredSpinner = (Spinner)findViewById(R.id.emergencyRepairRequiredDropdown);
        structuralProblemsSpinner = (Spinner)findViewById(R.id.structuralProblemsDropdown);
        sedimentationSpinner = (Spinner)findViewById(R.id.sedimentationDropdown);
        blockageSpinner = (Spinner)findViewById(R.id.blockageDropdown);
        fishReasonSpinner = (Spinner)findViewById(R.id.fishReasonDropdown);
        cst1Spinner = (Spinner)findViewById(R.id.cst1Dropdown);
        cst2Spinner = (Spinner)findViewById(R.id.cst2Dropdown);
        cst3Spinner = (Spinner)findViewById(R.id.cst3Dropdown);
        csp1Spinner = (Spinner)findViewById(R.id.csp1Dropdown);
        csp2Spinner = (Spinner)findViewById(R.id.csp2Dropdown);
        csp3Spinner = (Spinner)findViewById(R.id.csp3Dropdown);

        clientSpinner = (Spinner)findViewById(R.id.clientDropdown);
        setClientSpinner(clientSpinner);


        setSpinnerAdapter(accessSpinner, R.array.accessItems);
        setSpinnerAdapter(streamClassificationSpinner, R.array.streamClassItems2);
        setSpinnerAdapter(streamWidthMeasuredSpinner, R.array.streamMeasuredItems);
        setSpinnerAdapter(crossingTypeSpinner, R.array.crossingTypeItems);
        setSpinnerAdapter(erosionSpinner, R.array.erosionItems);
        setSpinnerAdapter(erosionType1Spinner, R.array.erosionType1Items);
        setSpinnerAdapter(erosionSourceSpinner, R.array.erosionSourceItems);
        setSpinnerAdapter(erosionDegreeSpinner, R.array.erosionDegreeItems);
        setSpinnerAdapter(culvertSubstrateSpinner, R.array.culvertSubstrateItems);

        setSpinnerAdapter(culvertBackWaterProportionSpinner, R.array.culvertBWProportionItems);
        setSpinnerAdapter(culvertSlopeSpinner, R.array.culvertSlopeItems);
        setSpinnerAdapter(culvertOutletTypeSpinner, R.array.culvertOutletTypeItems);
        setSpinnerAdapter(scourPoolPresentSpinner, R.array.scourPoolPresentItems);
        setSpinnerAdapter(delineatorSpinner, R.array.delineatorItems);
        setSpinnerAdapter(hazardMarkersSpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(approachSignageSpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(roadSurfaceSpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(approachRailsSpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(roadDrainageSpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(visibilitySpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(wearingSurfaceSpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(railCurbSpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(girdersBracingSpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(capBeamSpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(pilesSpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(abutmentWallSpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(wingWallSpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(bankStabilitySpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(slopeProtectionSpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(channelOpeningSpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(obstructionsSpinner, R.array.hazardMarkersItems);
        setSpinnerAdapter(fishSamplingSpinner, R.array.fishSamplingItems);
        setSpinnerAdapter(fishSamplingMethod, R.array.fishSamplingMethodItems);
        setSpinnerAdapter(fishSamplingSpecies1Spinner, R.array.fishSamplingSpecies1Items);
        setSpinnerAdapter(fishSamplingSpecies2Spinner, R.array.fishSamplingSpecies1Items);
        setSpinnerAdapter(fishPassageConcernsSpinner, R.array.fishPassageConcernsItems);
        setSpinnerAdapter(emergencyRepairRequiredSpinner, R.array.emergencyRepairRequiredItems);
        setSpinnerAdapter(structuralProblemsSpinner, R.array.structuralProblemsItems);
        setSpinnerAdapter(sedimentationSpinner, R.array.sedimentationItems);
        setSpinnerAdapter(blockageSpinner, R.array.emergencyRepairRequiredItems);
        setSpinnerAdapter(fishReasonSpinner, R.array.fishReasonItems);
        setSpinnerAdapter(cst1Spinner, R.array.culvertSubstrateTypeItems);
        setSpinnerAdapter(cst2Spinner, R.array.culvertSubstrateTypeItems);
        setSpinnerAdapter(cst3Spinner, R.array.culvertSubstrateTypeItems);
        setSpinnerAdapter(csp1Spinner, R.array.culvertSubstrateProportionItems);
        setSpinnerAdapter(csp2Spinner, R.array.culvertSubstrateProportionItems);
        setSpinnerAdapter(csp3Spinner, R.array.culvertSubstrateProportionItems);

        crossingTypeSpinner.setOnItemSelectedListener(this);
        erosionSpinner.setOnItemSelectedListener(this);
        fishSamplingSpinner.setOnItemSelectedListener(this);
        blockageSpinner.setOnItemSelectedListener(this);
        fishPassageConcernsSpinner.setOnItemSelectedListener(this);
    }

    public void setSpinnerAdapter(Spinner spinner, int arrayID){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayID, R.layout.spinner_item);
        if(arrayID == R.array.fishSamplingSpecies1Items){
            adapter.sort(new Comparator<CharSequence>() {
                @Override
                public int compare(CharSequence lhs, CharSequence rhs) {

                    return lhs.toString().compareTo(rhs.toString());
                }
            });
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void setClientSpinner(Spinner s){
        SharedPreferences sp = this.getSharedPreferences("Data", 0);
        String json = sp.getString("Clients", "");

        ArrayList<CharSequence> arrayList = new ArrayList<CharSequence>();
        arrayList.add("");

        if(!json.equals("")){
            try{
                JSONArray jsonArray = new JSONArray(json);

                for(int i=0;i<jsonArray.length();i++){
                    arrayList.add(jsonArray.getString(i));
                }

            }catch (Exception e){

            }
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this
                ,R.layout.spinner_item,
                arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

    public void setLayouts() {
        culvertBlock = (LinearLayout)findViewById(R.id.culvertBlock);
        bridgeBlock = (LinearLayout)findViewById(R.id.bridgeBlock);
        erosionBlock = (LinearLayout)findViewById(R.id.erosionBlock);
        fishSamplingBlock = (LinearLayout)findViewById(R.id.fishSamplingBlock);
        blockageBlock = (LinearLayout)findViewById(R.id.blockageBlock);
        culvertDiameter2Block = (LinearLayout)findViewById(R.id.culvertD2Layout);
        culvertDiameter3Block = (LinearLayout)findViewById(R.id.culvertD3Layout);
        fishReasonBlock = (LinearLayout)findViewById(R.id.fishReasonLayout);

        clientBlock = (LinearLayout)findViewById(R.id.clientLayout);
        setClientBlock(clientBlock);
    }

    private void setClientBlock(LinearLayout layout){
        UserInfo ui = getUserInfo();

        if(ui != null){
            if(ui.role.equals("super admin")){
                layout.setVisibility(View.VISIBLE);
            }
        }
    }

    public UserInfo getUserInfo(){
        SharedPreferences sp = this.getSharedPreferences("UserInfo", 0);
        String json = sp.getString("json","");

        if(!json.equals("")){
            Gson gson = new Gson();
            UserInfo user;
            user = gson.fromJson(json, UserInfo.class);
            return user;
        }
        return null;
    }

    protected void setImageViews() {
        photoView1 = (ImageView) findViewById(R.id.inlet1);
        photoView2 = (ImageView) findViewById(R.id.inlet2);
        photoView3 = (ImageView) findViewById(R.id.outlet1);
        photoView4 = (ImageView) findViewById(R.id.outlet2);
        photoView5 = (ImageView) findViewById(R.id.other1);
        photoView6 = (ImageView) findViewById(R.id.other2);
        photoView7 = (ImageView) findViewById(R.id.roadLeft);
        photoView8 = (ImageView) findViewById(R.id.roadRight);


        photoView1.setOnClickListener(this);
        photoView2.setOnClickListener(this);
        photoView3.setOnClickListener(this);
        photoView4.setOnClickListener(this);
        photoView5.setOnClickListener(this);
        photoView6.setOnClickListener(this);
        photoView7.setOnClickListener(this);
        photoView8.setOnClickListener(this);

    }

    protected void setDatePicker() {
        dateButton = (ImageButton) findViewById(R.id.date_picker);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    protected void setCurrentDateView() {
        dateView = (TextView) findViewById(R.id.dateView);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        dateView.setText(new StringBuilder()
                .append(month + 1).append("/").append(day)
                .append("/").append(year));


        // Get timestamp
        formCreatedTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
//        formCreatedTime = DateFormat.getTimeInstance().format(new Date());

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case DATE_DIALOG_ID:

                return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }

    protected DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener(){

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay){
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Set selected date to date view
            dateView.setText(new StringBuilder()
                    .append(month + 1).append("/").append(day)
                    .append("/").append(year));
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                // Discard this form, open confirmation box
                showExitDialog();
                return true;

            case R.id.save:
                // Save form
                Form form = generateForm();
                validateForms(form);

                mFormController.saveForm(form);
                finish();

                return true;



            default:
                return super.onOptionsItemSelected(item);
        }

    }



    /* Generate a new form */
    public Form generateForm(){

        int id = mFormController.getNextFormID();

        Form f = new Form();
        f.ID = id;
        f.INSP_DATE = dateView.getText().toString();
        f.Client = clientSpinner.getSelectedItem().toString();
        f.INSP_CREW = inspectionCrewView.getText().toString();
        f.ACCESS = accessSpinner.getSelectedItem().toString();
        f.CROSS_NM = crossingNumberView.getText().toString();
        f.CROSS_ID = crossingIDView.getText().toString();
        f.STR_ID = streamIDView.getText().toString();
        f.DISPOSITION_ID = dispositionIDView.getText().toString();
        f.LAT = latitudeView.getText().toString();
        f.LONG = longitudeView.getText().toString();
        f.STR_CLASS = streamClassificationSpinner.getSelectedItem().toString();
        f.STR_WIDTH = streamWidthView.getText().toString();
        f.STR_WIDTHM = streamWidthMeasuredSpinner.getSelectedItem().toString();
        f.CROSS_TYPE = crossingTypeSpinner.getSelectedItem().toString();
        f.EROSION = erosionSpinner.getSelectedItem().toString();
        f.EROSION_TY1 = erosionType1Spinner.getSelectedItem().toString();
        f.EROSION_SO = erosionSourceSpinner.getSelectedItem().toString();
        f.EROSION_DE = erosionDegreeSpinner.getSelectedItem().toString();
        f.EROSION_AR = erosionAreaView.getText().toString();
        f.CULV_LEN = culvertLengthView.getText().toString();

        f.CULV_DIA_1 = culvertDiameter1View.getText().toString();
        f.CULV_DIA_2 = culvertDiameter2View.getText().toString();
        f.CULV_DIA_3 = culvertDiameter3View.getText().toString();

        f.CULV_DIA_1_M = convertMMToM(f.CULV_DIA_1);
        f.CULV_DIA_2_M = convertMMToM(f.CULV_DIA_2);
        f.CULV_DIA_3_M = convertMMToM(f.CULV_DIA_3);

        f.CULV_SUBS = culvertSubstrateSpinner.getSelectedItem().toString();


        f.CULV_SUBSTYPE1 = cst1Spinner.getSelectedItem().toString();
        f.CULV_SUBSTYPE2 = cst2Spinner.getSelectedItem().toString();
        f.CULV_SUBSTYPE3 = cst3Spinner.getSelectedItem().toString();

        f.CULV_SUBSPROPORTION1 = csp1Spinner.getSelectedItem().toString();
        f.CULV_SUBSPROPORTION2 = csp2Spinner.getSelectedItem().toString();
        f.CULV_SUBSPROPORTION3 = csp3Spinner.getSelectedItem().toString();

        f.CULV_BACKWATERPROPORTION = culvertBackWaterProportionSpinner.getSelectedItem().toString();
        f.CULV_SLOPE = culvertSlopeSpinner.getSelectedItem().toString();
        f.CULV_OUTLETTYPE = culvertOutletTypeSpinner.getSelectedItem().toString();
        f.SCOUR_POOL = scourPoolPresentSpinner.getSelectedItem().toString();
        f.CULV_OPOOD = culvertPoolDepthView.getText().toString();
        f.CULV_OPGAP = culvertOutletGapView.getText().toString();
        f.DELINEATOR = delineatorSpinner.getSelectedItem().toString();
        f.BRDG_LEN = bridgeLengthView.getText().toString();
        f.HAZMARKR = hazardMarkersSpinner.getSelectedItem().toString();
        f.APROCHSIGR = approachSignageSpinner.getSelectedItem().toString();
        f.RDSURFR = roadSurfaceSpinner.getSelectedItem().toString();
        f.APROCHRAIL = approachRailsSpinner.getSelectedItem().toString();
        f.RDDRAINR = roadDrainageSpinner.getSelectedItem().toString();
        f.VISIBILITY = visibilitySpinner.getSelectedItem().toString();
        f.WEARSURF = wearingSurfaceSpinner.getSelectedItem().toString();
        f.RAILCURBR = railCurbSpinner.getSelectedItem().toString();
        f.GIRDEBRACR = girdersBracingSpinner.getSelectedItem().toString();
        f.CAPBEAMR = capBeamSpinner.getSelectedItem().toString();
        f.PILESR = pilesSpinner.getSelectedItem().toString();
        f.ABUTWALR = abutmentWallSpinner.getSelectedItem().toString();
        f.WINGWALR = wingWallSpinner.getSelectedItem().toString();
        f.BANKSTABR = bankStabilitySpinner.getSelectedItem().toString();
        f.SLOPEPROTR = slopeProtectionSpinner.getSelectedItem().toString();
        f.CHANNELOPEN = channelOpeningSpinner.getSelectedItem().toString();
        f.OBSTRUCTIO = obstructionsSpinner.getSelectedItem().toString();
        f.FISH_SAMP = fishSamplingSpinner.getSelectedItem().toString();
        f.FISH_SM = fishSamplingMethod.getSelectedItem().toString();
        f.FISH_SPP = fishSamplingSpecies1Spinner.getSelectedItem().toString();
        f.FISH_SPP2 = fishSamplingSpecies2Spinner.getSelectedItem().toString();
        f.FISH_PCONC = fishPassageConcernsSpinner.getSelectedItem().toString();
        f.FISH_PCONCREASON = fishPassageConvernsReasonView.getText().toString();
        f.FISH_ReasonDropdown = fishReasonSpinner.getSelectedItem().toString();
        f.BLOCKAGE = blockageSpinner.getSelectedItem().toString();
        f.BLOC_MATR = blockageMaterialView.getText().toString();
        f.BLOC_CAUS = blockageCauseView.getText().toString();
        f.EMG_REP_RE = emergencyRepairRequiredSpinner.getSelectedItem().toString();
        f.STU_PROBS = structuralProblemsSpinner.getSelectedItem().toString();
        f.SEDEMENTAT = sedimentationSpinner.getSelectedItem().toString();
        f.REMARKS = remarksView.getText().toString();
        f.AttachmentPath1 = m_chosenDir;
        f.timestamp = formCreatedTime;

        f.outlet_score = getOutletScore(f.CULV_OPOOD, f.CULV_OPGAP);

        f.ChannelCreekDepthLeft = channelCreekDepthLeftView.getText().toString();
        f.ChannelCreekDepthRight = channelCreekDepthRightView.getText().toString();
        f.ChannelCreekDepthCenter = channelCreekDepthCenterView.getText().toString();
        f.FirstRiffleDistance = firstRiffleDistanceView.getText().toString();
        f.RoadFillAboveCulvert = roadFillAboveCulvertView.getText().toString();


        setPhotoPath(f);

        return f;
    }

    public String convertMMToM(String s){
        if(s != null){
            if(s.matches("-?\\d+(\\.\\d+)?")){
                double d = Double.parseDouble(s);
                d /= 1000; // Convert mm to m
//                Log.i("debug", "Diameter in m: "+ d);
                return Double.toString(d);
            }
        }

        return null;
    }

    /**
     * Return a number string in (m)
     * */
    public String getOutletScore(String s1, String s2){
        int factor = 100;
        if(s1 != null){
            if(s2 != null){
                if(s1.matches("-?\\d+(\\.\\d+)?") && s2.matches("-?\\d+(\\.\\d+)?")){
                    // s1 and s2 are numbers (cm)
                    double d1 = Double.parseDouble(s1);
                    double d2 = Double.parseDouble(s2);
                    double total = (d1 + d2)/factor;
                    return Double.toString(total);
//
                }else if(s1.matches("-?\\d+(\\.\\d+)?")){
                    double d = Double.parseDouble(s1);
                    d = d/factor;
                    return Double.toString(d);

                }else if(s2.matches("-?\\d+(\\.\\d+)?")){
                    double d = Double.parseDouble(s2);
                    d = d/factor;
                    return Double.toString(d);

                }else{
                    return null;
                }
            }else{
                if(s1.matches("-?\\d+(\\.\\d+)?")){
                    double d = Double.parseDouble(s1);
                    d = d/factor;
                    return Double.toString(d);
                }
            }
        }else {
            if(s2 != null && s2.matches("-?\\d+(\\.\\d+)?")){
                double d = Double.parseDouble(s2);
                d = d/factor;
                return Double.toString(d);
            }
        }

        return null;
    }


    public void setPhotoPath(Form f) {
        saveTempImage();
        f.PHOTO_INUP = mPhotoMap.get(1);
        f.PHOTO_INDW = mPhotoMap.get(2);
        f.PHOTO_OTUP = mPhotoMap.get(3);
        f.PHOTO_OTDW = mPhotoMap.get(4);
        f.PHOTO_1 = mPhotoMap.get(5);
        f.PHOTO_2 = mPhotoMap.get(6);
        f.PHOTO_ROAD_LEFT = mPhotoMap.get(7);
        f.PHOTO_ROAD_RIGHT = mPhotoMap.get(8);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form_details, menu);
        return true;
    }


    /*
    * Validate a form
    * */
    public void validateForms(Form form){
        // Assume it is valid now
        FormValidator formValidator = new FormValidator(form);
        formValidator.validateForm();
//        form.STATUS = formValidator.getFormStatus();

    }

    public void setPhotoView(ImageView photoView, String path){
//        Log.i("debug", "out: "+ path);
        ImageProcessor_Old imageProcessorOld = new ImageProcessor_Old(photoView, path, true, location);

        imageProcessorOld.setImageView();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
//            Bundle bundle = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) bundle.get("data");

            switch (requestCode) {
                case 1:
//                    photoView1.setImageBitmap(imageBitmap);
                    setPhotoView(photoView1, mCurrentPhotoPath);
                    break;

                case 2:
                    setPhotoView(photoView2, mCurrentPhotoPath);
                    break;

                case 3:
                    setPhotoView(photoView3, mCurrentPhotoPath);
                    break;

                case 4:
                    setPhotoView(photoView4, mCurrentPhotoPath);
                    break;

                case 5:
                    setPhotoView(photoView5, mCurrentPhotoPath);
                    break;

                case 6:
                    setPhotoView(photoView6, mCurrentPhotoPath);
                    break;

                case 7:
                    setPhotoView(photoView7, mCurrentPhotoPath);
                    break;

                case 8:
                    setPhotoView(photoView8, mCurrentPhotoPath);
                    break;

            }
        }

    }

    @Override
    public void onClick(View v) {

//        Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
        switch(v.getId()){
            case R.id.inlet1:
//                startActivityForResult(i, 1);
                dispatchTakePictureIntent(1);
                break;

            case R.id.inlet2:
//                startActivityForResult(i, 2);
                dispatchTakePictureIntent(2);
                break;

            case R.id.outlet1:
                dispatchTakePictureIntent(3);
                break;

            case R.id.outlet2:
                dispatchTakePictureIntent(4);
                break;


            case R.id.other1:
                dispatchTakePictureIntent(5);
                break;

            case R.id.other2:
                dispatchTakePictureIntent(6);
                break;

            case R.id.roadLeft:
                dispatchTakePictureIntent(7);
                break;

            case R.id.roadRight:
                dispatchTakePictureIntent(8);
                break;

            case R.id.attachmentButton:
                openFileChooser();
                break;

            case R.id.cancel_attachment:
                cancelAttachment();
                break;

            case R.id.attachmentName:
                // Get intent
                if(m_chosenDir != null && !m_chosenDir.equals("")){
                    try{

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        File file = new File(m_chosenDir);
                        String name = file.getName();

                        MimeTypeMap mime = MimeTypeMap.getSingleton();
                        String extension = name.substring(name.lastIndexOf(".")+1).toLowerCase();
                        String type = mime.getMimeTypeFromExtension(extension);

                        intent.setDataAndType(Uri.fromFile(file), type);

                        startActivity(intent);

                    }catch (Exception e){
                        Toast.makeText(this, "Cannot open the file", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    protected void cancelAttachment(){
        m_chosenDir = "";
        attachmentName.setText("");
    }

    protected String m_chosenDir = "";
    protected void openFileChooser() {
        DirectoryChooserDialog directoryChooserDialog =
                new DirectoryChooserDialog(FormActivity.this,
                        new DirectoryChooserDialog.ChosenDirectoryListener(){
                            @Override
                            public void onChosenDir(String chosenDir) {
                                File file = new File(chosenDir);
                                if(file.exists()){
                                    long fileSizeInBytes = file.length();
                                    if(fileSizeInBytes > 10000000){
                                        Toast.makeText(FormActivity.this,
                                                "File size must be less than 10MB",
                                                Toast.LENGTH_SHORT).show();
                                    }else{
                                        m_chosenDir = chosenDir;
                                        Toast.makeText(FormActivity.this, "Choosen file: "
                                                + m_chosenDir, Toast.LENGTH_LONG).show();
                                        String fileName = m_chosenDir
                                                .substring(m_chosenDir
                                                        .lastIndexOf("/")+1);
                                        attachmentName.setText(fileName);
                                        cancelAttachmentButton.setVisibility(View.VISIBLE);
                                    }
                                }

                            }
                        });
        directoryChooserDialog.setNewFolderEnabled(false);
        directoryChooserDialog.chooseDirectory(m_chosenDir);
    }

    /* Create a camera intent */
    public void dispatchTakePictureIntent(int requestCode){
        mCurrentRequestCode = requestCode;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch(IOException ex){
                // Error occurred while creating file
                Toast.makeText(this, "Error occurred while creating file", Toast.LENGTH_SHORT).show();
            }

            // Continue only if the File was successfully created
            if(photoFile != null){
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, requestCode);
            }
        }else{
            Toast.makeText(this, "No applications handle camera", Toast.LENGTH_SHORT).show();
        }


    }

    public File createImageFile() throws IOException{
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp;
        File dir = new File(this.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "picupload");

        if(!dir.exists()){
            dir.mkdir();
        }

        File image = new File(dir.getAbsolutePath() + "/" + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        deleteTempImageIfExist();
        mCurrentPhotoPath = image.getAbsolutePath();

//        Log.i("debug", "Image path: " + mCurrentPhotoPath);
        mTempPhotoMap.put(mCurrentRequestCode, mCurrentPhotoPath);


        return image;
    }
    protected void deleteTempImageIfExist(){
        if(mTempPhotoMap.containsKey(mCurrentRequestCode)){
            String p = mTempPhotoMap.get(mCurrentRequestCode);
            File f = new File(p);
            if(f.exists()){
                f.delete();
            }
        }
    }

    protected void deleteTempImageIfExist2(){
        if(mTempPhotoMap != null && mTempPhotoMap.size()>0) {
            Iterator iterator = mTempPhotoMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> me = (Map.Entry<Integer, String>) iterator.next();
                File f = new File(me.getValue());
                if (f.exists()) {
                    f.delete();
                }
                iterator.remove();
            }
        }
    }

    protected void saveTempImage(){
        if(mTempPhotoMap != null && mTempPhotoMap.size()>0) {
            Iterator iterator = mTempPhotoMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> me = (Map.Entry<Integer, String>) iterator.next();
                if(mPhotoMap.get(me.getKey()) != null){
                    // Photo get replaced, delete old photo
                    File temp = new File(mPhotoMap.get(me.getKey()));
                    if(temp.exists()){
                        temp.delete();
                    }

                }

                // Add/Update new photo
                Log.i("debug", me.getValue());
                File temp2 = new File(me.getValue());
                if(temp2.exists()) {
                    mPhotoMap.put(me.getKey(), me.getValue());
                }

                iterator.remove();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ViewToggler toggler = new ViewToggler(parent, position, culvertBlock, bridgeBlock,
                erosionBlock, fishSamplingBlock, blockageBlock,culvertDiameter2Block,
                culvertDiameter3Block,fishReasonBlock);
        toggler.toggleView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    protected void showExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Discard");
        builder.setMessage("This form is not saved, are you sure you want to discard it?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTempImageIfExist2();
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
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                REQUEST,
                this);  // LocationListener
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitudeView.setText(""+location.getLatitude());
        longitudeView.setText(""+location.getLongitude());
        this.location = location;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
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

        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//        }
    }
}
