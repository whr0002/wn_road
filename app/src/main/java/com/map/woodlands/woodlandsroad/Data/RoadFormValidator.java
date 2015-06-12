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

            if(mForm.InspectorName == null) {
                messages.add("Name is required");
            }else if(mForm.InspectorName.trim().length() == 0){
                    messages.add("Name is required");
            }


            if(mForm.Locations == null){
                messages.add("Record coordinates is required");
            }else if(mForm.Locations != null && mForm.Locations.size() == 0){
                messages.add("Record coordinates is required");
            }

            if(mForm.RoadName == null){
                messages.add("Road Name is required");
            }else if(mForm.RoadName.trim().length() == 0){
                messages.add("Road Name is required");
            }

            if(mForm.RoadStatus == null){
                messages.add("Road Status is required");
            }else if(mForm.RoadStatus.trim().length() == 0){
                messages.add("Road Status is required");
            }

            if(mForm.RS_Condition == null){
                messages.add("Road Surface Condition is required");
            }else if(mForm.RS_Condition.trim().length() == 0){
                messages.add("Road Surface Condition is required");
            }


            if(mForm.RS_RoadSurface == null){
                messages.add("Road Surface is required");
            }else if(mForm.RS_RoadSurface.trim().length() == 0){
                messages.add("Road Surface is required");
            }

            if(mForm.RS_VegetationCover == null){
                messages.add("Road Surface Vegetation is required");
            }else if(mForm.RS_VegetationCover.trim().length() == 0){
                messages.add("Road Surface Vegetation Cover is required");
            }

            if(mForm.DI_Ditches == null){
                messages.add("Ditches is required");
            }else if(mForm.DI_Ditches.trim().length() == 0){
                messages.add("Ditches is required");
            }

            if(mForm.DI_VegetationCover == null){
                messages.add("Ditches Vegetation Cover is required");
            }else if(mForm.DI_VegetationCover.trim().length() == 0){
                messages.add("Ditches Vegetation Cover is required");
            }

            if(mForm.RoadStatus == null){
                messages.add("Road Status is required");
            }else if(mForm.OT_Signage.trim().length() == 0){
                messages.add("Signage is required");
            }

            if(mForm.OT_RoadMR == null){
                messages.add("Road maintenace is required");
            }else if(mForm.OT_RoadMR.trim().length() == 0){
                messages.add("Road maintenace is required");
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
