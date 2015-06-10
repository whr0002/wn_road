package com.map.woodlands.woodlandsroad.Data;

import java.util.ArrayList;

/**
 * Created by Jimmy on 3/16/2015.
 * Used for validating a form
 */
public class RoadFormValidator {
    private RoadForm mForm;
    private ArrayList<String> messages;
    public RoadFormValidator(RoadForm form){
        this.mForm = form;
        this.messages = new ArrayList<String>();
    }


    public void validateForm(){
        if (mForm != null){
            // Date is required
            if(mForm.INSP_DATE.length() == 0){
                messages.add("Date is required");
            }

            if(mForm.InspectorName.length() == 0){
                messages.add("Name is required");
            }

        }else{
            messages.add("Form is null");
        }

        if(messages.size() == 0){
            mForm.STATUS = "Ready to submit";
            mForm.messages = null;
        }else{
            mForm.STATUS = "Not complete";
            mForm.messages = messages;
        }

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

    private String generateRangeMessage(String fieldName, String value, int start, int end){
        if(value != null && value.length()>0 && isNumeric(value)){
            double d = Double.parseDouble(value);
            if(d < start || d > end){
                return fieldName+" must be in range "+start+"-"+end;
            }
        }
        return null;
    }
}
