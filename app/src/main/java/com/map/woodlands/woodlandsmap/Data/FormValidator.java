package com.map.woodlands.woodlandsmap.Data;

import java.util.ArrayList;

/**
 * Created by Jimmy on 3/16/2015.
 */
public class FormValidator {
    private Form mForm;
    private ArrayList<String> messages;
    public FormValidator(Form form){
        this.mForm = form;
        this.messages = new ArrayList<String>();
    }


    public void validateForm(){
        if (mForm != null){
            // Date is required
            if(mForm.INSP_DATE.length() == 0){
                messages.add("Date is required");
            }

            if(mForm.INSP_CREW.trim().length() == 0){
                messages.add("Inspection Crew is required");
            }else if(mForm.INSP_CREW.length() > 40){
                messages.add(generateLengthMessage("Inspection Crew", 40));
            }

            if(mForm.ACCESS.length() > 10){
                messages.add(generateLengthMessage("Access", 10));
            }

            if(mForm.CROSS_NM.length() > 20){
                messages.add(generateLengthMessage("Crossing Number", 20));
            }

            if(mForm.CROSS_ID.length() > 20){
                messages.add(generateLengthMessage("Crossing ID", 20));
            }

            if(mForm.STR_ID.length() > 20){
                messages.add(generateLengthMessage("Stream ID", 20));
            }

            if(mForm.DISPOSITION_ID.length() > 20){
                messages.add(generateLengthMessage("Disposition ID", 20));
            }

            if(mForm.LAT.length()> 0 && !isNumeric(mForm.LAT)){
                messages.add(generateNumericMessage("Latitude"));
            }

            if(mForm.LONG.length()> 0 && !isNumeric(mForm.LONG)){
                messages.add(generateNumericMessage("Longitude"));
            }

            if(mForm.STR_CLASS.length() > 30){
                messages.add(generateLengthMessage("Stream Classification", 30));
            }

            if(mForm.STR_WIDTH.length()> 0 && !isNumeric(mForm.STR_WIDTH)){
                messages.add(generateNumericMessage("Stream Width"));
            }

            if(mForm.CROSS_TYPE.length() == 0){
                messages.add("Crossing Type is required");
            }

            if(mForm.EROSION.length() == 0){
                messages.add("Erosion is required");
            }

            if(mForm.EROSION_AR.length()> 0 && !isNumeric(mForm.EROSION_AR)){
                messages.add(generateNumericMessage("Erosion Area"));
            }

            if(mForm.CULV_LEN.length()> 0 && !isNumeric(mForm.CULV_LEN)){
                messages.add(generateNumericMessage("Culvert Length"));
            }

            if(mForm.CULV_DIA_1.length()> 0 && !isNumeric(mForm.CULV_DIA_1)){
                messages.add(generateNumericMessage("Culvert Diameter 1"));
            }

            if(mForm.CULV_DIA_2.length()> 0 && !isNumeric(mForm.CULV_DIA_2)){
                messages.add(generateNumericMessage("Culvert Diameter 2"));
            }

            if(mForm.CULV_DIA_3.length()> 0 && !isNumeric(mForm.CULV_DIA_3)){
                messages.add(generateNumericMessage("Culvert Diameter 3"));
            }

            if(mForm.CULV_OPOOD.length()> 0 && !isNumeric(mForm.CULV_OPOOD)){
                messages.add(generateNumericMessage("Culvert Pool Depth"));
            }

            if(mForm.CULV_OPGAP.length()> 0 && !isNumeric(mForm.CULV_OPGAP)){
                messages.add(generateNumericMessage("Culvert Outlet Gap"));
            }

            if(mForm.BRDG_LEN.length()> 0 && !isNumeric(mForm.BRDG_LEN)){
                messages.add(generateNumericMessage("Bridge Length"));
            }

            if(mForm.FISH_PCONC.length() == 0){
                messages.add("Fish Passage Concerns is required");
            }

            if(mForm.FISH_PCONCREASON.length() > 20){
                messages.add(generateLengthMessage("Fish Passage Concerns Reason", 20));
            }

            if(mForm.BLOCKAGE.length() == 0){
                messages.add("Blockage is required");
            }

            if(mForm.BLOCKAGE.length() > 50){
                messages.add(generateLengthMessage("Blockage", 50));
            }

            if(mForm.BLOC_MATR.length() > 20){
                messages.add(generateLengthMessage("Blockage Material", 20));
            }

            if(mForm.BLOC_CAUS.length() > 50){
                messages.add(generateLengthMessage("Blockage Cause", 50));
            }

            if(mForm.REMARKS.length() > 120){
                messages.add(generateLengthMessage("Remarks", 120));
            }

        }else{
            messages.add("Form is null");
        }

        if(messages.size() == 0){
            mForm.STATUS = "Ready to submit";
        }else{
            mForm.STATUS = "Not complete";
        }
        mForm.messages = messages;
    }

    private boolean isNumeric(String str){
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private String generateLengthMessage(String fieldName, int length){
        String s = "The length of " + fieldName +" must be less than " + length + " characters";
        return s;
    }

    private String generateNumericMessage(String fieldName){
        String s = fieldName +" must be a numeric value";
        return s;
    }
}
