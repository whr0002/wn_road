package com.map.woodlands.woodlandsmap.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.map.woodlands.woodlandsmap.Data.Form;
import com.map.woodlands.woodlandsmap.Data.GPSTracker;
import com.map.woodlands.woodlandsmap.Data.ImageProcessor;
import com.map.woodlands.woodlandsmap.Data.ViewToggler;
import com.map.woodlands.woodlandsmap.R;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Jimmy on 3/11/2015.
 */
public class FormActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    static final int DATE_DIALOG_ID = 999;

    public int year;
    public int month;
    public int day;
    public String mCurrentPhotoPath;
    public int mCurrentRequestCode;
    public HashMap<Integer, String> mPhotoMap;

    public ImageButton dateButton;

    public TextView dateView,latitudeView,longitudeView;

    public EditText inspectionCrewView,crossingNumberView,crossingIDView,streamIDView,dispositionIDView,
            streamWidthView,erosionAreaView,culvertLengthView,culvertDiameter1View,
            culvertDiameter2View,culvertDiameter3View,culvertPoolDepthView,culvertOutletGapView,
            bridgeLengthView,fishPassageConvernsReasonView,blockageView,blockageMaterialView,
            blockageCauseView,remarksView;


    public Spinner accessSpinner,streamClassificationSpinner,streamWidthMeasuredSpinner,crossingTypeSpinner,erosionSpinner,
            erosionType1Spinner,erosionType2Spinner,erosionSourceSpinner,erosionDegreeSpinner,culvertSubstrateSpinner,
            culvertSubstratePSpinner, culvertSubstrateTypeSpinner,culvertSubstrateProportionSpinner,
            culvertBackWaterProportionSpinner,culvertSlopeSpinner,culvertOutletTypeSpinner,scourPoolPresentSpinner,
            delineatorSpinner,hazardMarkersSpinner,approachSignageSpinner,roadSurfaceSpinner,approachRailsSpinner,
            roadDrainageSpinner,visibilitySpinner,wearingSurfaceSpinner,railCurbSpinner,girdersBracingSpinner,
            capBeamSpinner,pilesSpinner,abutmentWallSpinner,wingWallSpinner,bankStabilitySpinner,slopeProtectionSpinner,
            channelOpeningSpinner,obstructionsSpinner,fishSamplingSpinner,fishSamplingMethod,fishSamplingSpecies1Spinner,
            fishSamplingSpecies2Spinner,fishPassageConcernsSpinner,emergencyRepairRequiredSpinner,structuralProblemsSpinner,
            sedimentationSpinner;

    public ImageView photoView1,photoView2,photoView3,photoView4,photoView5,photoView6;

    public LinearLayout culvertBlock,bridgeBlock,erosionBlock,fishSamplingBlock;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.mPhotoMap = new HashMap<Integer, String>();
        setView();

    }

    protected void setView() {
        setCurrentDateView();
        setDatePicker();
        setTextViews();
        setSpinners();
        setLayouts();
        setImageViews();
        setLatLong();
    }

    protected void setLatLong() {
        GPSTracker gpsTracker = new GPSTracker(this.getApplicationContext());
        latitudeView.setText(""+gpsTracker.getLatitude());
        longitudeView.setText(""+gpsTracker.getLongitude());
    }

    public void setTextViews() {
        latitudeView = (TextView)findViewById(R.id.latText);
        longitudeView = (TextView)findViewById(R.id.longText);

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
        blockageView = (EditText)findViewById(R.id.blockageText);
        blockageMaterialView = (EditText)findViewById(R.id.blockageMaterialText);
        blockageCauseView = (EditText)findViewById(R.id.blockageCauseText);
        remarksView = (EditText)findViewById(R.id.remarksText);
    }

    public void setSpinners() {
        accessSpinner = (Spinner)findViewById(R.id.accessDropdown);
        streamClassificationSpinner = (Spinner)findViewById(R.id.streamClassDropdown);
        streamWidthMeasuredSpinner = (Spinner)findViewById(R.id.streamMeasuredDropdown);
        crossingTypeSpinner = (Spinner)findViewById(R.id.crossingTypeDropdown);
        erosionSpinner = (Spinner)findViewById(R.id.erosionDropdown);
        erosionType1Spinner = (Spinner)findViewById(R.id.erosionType1Dropdown);
        erosionType2Spinner = (Spinner)findViewById(R.id.erosionType2Dropdown);
        erosionSourceSpinner = (Spinner)findViewById(R.id.erosionSourceDropdown);
        erosionDegreeSpinner = (Spinner)findViewById(R.id.erosionDegreeDropdown);
        culvertSubstrateSpinner = (Spinner)findViewById(R.id.culvertSubstrateDropdown);
        culvertSubstratePSpinner = (Spinner)findViewById(R.id.culvertSubstratePDropdown);
        culvertSubstrateTypeSpinner = (Spinner)findViewById(R.id.culvertSubstrateTypeDropdown);
        culvertSubstrateProportionSpinner = (Spinner)findViewById(R.id.culvertSubstrateProportionDropdown);
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

        crossingTypeSpinner.setOnItemSelectedListener(this);
        erosionSpinner.setOnItemSelectedListener(this);
        fishSamplingSpinner.setOnItemSelectedListener(this);
    }

    public void setLayouts() {
        culvertBlock = (LinearLayout)findViewById(R.id.culvertBlock);
        bridgeBlock = (LinearLayout)findViewById(R.id.bridgeBlock);
        erosionBlock = (LinearLayout)findViewById(R.id.erosionBlock);
        fishSamplingBlock = (LinearLayout)findViewById(R.id.fishSamplingBlock);
    }


    protected void setImageViews() {
        photoView1 = (ImageView) findViewById(R.id.inlet1);
        photoView2 = (ImageView) findViewById(R.id.inlet2);
        photoView3 = (ImageView) findViewById(R.id.outlet1);
        photoView4 = (ImageView) findViewById(R.id.outlet2);
        photoView5 = (ImageView) findViewById(R.id.other1);
        photoView6 = (ImageView) findViewById(R.id.other2);

        photoView1.setOnClickListener(this);
        photoView2.setOnClickListener(this);
        photoView3.setOnClickListener(this);
        photoView4.setOnClickListener(this);
        photoView5.setOnClickListener(this);
        photoView6.setOnClickListener(this);
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
                .append(day).append("/").append(month + 1)
                .append("/").append(year));



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
                    .append(day).append("/").append(month + 1)
                    .append("/").append(year));
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                // Discard this form, open confirmation box
                finish();
                return true;

            case R.id.save:
                // Save form
                Form form = generateForm();
                if(validateForms(form)){
                    // Form is complete
                    saveData(form);
                    finish();
                }else{
                    // Form is not complete

                }
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void saveData(Form form) {
        ArrayList<Form> formList;
        SharedPreferences sp = getSharedPreferences("Data",0);
        SharedPreferences.Editor spEditor = sp.edit();
        String json = sp.getString("FormData","");
        Gson gson = new Gson();


        if(json.equals("")){
            // No form data, add now
            formList = new ArrayList<Form>();

        }else{
            // Have old data
            Type listType = new TypeToken<ArrayList<Form>>(){}.getType();
            formList = gson.fromJson(json, listType);

        }

        formList.add(form);
        json = gson.toJson(formList);

        spEditor.putString("FormData", json);
        spEditor.commit();

        Log.i("debug","json2: "+ json);

    }

    /* Generate a new form */
    public Form generateForm(){
        SharedPreferences sp = getSharedPreferences("Data",0);
        SharedPreferences.Editor spEditor = sp.edit();
        int id = sp.getInt("ID", 0);

        Form theForm = new Form();
        theForm.ID = id;
        theForm.INSP_DATE = dateView.getText().toString();
        theForm.INSP_CREW = inspectionCrewView.getText().toString();
        theForm.ACCESS = accessSpinner.getSelectedItem().toString();
        theForm.CROSS_NM = crossingNumberView.getText().toString();
        theForm.CROSS_ID = crossingIDView.getText().toString();
        theForm.STR_ID = streamIDView.getText().toString();
        theForm.DISPOSITION_ID = dispositionIDView.getText().toString();
        theForm.LAT = latitudeView.getText().toString();
        theForm.LONG = longitudeView.getText().toString();
        theForm.STR_CLASS = streamClassificationSpinner.getSelectedItem().toString();
        theForm.STR_WIDTH = streamWidthView.getText().toString();
        theForm.STR_WIDTHM = streamWidthMeasuredSpinner.getSelectedItem().toString();
        theForm.CROSS_TYPE = crossingTypeSpinner.getSelectedItem().toString();
        theForm.EROSION = erosionSpinner.getSelectedItem().toString();
        theForm.EROSION_TY1 = erosionType1Spinner.getSelectedItem().toString();
        theForm.EROSION_TY2 = erosionType2Spinner.getSelectedItem().toString();
        theForm.EROSION_SO = erosionSourceSpinner.getSelectedItem().toString();
        theForm.EROSION_DE = erosionDegreeSpinner.getSelectedItem().toString();
        theForm.EROSION_AR = erosionAreaView.getText().toString();
        theForm.CULV_LEN = culvertLengthView.getText().toString();
        theForm.CULV_DIA_1 = culvertDiameter1View.getText().toString();
        theForm.CULV_DIA_2 = culvertDiameter2View.getText().toString();
        theForm.CULV_DIA_3 = culvertDiameter3View.getText().toString();
        theForm.CULV_SUBS = culvertSubstrateSpinner.getSelectedItem().toString();
        theForm.CULV_SUBSP = culvertSubstratePSpinner.getSelectedItem().toString();
        theForm.CULV_SUBSTYPE = culvertSubstrateTypeSpinner.getSelectedItem().toString();
        theForm.CULV_SUBSPROPORTION = culvertSubstrateProportionSpinner.getSelectedItem().toString();
        theForm.CULV_BACKWATERPROPORTION = culvertBackWaterProportionSpinner.getSelectedItem().toString();
        theForm.CULV_SLOPE = culvertSlopeSpinner.getSelectedItem().toString();
        theForm.CULV_OUTLETTYPE = culvertOutletTypeSpinner.getSelectedItem().toString();
        theForm.SCOUR_POOL = scourPoolPresentSpinner.getSelectedItem().toString();
        theForm.CULV_OPOOD = culvertPoolDepthView.getText().toString();
        theForm.CULV_OPGAP = culvertOutletGapView.getText().toString();
        theForm.DELINEATOR = delineatorSpinner.getSelectedItem().toString();
        theForm.BRDG_LEN = bridgeLengthView.getText().toString();
        theForm.HAZMARKR = hazardMarkersSpinner.getSelectedItem().toString();
        theForm.APROCHSIGR = approachSignageSpinner.getSelectedItem().toString();
        theForm.RDSURFR = roadSurfaceSpinner.getSelectedItem().toString();
        theForm.APROCHRAIL = approachRailsSpinner.getSelectedItem().toString();
        theForm.RDDRAINR = roadDrainageSpinner.getSelectedItem().toString();
        theForm.VISIBILITY = visibilitySpinner.getSelectedItem().toString();
        theForm.WEARSURF = wearingSurfaceSpinner.getSelectedItem().toString();
        theForm.RAILCURBR = railCurbSpinner.getSelectedItem().toString();
        theForm.GIRDEBRACR = girdersBracingSpinner.getSelectedItem().toString();
        theForm.CAPBEAMR = capBeamSpinner.getSelectedItem().toString();
        theForm.PILESR = pilesSpinner.getSelectedItem().toString();
        theForm.ABUTWALR = abutmentWallSpinner.getSelectedItem().toString();
        theForm.WINGWALR = wingWallSpinner.getSelectedItem().toString();
        theForm.BANKSTABR = bankStabilitySpinner.getSelectedItem().toString();
        theForm.SLOPEPROTR = slopeProtectionSpinner.getSelectedItem().toString();
        theForm.CHANNELOPEN = channelOpeningSpinner.getSelectedItem().toString();
        theForm.OBSTRUCTIO = obstructionsSpinner.getSelectedItem().toString();
        theForm.FISH_SAMP = fishSamplingSpinner.getSelectedItem().toString();
        theForm.FISH_SM = fishSamplingMethod.getSelectedItem().toString();
        theForm.FISH_SPP = fishSamplingSpecies1Spinner.getSelectedItem().toString();
        theForm.FISH_SPP2 = fishSamplingSpecies2Spinner.getSelectedItem().toString();
        theForm.FISH_PCONC = fishPassageConcernsSpinner.getSelectedItem().toString();
        theForm.FISH_PCONCREASON = fishPassageConvernsReasonView.getText().toString();
        theForm.BLOCKAGE = blockageView.getText().toString();
        theForm.BLOC_MATR = blockageMaterialView.getText().toString();
        theForm.BLOC_CAUS = blockageCauseView.getText().toString();
        theForm.EMG_REP_RE = emergencyRepairRequiredSpinner.getSelectedItem().toString();
        theForm.STU_PROBS = structuralProblemsSpinner.getSelectedItem().toString();
        theForm.SEDEMENTAT = sedimentationSpinner.getSelectedItem().toString();
        theForm.REMARKS = remarksView.getText().toString();

        setPhotoPath(theForm);


        spEditor.putInt("ID", id+1);
        spEditor.commit();

        return theForm;
    }

    public void setPhotoPath(Form f) {
        f.PHOTO_INUP = mPhotoMap.get(1);
        f.PHOTO_INDW = mPhotoMap.get(2);
        f.PHOTO_OTUP = mPhotoMap.get(3);
        f.PHOTO_OTDW = mPhotoMap.get(4);
        f.PHOTO_1 = mPhotoMap.get(5);
        f.PHOTO_2 = mPhotoMap.get(6);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form_details, menu);
        return true;
    }


    /*
    * Validate a form
    * */
    public boolean validateForms(Form form){
        // Assume it is valid now
        form.STATUS = "Ready to submit";
        return true;
    }

    public void setPhotoView(ImageView photoView, String path){
        ImageProcessor imageProcessor = new ImageProcessor(photoView, path, true);
        imageProcessor.setImageView();

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

            default:
                break;
        }
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
        String storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/picupload";
        File dir = new File(storageDir);

        if(!dir.exists()){
            dir.mkdir();
        }

        File image = new File(storageDir + "/" + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("debug", "Image path: " + mCurrentPhotoPath);
        mPhotoMap.put(mCurrentRequestCode, mCurrentPhotoPath);

        return image;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ViewToggler toggler = new ViewToggler(parent, position, culvertBlock, bridgeBlock, erosionBlock, fishSamplingBlock);
        toggler.toggleView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
