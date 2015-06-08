package com.map.woodlands.woodlandsroad.Data;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.map.woodlands.woodlandsroad.R;

import java.util.ArrayList;

/**
 * Created by Jimmy on 6/5/2015.
 */
public class LayoutBuilder {

    private Context mContext;

    public LayoutBuilder(Context context){

        mContext = context;
    }

    public LinearLayout buildDropDown(final String title, int arrayID){

        // Wrapper
        final LinearLayout wrapper = new LinearLayout(mContext);
        LinearLayout.LayoutParams wrapperParams = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
        wrapper.setWeightSum(1.0f);
        wrapper.setOrientation(LinearLayout.HORIZONTAL);
        wrapper.setLayoutParams(wrapperParams);

        // child views
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        params.weight = 0.5f;

        LinearLayout.LayoutParams params2 = new LinearLayout
                .LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params2.gravity = Gravity.CENTER;
        params2.weight = 0.3f;

        LinearLayout.LayoutParams params3 = new LinearLayout
                .LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params3.gravity = Gravity.CENTER;
        params3.weight = 0.2f;




        // Title view
        TextView titleView = new TextView(mContext);
        titleView.setText(title);
        titleView.setTextSize(18.0f);
        titleView.setTextColor(mContext.getResources().getColor(R.color.black));
        titleView.setGravity(Gravity.CENTER_VERTICAL);

        titleView.setTag(arrayID);
        titleView.setLayoutParams(params);



        // Drop Down
        final Spinner spinner = new Spinner(mContext);
        String[] values = mContext.getResources().getStringArray(R.array.road_status_test);

        ArrayList<String> names = new ArrayList<String>();
        final ArrayList<String> descs = new ArrayList<String>();

        for(int i=0;i<values.length;i++){
            String s = values[i];

            int breakPoint = s.indexOf("$");

            String desc;
            String name;
            if(breakPoint == -1){
                name = s;
                desc = "none";
            }else{
                name = s.substring(0, s.indexOf("$"));
                desc = s.substring(s.indexOf("$")+1);
            }
            names.add(name);
            descs.add(desc);

        }

        String[] namesArray = names.toArray(new String[names.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                R.layout.spinner_item, namesArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setLayoutParams(params2);


        ImageButton imageButton = new ImageButton(mContext);
        imageButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.file_warning));
        imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageButton.setTag(titleView);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = spinner.getSelectedItemPosition();
                showInfoDialog(descs.get(temp));
            }
        });
        imageButton.setLayoutParams(params3);


        // Add child views to wrapper
        wrapper.addView(titleView);
        wrapper.addView(spinner);
        wrapper.addView(imageButton);



        return wrapper;
    }

    public Button buildToggleButton(final LinearLayout layout){

        LinearLayout.LayoutParams params4 = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60);

        // Show/Hide button
        Button button = new Button(mContext);
        button.setText("Show/Hide");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout.getVisibility() == View.VISIBLE){
                    layout.setVisibility(View.GONE);
                }else{
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });
        button.setLayoutParams(params4);

        return button;
    }


    public LinearLayout buildWrapper(String title){

        LinearLayout wrapper = new LinearLayout(mContext);
        wrapper.setBackground(mContext.getResources().getDrawable(R.drawable.box));
        LinearLayout.LayoutParams wrapperParams = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wrapper.setOrientation(LinearLayout.VERTICAL);
        wrapperParams.topMargin = 10;
        wrapper.setLayoutParams(wrapperParams);

        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setText(title);
        textView.setTextColor(mContext.getResources().getColor(R.color.black));
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(params1);



        wrapper.addView(textView);


        return wrapper;

    }

    private void showInfoDialog(String desc){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Description");
        builder.setMessage(desc);

        builder.setPositiveButton("OK", null);

        builder.show();

    }

}
