package com.map.woodlands.woodlandsmap.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.map.woodlands.woodlandsmap.Data.Form;
import com.map.woodlands.woodlandsmap.Data.ImageProcessor;
import com.map.woodlands.woodlandsmap.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Jimmy on 3/12/2015.
 */
public class EditFormActivity extends FormActivity{

    private int formID;
    private int formIndex;
    private ArrayList<Form> mForms;
    private Form theForm;
    private boolean isInOnce = false;

    @Override
    protected void setView() {
        setCurrentDateView();
        setDatePicker();
        setTextViews();
        setSpinners();
        setLayouts();
        setImageViews();


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!isInOnce && formID != -1){
            // Got the form ID, set its data on view
//            setForm();
            setPhotoFromPath();
            isInOnce = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        dateView = (TextView) findViewById(R.id.dateView);

        this.formID = getIntent().getIntExtra("ID", -1);
        setForm();


    }

    /* Generate a new form */
    @Override
    public Form generateForm() {
        if(theForm != null){
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

            return theForm;
        }
        return super.generateForm();
    }


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
                    modifyData(form);
                    finish();
                }else{
                    // Form is not complete

                }
                return true;



            default:
                return true;
        }
    }

    private void modifyData(Form form) {
        mForms.set(formIndex, form);
        SharedPreferences sp = getSharedPreferences("Data",0);
        SharedPreferences.Editor spEditor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mForms);

        spEditor.putString("FormData", json);
        spEditor.commit();


    }

    private int getSpinnerIndex(Spinner spinner, String name){
        int index = 0;
        if(spinner != null && name != null) {

            for (int i = 0; i < spinner.getCount(); i++) {
                if (spinner.getItemAtPosition(i).equals(name)) {
                    index = i;
                }
            }

        }
        return index;
    }

    /*
    * Set form data in view
    * */
    private void setForm() {
        theForm = searchForForm();
        if(theForm != null) {
            dateView.setText(theForm.INSP_DATE);
            inspectionCrewView.setText(theForm.INSP_CREW);
            accessSpinner.setSelection(getSpinnerIndex(accessSpinner, theForm.ACCESS));
            crossingNumberView.setText(theForm.CROSS_NM);

//            theForm.CROSS_NM = crossingNumberView.getText().toString();
//            theForm.CROSS_ID = crossingIDView.getText().toString();
//            theForm.STR_ID = streamIDView.getText().toString();
//            theForm.DISPOSITION_ID = dispositionIDView.getText().toString();
//            theForm.LAT = latitudeView.getText().toString();
//            theForm.LONG = longitudeView.getText().toString();
//            theForm.STR_CLASS = streamClassificationSpinner.getSelectedItem().toString();
//            theForm.STR_WIDTH = streamWidthView.getText().toString();
//            theForm.STR_WIDTHM = streamWidthMeasuredSpinner.getSelectedItem().toString();
//            theForm.CROSS_TYPE = crossingTypeSpinner.getSelectedItem().toString();
//            theForm.EROSION = erosionSpinner.getSelectedItem().toString();
//            theForm.EROSION_TY1 = erosionType1Spinner.getSelectedItem().toString();
//            theForm.EROSION_TY2 = erosionType2Spinner.getSelectedItem().toString();
//            theForm.EROSION_SO = erosionSourceSpinner.getSelectedItem().toString();
//            theForm.EROSION_DE = erosionDegreeSpinner.getSelectedItem().toString();
//            theForm.EROSION_AR = erosionAreaView.getText().toString();
//            theForm.CULV_LEN = culvertLengthView.getText().toString();
//            theForm.CULV_DIA_1 = culvertDiameter1View.getText().toString();
//            theForm.CULV_DIA_2 = culvertDiameter2View.getText().toString();
//            theForm.CULV_DIA_3 = culvertDiameter3View.getText().toString();
//            theForm.CULV_SUBS = culvertSubstrateSpinner.getSelectedItem().toString();
//            theForm.CULV_SUBSP = culvertSubstratePSpinner.getSelectedItem().toString();
//            theForm.CULV_SUBSTYPE = culvertSubstrateTypeSpinner.getSelectedItem().toString();
//            theForm.CULV_SUBSPROPORTION = culvertSubstrateProportionSpinner.getSelectedItem().toString();
//            theForm.CULV_BACKWATERPROPORTION = culvertBackWaterProportionSpinner.getSelectedItem().toString();
//            theForm.CULV_SLOPE = culvertSlopeSpinner.getSelectedItem().toString();
//            theForm.CULV_OUTLETTYPE = culvertOutletTypeSpinner.getSelectedItem().toString();
//            theForm.SCOUR_POOL = scourPoolPresentSpinner.getSelectedItem().toString();
//            theForm.CULV_OPOOD = culvertPoolDepthView.getText().toString();
//            theForm.CULV_OPGAP = culvertOutletGapView.getText().toString();
//            theForm.DELINEATOR = delineatorSpinner.getSelectedItem().toString();
//            theForm.BRDG_LEN = bridgeLengthView.getText().toString();
//            theForm.HAZMARKR = hazardMarkersSpinner.getSelectedItem().toString();
//            theForm.APROCHSIGR = approachSignageSpinner.getSelectedItem().toString();
//            theForm.RDSURFR = roadSurfaceSpinner.getSelectedItem().toString();
//            theForm.APROCHRAIL = approachRailsSpinner.getSelectedItem().toString();
//            theForm.RDDRAINR = roadDrainageSpinner.getSelectedItem().toString();
//            theForm.VISIBILITY = visibilitySpinner.getSelectedItem().toString();
//            theForm.WEARSURF = wearingSurfaceSpinner.getSelectedItem().toString();
//            theForm.RAILCURBR = railCurbSpinner.getSelectedItem().toString();
//            theForm.GIRDEBRACR = girdersBracingSpinner.getSelectedItem().toString();
//            theForm.CAPBEAMR = capBeamSpinner.getSelectedItem().toString();
//            theForm.PILESR = pilesSpinner.getSelectedItem().toString();
//            theForm.ABUTWALR = abutmentWallSpinner.getSelectedItem().toString();
//            theForm.WINGWALR = wingWallSpinner.getSelectedItem().toString();
//            theForm.BANKSTABR = bankStabilitySpinner.getSelectedItem().toString();
//            theForm.SLOPEPROTR = slopeProtectionSpinner.getSelectedItem().toString();
//            theForm.CHANNELOPEN = channelOpeningSpinner.getSelectedItem().toString();
//            theForm.OBSTRUCTIO = obstructionsSpinner.getSelectedItem().toString();
//            theForm.FISH_SAMP = fishSamplingSpinner.getSelectedItem().toString();
//            theForm.FISH_SM = fishSamplingMethod.getSelectedItem().toString();
//            theForm.FISH_SPP = fishSamplingSpecies1Spinner.getSelectedItem().toString();
//            theForm.FISH_SPP2 = fishSamplingSpecies2Spinner.getSelectedItem().toString();
//            theForm.FISH_PCONC = fishPassageConcernsSpinner.getSelectedItem().toString();
//            theForm.FISH_PCONCREASON = fishPassageConvernsReasonView.getText().toString();
//            theForm.BLOCKAGE = blockageView.getText().toString();
//            theForm.BLOC_MATR = blockageMaterialView.getText().toString();
//            theForm.BLOC_CAUS = blockageCauseView.getText().toString();
//            theForm.EMG_REP_RE = emergencyRepairRequiredSpinner.getSelectedItem().toString();
//            theForm.STU_PROBS = structuralProblemsSpinner.getSelectedItem().toString();
//            theForm.SEDEMENTAT = sedimentationSpinner.getSelectedItem().toString();
//            theForm.REMARKS = remarksView.getText().toString();
//            setPhotoFromPath();
        }
    }

    private void setPhotoFromPath() {
        if(theForm.PHOTO_INUP != null){
            setPhotoView(photoView1, theForm.PHOTO_INUP);
            mPhotoMap.put(1,theForm.PHOTO_INUP);
        }

        if(theForm.PHOTO_INDW != null){
            setPhotoView(photoView2, theForm.PHOTO_INDW);
            mPhotoMap.put(2,theForm.PHOTO_INDW);
        }

        if(theForm.PHOTO_OTUP != null){
            setPhotoView(photoView3, theForm.PHOTO_OTUP);
            mPhotoMap.put(3, theForm.PHOTO_OTUP);
        }

        if(theForm.PHOTO_OTDW != null){
            setPhotoView(photoView4, theForm.PHOTO_OTDW);
            mPhotoMap.put(4, theForm.PHOTO_OTDW);
        }

        if(theForm.PHOTO_1 != null){
            setPhotoView(photoView5, theForm.PHOTO_1);
            mPhotoMap.put(5,theForm.PHOTO_1);
        }

        if(theForm.PHOTO_2 != null){
            setPhotoView(photoView6, theForm.PHOTO_2);
            mPhotoMap.put(6,theForm.PHOTO_2);
        }
    }

    /*
    * Return a form by form ID
    * */
    private Form searchForForm() {
        SharedPreferences sp = getSharedPreferences("Data", 0);
//        SharedPreferences.Editor spEditor = sp.edit();
        String json = sp.getString("FormData", "");
        if(!json.equals("")){
            Gson gson = new Gson();
            Type listType = new TypeToken< ArrayList<Form>>(){}.getType();
            mForms = gson.fromJson(json, listType);

            for(int i=0;i<mForms.size();i++){
                if(mForms.get(i).ID == formID){
                    formIndex = i;
                    return mForms.get(i);
                }
            }

        }
        return null;
    }

    @Override
    public void setPhotoView(ImageView photoView, String path) {
        ImageProcessor imageProcessor = new ImageProcessor(photoView, path, false);
        imageProcessor.setImageView();
    }
}
