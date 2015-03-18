package com.map.woodlands.woodlandsmap.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.map.woodlands.woodlandsmap.Activities.EditFormActivity;
import com.map.woodlands.woodlandsmap.Activities.FormActivity;
import com.map.woodlands.woodlandsmap.Adapters.FormArrayAdapter;
import com.map.woodlands.woodlandsmap.Data.Form;
import com.map.woodlands.woodlandsmap.Data.FormController;
import com.map.woodlands.woodlandsmap.R;

import java.util.ArrayList;

/**
 * Created by Jimmy on 3/11/2015.
 */
public class FormFragment  extends Fragment{
    private ActionBar mActionBar;
    private ActionBarActivity aba;

    private ListView listView;
    private ArrayList<Form> forms;

    public FormController formController;

    public static FormFragment newInstance(){
        FormFragment f = new FormFragment();
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_form, container, false);

        aba = (ActionBarActivity) this.getActivity();
        formController = new FormController(aba.getApplicationContext(), this);
        setHasOptionsMenu(true);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListView();
    }

    public void setListView() {
        forms = formController.getAllForms();
//        Log.i("debug","#Forms: "+ forms.size());
        final FormArrayAdapter adapter = new FormArrayAdapter(aba, forms, this);

        listView = (ListView) aba.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Form theForm = adapter.getItem(position);
//                Log.i("debug","Form ID: "+theForm.ID);

                Intent intent = new Intent(aba, EditFormActivity.class);
                intent.putExtra("ID", theForm.ID);


                startActivityForResult(intent, 0);
            }
        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0){
//            Log.i("debug", "Form finished");
            setListView();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                // Create a new form, go to form detail activity
                Intent intent = new Intent(aba, FormActivity.class);
//                startActivity(intent);
                startActivityForResult(intent, 0);

                return true;

            case 1:
                // Filter forms
                return true;

            case 2:
                // Delete forms
                formController.deleteAllForms();
                return true;

            case 3:
                // Submit forms
                formController.submitForms();
                return true;

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
