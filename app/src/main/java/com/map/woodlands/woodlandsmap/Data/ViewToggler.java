package com.map.woodlands.woodlandsmap.Data;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.map.woodlands.woodlandsmap.R;

/**
 * Created by Jimmy on 3/16/2015.
 */
public class ViewToggler {
    private AdapterView<?> mParent;
    private int mPosition;
    private LinearLayout culvertBlock;
    private LinearLayout bridgeBlock;
    private LinearLayout erosionBlock;
    private LinearLayout fishSamplingBlock;
    private LinearLayout blockageBlock;
    private View loadingView;

    public ViewToggler(AdapterView<?> parent, int position, LinearLayout... blocks){
        this.mParent = parent;
        this.mPosition = position;
        if(blocks.length>4){
            this.culvertBlock = blocks[0];
            this.bridgeBlock = blocks[1];
            this.erosionBlock = blocks[2];
            this.fishSamplingBlock = blocks[3];
            this.blockageBlock = blocks[4];
        }
    }

    public ViewToggler(View v){
        this.loadingView = v;

    }

    public void toggleView(){
        String s = mParent.getItemAtPosition(mPosition).toString().toLowerCase();
        switch (mParent.getId()){
            case R.id.crossingTypeDropdown:

                if(s.length() == 0){
                    culvertBlock.setVisibility(View.GONE);
                    bridgeBlock.setVisibility(View.GONE);
                }else if(s.contains("bridge")){
                    culvertBlock.setVisibility(View.GONE);
                    bridgeBlock.setVisibility(View.VISIBLE);
                }else{
                    culvertBlock.setVisibility(View.VISIBLE);
                    bridgeBlock.setVisibility(View.GONE);
                }
                break;

            case R.id.erosionDropdown:
                if(s.equals("yes") || s.equals("pot")){
                    erosionBlock.setVisibility(View.VISIBLE);
                }else{
                    erosionBlock.setVisibility(View.GONE);
                }
                break;

            case R.id.fishSamplingDropdown:
                if(s.equals("yes")){
                    fishSamplingBlock.setVisibility(View.VISIBLE);
                }else{
                    fishSamplingBlock.setVisibility(View.GONE);
                }
                break;

            case R.id.blockageDropdown:
                if(s.equals("yes")){
                    blockageBlock.setVisibility(View.VISIBLE);
                }else{
                    blockageBlock.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void toggleLoadingView(){
        if(loadingView.getVisibility() == View.VISIBLE){
            loadingView.setVisibility(View.GONE);
        }else{
            loadingView.setVisibility(View.VISIBLE);
        }
    }
}
