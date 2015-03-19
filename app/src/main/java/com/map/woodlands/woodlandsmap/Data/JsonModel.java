package com.map.woodlands.woodlandsmap.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 3/19/2015.
 */
public class JsonModel {
    public List<Form> fieldDataList;
    public JsonModel(ArrayList<Form> forms){
        this.fieldDataList = forms;
    }
}
