package com.map.woodlands.woodlandsmap.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.map.woodlands.woodlandsmap.R;

/**
 * Created by Jimmy on 3/11/2015.
 */
public class FormActivity extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        ActionBar actionBar = getSupportActionBar();


        actionBar.setDisplayHomeAsUpEnabled(true);


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
                Log.i("debug", "Saved");
                validateForms();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form_details, menu);
        return true;
    }


    /*
    * Validate a form
    * */
    public void validateForms(){

    }
}
