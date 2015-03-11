package com.map.woodlands.woodlandsmap.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.map.woodlands.woodlandsmap.Activities.FormActivity;
import com.map.woodlands.woodlandsmap.R;

/**
 * Created by Jimmy on 3/11/2015.
 */
public class FormFragment  extends Fragment{
    private ActionBar mActionBar;
    private ActionBarActivity aba;



    public static FormFragment newInstance(){
        FormFragment f = new FormFragment();
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_form, container, false);

        aba = (ActionBarActivity) this.getActivity();
        setHasOptionsMenu(true);

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                // Create a new form, go to form detail activity
                Log.i("debug", "0");
                Intent intent = new Intent(aba, FormActivity.class);
                startActivity(intent);

                return true;

            case 1:
                // Filter forms
                Log.i("debug", "1");
                return true;

            case 2:
                // Delete forms
                Log.i("debug", "2");
                return true;

            case 3:
                // Submit forms
                Log.i("debug", "3");

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0,0,0,"Add")
                .setIcon(R.drawable.file_add)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
                        | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        menu.add(0,1,1,"Filter")
                .setIcon(R.drawable.file_search)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
                        | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        menu.add(0,2,2,"Delete")
                .setIcon(R.drawable.file_delete)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        menu.add(0,3,3,"Submit")
                .setIcon(R.drawable.file_submit)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
                        | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
    }

}
