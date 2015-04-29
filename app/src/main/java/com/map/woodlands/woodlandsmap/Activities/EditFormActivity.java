package com.map.woodlands.woodlandsmap.Activities;

import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;

import com.map.woodlands.woodlandsmap.Data.Form;
import com.map.woodlands.woodlandsmap.Data.ImageProcessor;
import com.map.woodlands.woodlandsmap.Data.IndexForm;
import com.map.woodlands.woodlandsmap.R;

import java.util.ArrayList;

/**
 * Created by Jimmy on 3/12/2015.
 */
public class EditFormActivity extends FormActivity{

    private boolean isCompress = false;
    private int formID;
    private int formIndex;
    private ArrayList<Form> mForms;
    private Form f;
    private boolean isInOnce = false;

    @Override
    protected void setView() {
        setCurrentDateView();
        setDatePicker();
        setTextViews();
        setSpinners();
        setLayouts();
        setImageViews();
        setImageButtons();


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
        this.formID = getIntent().getIntExtra("ID", -1);
        setForm();


    }

    /* Generate a new form */
    @Override
    public Form generateForm() {
        if(f != null){
            f.INSP_DATE = dateView.getText().toString();
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
            f.outlet_score = getOutletScore(f.CULV_OPOOD, f.CULV_OPGAP);
            f.AttachmentPath1 = m_chosenDir;
            setPhotoPath(f);

            return f;
        }
        return super.generateForm();
    }


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
                // Form is complete
                mFormController.saveForm(formIndex, form);
                finish();

                return true;



            default:
                return true;
        }
    }

    private int getSpinnerIndex(Spinner spinner, String name){
        if(spinner != null && name != null) {

            for (int i = 0; i < spinner.getCount(); i++) {
                if (spinner.getItemAtPosition(i).equals(name)) {
                    return i;
                }
            }

        }
        return 0;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /*
        * Set form data in view
        * */
    private void setForm() {
        IndexForm mif = mFormController.getIndexForm(formID);
        formIndex = mif.index;
        f = mif.form;
        if(f != null) {
            dateView.setText(f.INSP_DATE);
            inspectionCrewView.setText(f.INSP_CREW);
            accessSpinner.setSelection(getSpinnerIndex(accessSpinner, f.ACCESS));
            crossingNumberView.setText(f.CROSS_NM);
            crossingIDView.setText(f.CROSS_ID);
            streamIDView.setText(f.STR_ID);
            dispositionIDView.setText(f.DISPOSITION_ID);
            latitudeView.setText(f.LAT);
            longitudeView.setText(f.LONG);
            streamClassificationSpinner.setSelection(getSpinnerIndex(streamClassificationSpinner,f.STR_CLASS));
            streamWidthView.setText(f.STR_WIDTH);
            streamWidthMeasuredSpinner.setSelection(getSpinnerIndex(streamWidthMeasuredSpinner,f.STR_WIDTHM));
            crossingTypeSpinner.setSelection(getSpinnerIndex(crossingTypeSpinner,f.CROSS_TYPE));
            erosionSpinner.setSelection(getSpinnerIndex(erosionSpinner,f.EROSION));
            erosionType1Spinner.setSelection(getSpinnerIndex(erosionType1Spinner,f.EROSION_TY1));
            erosionSourceSpinner.setSelection(getSpinnerIndex(erosionSourceSpinner, f.EROSION_SO));
            erosionDegreeSpinner.setSelection(getSpinnerIndex(erosionDegreeSpinner, f.EROSION_DE));
            erosionAreaView.setText(f.EROSION_AR);
            culvertLengthView.setText(f.CULV_LEN);
            culvertDiameter1View.setText(f.CULV_DIA_1);
            culvertDiameter2View.setText(f.CULV_DIA_2);
            culvertDiameter3View.setText(f.CULV_DIA_3);
            culvertSubstrateSpinner.setSelection(getSpinnerIndex(culvertSubstrateSpinner,f.CULV_SUBS));

            cst1Spinner.setSelection(getSpinnerIndex(cst1Spinner, f.CULV_SUBSTYPE1));
            cst2Spinner.setSelection(getSpinnerIndex(cst2Spinner, f.CULV_SUBSTYPE2));
            cst3Spinner.setSelection(getSpinnerIndex(cst3Spinner, f.CULV_SUBSTYPE3));

            csp1Spinner.setSelection(getSpinnerIndex(csp1Spinner, f.CULV_SUBSPROPORTION1));
            csp2Spinner.setSelection(getSpinnerIndex(csp2Spinner, f.CULV_SUBSPROPORTION2));
            csp3Spinner.setSelection(getSpinnerIndex(csp3Spinner, f.CULV_SUBSPROPORTION3));

            culvertBackWaterProportionSpinner.setSelection(getSpinnerIndex(culvertBackWaterProportionSpinner,f.CULV_BACKWATERPROPORTION));
            culvertSlopeSpinner.setSelection(getSpinnerIndex(culvertSlopeSpinner,f.CULV_SLOPE));
            culvertOutletTypeSpinner.setSelection(getSpinnerIndex(culvertOutletTypeSpinner,f.CULV_OUTLETTYPE));
            scourPoolPresentSpinner.setSelection(getSpinnerIndex(scourPoolPresentSpinner,f.SCOUR_POOL));
            culvertPoolDepthView.setText(f.CULV_OPOOD);
            culvertOutletGapView.setText(f.CULV_OPGAP);
            delineatorSpinner.setSelection(getSpinnerIndex(delineatorSpinner,f.DELINEATOR));
            bridgeLengthView.setText(f.BRDG_LEN);
            hazardMarkersSpinner.setSelection(getSpinnerIndex(hazardMarkersSpinner,f.HAZMARKR));
            approachSignageSpinner.setSelection(getSpinnerIndex(approachSignageSpinner,f.APROCHSIGR));
            roadSurfaceSpinner.setSelection(getSpinnerIndex(roadSurfaceSpinner,f.RDSURFR));
            approachRailsSpinner.setSelection(getSpinnerIndex(approachRailsSpinner,f.APROCHRAIL));
            roadDrainageSpinner.setSelection(getSpinnerIndex(roadDrainageSpinner,f.RDDRAINR));
            visibilitySpinner.setSelection(getSpinnerIndex(visibilitySpinner,f.VISIBILITY));
            wearingSurfaceSpinner.setSelection(getSpinnerIndex(wearingSurfaceSpinner,f.WEARSURF));
            railCurbSpinner.setSelection(getSpinnerIndex(railCurbSpinner,f.RAILCURBR));
            girdersBracingSpinner.setSelection(getSpinnerIndex(girdersBracingSpinner,f.GIRDEBRACR));
            capBeamSpinner.setSelection(getSpinnerIndex(capBeamSpinner,f.CAPBEAMR));
            pilesSpinner.setSelection(getSpinnerIndex(pilesSpinner,f.PILESR));
            abutmentWallSpinner.setSelection(getSpinnerIndex(abutmentWallSpinner,f.ABUTWALR));
            wingWallSpinner.setSelection(getSpinnerIndex(wingWallSpinner,f.WINGWALR));
            bankStabilitySpinner.setSelection(getSpinnerIndex(bankStabilitySpinner,f.BANKSTABR));
            slopeProtectionSpinner.setSelection(getSpinnerIndex(slopeProtectionSpinner,f.SLOPEPROTR));
            channelOpeningSpinner.setSelection(getSpinnerIndex(channelOpeningSpinner,f.CHANNELOPEN));
            obstructionsSpinner.setSelection(getSpinnerIndex(obstructionsSpinner,f.OBSTRUCTIO));
            fishSamplingSpinner.setSelection(getSpinnerIndex(fishSamplingSpinner,f.FISH_SAMP));
            fishSamplingMethod.setSelection(getSpinnerIndex(fishSamplingMethod,f.FISH_SM));
            fishSamplingSpecies1Spinner.setSelection(getSpinnerIndex(fishSamplingSpecies1Spinner,f.FISH_SPP));
            fishSamplingSpecies2Spinner.setSelection(getSpinnerIndex(fishSamplingSpecies2Spinner,f.FISH_SPP2));
            fishPassageConcernsSpinner.setSelection(getSpinnerIndex(fishPassageConcernsSpinner,f.FISH_PCONC));
            fishPassageConvernsReasonView.setText(f.FISH_PCONCREASON);
            fishReasonSpinner.setSelection(getSpinnerIndex(fishReasonSpinner, f.FISH_ReasonDropdown));
            blockageSpinner.setSelection(getSpinnerIndex(blockageSpinner, f.BLOCKAGE));
            blockageMaterialView.setText(f.BLOC_MATR);
            blockageCauseView.setText(f.BLOC_CAUS);
            emergencyRepairRequiredSpinner.setSelection(getSpinnerIndex(emergencyRepairRequiredSpinner,f.EMG_REP_RE));
            structuralProblemsSpinner.setSelection(getSpinnerIndex(structuralProblemsSpinner,f.STU_PROBS));
            sedimentationSpinner.setSelection(getSpinnerIndex(sedimentationSpinner,f.SEDEMENTAT));
            remarksView.setText(f.REMARKS);
            String s = f.AttachmentPath1;
            if(s != null && !s.equals("")) {
                m_chosenDir = s;
                attachmentName.setText(f.AttachmentPath1.substring(f.AttachmentPath1.lastIndexOf("/")+1));
                cancelAttachmentButton.setVisibility(View.VISIBLE);
            }
//            setPhotoFromPath();
        }
    }

    private void setPhotoFromPath() {
        if(f.PHOTO_INUP != null){
            setPhotoView(photoView1, f.PHOTO_INUP);
            mPhotoMap.put(1, f.PHOTO_INUP);
        }

        if(f.PHOTO_INDW != null){
            setPhotoView(photoView2, f.PHOTO_INDW);
            mPhotoMap.put(2, f.PHOTO_INDW);
        }

        if(f.PHOTO_OTUP != null){
            setPhotoView(photoView3, f.PHOTO_OTUP);
            mPhotoMap.put(3, f.PHOTO_OTUP);
        }

        if(f.PHOTO_OTDW != null){
            setPhotoView(photoView4, f.PHOTO_OTDW);
            mPhotoMap.put(4, f.PHOTO_OTDW);
        }

        if(f.PHOTO_1 != null){
            setPhotoView(photoView5, f.PHOTO_1);
            mPhotoMap.put(5, f.PHOTO_1);
        }

        if(f.PHOTO_2 != null){
            setPhotoView(photoView6, f.PHOTO_2);
            mPhotoMap.put(6, f.PHOTO_2);
        }
        isCompress = true;
    }

    @Override
    public void setPhotoView(ImageView photoView, String path) {
        ImageProcessor imageProcessor = new ImageProcessor(photoView, path, isCompress, location);
        imageProcessor.setImageView();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }
}
