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


        }else{
            messages.add("Form is null");
        }

        if(messages.size() == 0){
            mForm.STATUS = "Ready to submit";
        }else{
            mForm.STATUS = "Not complete";
            mForm.messages = messages;
        }
    }

    public String generateLengthMessage(String fieldName, int length){
        String s = "The length of " + fieldName +" must be less than " + length + " characters";
        return s;
    }
}
