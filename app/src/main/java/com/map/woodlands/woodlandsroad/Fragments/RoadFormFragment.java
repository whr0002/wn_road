package com.map.woodlands.woodlandsroad.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.map.woodlands.woodlandsroad.Activities.RoadInspectionActivity;
import com.map.woodlands.woodlandsroad.Adapters.RoadFormArrayAdapter;
import com.map.woodlands.woodlandsroad.Data.DeleteDialog;
import com.map.woodlands.woodlandsroad.Data.RoadForm;
import com.map.woodlands.woodlandsroad.Data.RoadFormController;
import com.map.woodlands.woodlandsroad.Data.SubmitDialog;
import com.map.woodlands.woodlandsroad.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 3/11/2015.
 * Form Section
 */
public class RoadFormFragment extends Fragment{
    private ActionBar mActionBar;
    private ActionBarActivity aba;

    private ListView listView;
    private TextView noneView;
//    private View loadingView;
    private ArrayList<RoadForm> forms;


    public RoadFormController formController;


    public static RoadFormFragment newInstance(){
        RoadFormFragment f = new RoadFormFragment();
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_form, container, false);
        aba = (ActionBarActivity) this.getActivity();

        formController = new RoadFormController(aba, this);

        formController.addTestData();

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
        noneView = (TextView)aba.findViewById(R.id.none);
        if(forms == null || forms.size() == 0){
            // No form, Show "None" on view
            this.noneView.setVisibility(View.VISIBLE);
        }else {
            this.noneView.setVisibility(View.GONE);
        }

//        Log.i("debug","#Forms: "+ forms.size());

        final RoadFormArrayAdapter adapter = new RoadFormArrayAdapter(aba, forms, this);

        listView = (ListView) aba.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RoadForm theForm = adapter.getItem(position);
//                Log.i("debug","Form ID: "+theForm.ID);

                Intent intent = new Intent(aba, RoadInspectionActivity.class);
                intent.putExtra("ID", theForm.ID);


                startActivityForResult(intent, 0);
            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                mode.setTitle("Selected " + listView.getCheckedItemCount());

                Log.i("debug", "Checked");
                View v = getViewByPosition(position, listView);
                CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkbox);
                checkBox.setChecked(checked);


                adapter.getItem(position).isSelected = checked;

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.edit_mode_menu, menu);

                for (int i = 0; i < adapter.getCount(); i++) {
//                    View v = adapter.getView(i, null, listView);
                    View v = getViewByPosition(i, listView);
                    CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkbox);
                    checkBox.setVisibility(View.VISIBLE);

                }

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        deleteSelectedItems(adapter);
                        actionMode.finish(); // Action picked, so close the CAB
                        return true;

                    case R.id.submit_selected:

                        submitSelectedItems(adapter);
                        actionMode.finish();

                        return true;


                    default:
                        return false;


                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.notifyDataSetChanged();

                listView.clearChoices();

                for (int i = 0; i < adapter.getCount(); i++) {
                    View v = getViewByPosition(i, listView);
                    CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkbox);
                    checkBox.setChecked(false);
                    checkBox.setVisibility(View.GONE);
                    adapter.getItem(i).isSelected = false;
                }
            }
        });
    }


    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    /**
     * Delete selected items in action mode
     */
    public void deleteSelectedItems(ArrayAdapter<RoadForm> adapter){
        SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
        List<RoadForm> forms = new ArrayList<RoadForm>();

        for(int i=0;i<adapter.getCount();i++){
            if(sparseBooleanArray.get(i)){
                forms.add(adapter.getItem(i));
            }

        }

        DeleteDialog deleteDialog = new DeleteDialog(this.getActivity(),
                forms, "Are you sure you want to delete selected forms?", formController, adapter);

        deleteDialog.show();
    }

    /**
     * Submit selected items in action mode
     */
    public void submitSelectedItems(ArrayAdapter<RoadForm> adapter){
        SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
        List<RoadForm> forms = new ArrayList<RoadForm>();

        for(int i=0;i<adapter.getCount();i++){
            if(sparseBooleanArray.get(i)){
                forms.add(adapter.getItem(i));
            }

        }

        SubmitDialog submitDialog = new SubmitDialog(this.getActivity(),
                forms, "Are you sure you want to submit selected forms?", formController, adapter);

        submitDialog.show();
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
                Intent intent = new Intent(aba, RoadInspectionActivity.class);
                startActivityForResult(intent, 0);

                return true;

            case 3:
                // Submit forms
                AlertDialog.Builder builder = new AlertDialog.Builder(aba);
                builder.setTitle("Submit");
                builder.setMessage("Do you want to submit all forms?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        formController.submitForms();
                    }
                });

                builder.setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0,0,0,"Create")
                .setIcon(R.drawable.file_add)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);


        menu.add(0,3,3,"Submit")
                .setIcon(R.drawable.file_submit)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

    }

}
