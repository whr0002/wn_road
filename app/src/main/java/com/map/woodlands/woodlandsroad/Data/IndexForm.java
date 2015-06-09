package com.map.woodlands.woodlandsroad.Data;

/**
 * Created by Jimmy on 3/18/2015.
 */
public class IndexForm{
    public int index;
    public Form form;
    public RoadForm roadForm;
    public IndexForm(int i, Form f){
        this.index = i;
        this.form = f;
    }

    public IndexForm(int i, RoadForm f){
        this.index = i;
        this.roadForm = f;
    }
}
