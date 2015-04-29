package com.map.woodlands.woodlandsmap.Activities;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.map.woodlands.woodlandsmap.Data.MyApplication;
import com.map.woodlands.woodlandsmap.R;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Jimmy on 3/24/2015.
 */
public class ViewCrossingDataActivity extends ActionBarActivity {
    private String json;
    private LinearLayout linearLayout;
    private boolean isColor = true;
    private ActionBar actionBar;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossing);

        ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);

        this.linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        this.actionBar = this.getSupportActionBar();
        this.actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            json = extras.getString("json");
//            Log.i("debug", json);
            parseJsonInOrder();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void parseJson(){
        try {

            JSONObject jsonObject = new JSONObject(json.trim());
            Iterator<?> keys = jsonObject.keys();

            while(keys.hasNext()){
                String key = (String)keys.next();
                Object o = jsonObject.get(key);
                if(o instanceof String){
                    if(key.contains("PHOTO")){
                        if(((String) o).length()> 10) {
                            o = ((String) o).substring(10);
                            addImageView(key, (String) o);
                        }
                    }else {
                        addView(key, (String) o);
                    }
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void parseJsonInOrder(){
        try{
            this.jsonObject = new JSONObject(json.trim());
            addToView("Inspection Date");
            addToView("Crew");
            addToView("Access");
            addToView("Water Crossing Name or ID");
            addToView("Disposition No.");
            addToView("Latitude");
            addToView("Longitude");
            addToView("Stream Classification");
            addToView("Bankfull Width");
            addToView("Bankfull Width measured?");

            addToView("Crossing Type");
            addToView("Culvert Length");
            addToView("Culvert Diameter 1");
            addToView("Culvert Diameter 2");
            addToView("Culvert Diameter 3");
            addToView("Culvert Substrate");
            addToView("Culvert Substrate Type");
            addToView("For what length of culvert?");
            addToView("What proportion of back water?");
            addToView("Culvert Slope");
            addToView("Culvert Outlet Type");
            addToView("Culvert Pool Depth");
            addToView("Scour Pool Present");
            addToView("Culvert Outlet Gap");
            addToView("Delineators");

            addToView("Bridge Length");
            addToView("Bridge Hazard Markers");
            addToView("Bridge Approach Signs");
            addToView("Bridge Road Surface");
            addToView("Bridge Road Drainage");
            addToView("Bridge Signage Comments");
            addToView("Bridge Wearing Surface");
            addToView("Bridge Rail & Curb");
            addToView("Bridge Girders & Bracing");
            addToView("Bridge Structure Comments");
            addToView("Bridge Cap Beam");
            addToView("Bridge Piles");
            addToView("Bridge Abutment Wall");
            addToView("Bridge Wing Wall");
            addToView("Bridge Foundation Comments");
            addToView("Bridge Bank Stability");
            addToView("Bridge Slope Protection");
            addToView("Bridge Channel Opening");
            addToView("Bridge Obstructions");
            addToView("Bridge Channel Comments");


            addToView("Erosion at Site?");
            addToView("Location of Erosion");
            addToView("Source of Erosion");
            addToView("Source of Erosion 2");
            addToView("Degree of Erosion");
            addToView("Area of Erosion");

            addToView("Fish Sampling");
            addToView("Sampling Method");
            addToView("Fish Species 1");
            addToView("Fish Species 2");
            addToView("Fish Passage");
            addToView("Fish Passage Concerns");

            addToView("Blockage");
            addToView("Blockage Material");
            addToView("Blockage Cause");
            addToView("Greater than 10% of diameter blocked by debris");

            addToView("Photo Inlet Upstream");
            addToView("Photo Inlet Downstream");
            addToView("Photo Outlet Upstream");
            addToView("Photo Outlet Downstream");
            addToView("Photo Other 1");
            addToView("Photo Other 2");

            addToView("Emergency Repairs Req");
            addToView("Structural Problems");
            addToView("Outlet Scour");
            addToView("Sedimentation");
            addToView("Remarks");

            addToView("Risk");




        }catch(Exception e){

        }
    }

    private void addToView(String key){
        try {
            String value = jsonObject.getString(key);
            if(value != null && !value.contains("null")) {
                if (key.toLowerCase().contains("photo") ) {
                    if(value.length()>10){
                        value = value.substring(10);
                        addImageView(key, value);
                    }
                } else {
                    addView(key, value);
                }
            }
        }catch (Exception e){

        }
    }

    private void addView(String key, String value){
        if(key.toLowerCase().contains("water crossing name or id")){
            actionBar.setTitle(value);
        }
        if(key.toLowerCase().contains("date")){
            value = value.substring(0,value.indexOf("T"));
        }

        // Wrapper
        LinearLayout linearLayout1 = new LinearLayout(this);
        LinearLayout.LayoutParams linearParams = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout1.setWeightSum(1.0f);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setLayoutParams(linearParams);
        setBackgroundColor(linearLayout1);

        // Key
        TextView titleView = new TextView(this);
        titleView.setText(key);
        titleView.setTextSize(18);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewParams.weight = 0.5f;
        textViewParams.setMargins(10, 10, 10, 10);
        textViewParams.gravity = Gravity.CENTER_VERTICAL;
        titleView.setGravity(Gravity.LEFT);
        titleView.setLayoutParams(textViewParams);

        // Value
        TextView valueView = new TextView(this);
        valueView.setText(value);
        valueView.setTextSize(18);
        LinearLayout.LayoutParams textViewParams2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewParams2.weight = 0.5f;
        textViewParams2.setMargins(10, 10, 10, 10);
        textViewParams2.gravity = Gravity.CENTER_VERTICAL;
        valueView.setGravity(Gravity.LEFT);
        valueView.setLayoutParams(textViewParams2);

        // Add to view
        linearLayout1.addView(titleView);
        linearLayout1.addView(valueView);

        linearLayout.addView(linearLayout1);


    }

    private void addImageView(String key, String value){
        LinearLayout linearLayout1 = new LinearLayout(this);
        LinearLayout.LayoutParams linearParams = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout1.setWeightSum(1.0f);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setLayoutParams(linearParams);
        setBackgroundColor(linearLayout1);

        // Key
        TextView titleView = new TextView(this);
        titleView.setText(key);
        titleView.setTextSize(18);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewParams.weight = 0.5f;
        textViewParams.leftMargin = 10;
        textViewParams.rightMargin = 10;
        textViewParams.gravity = Gravity.CENTER_VERTICAL;
        titleView.setGravity(Gravity.LEFT);
        titleView.setLayoutParams(textViewParams);

        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(textViewParams);
        if(key.contains("Inlet Upstream")) {
            imageView.setId(R.id.photo_inup);
        }else if(key.contains("Inlet Downstream")) {
            imageView.setId(R.id.photo_indw);
        }else if(key.contains("Outlet Upstream")) {
            imageView.setId(R.id.photo_otup);
        }else if(key.contains("Outlet Downstream")) {
            imageView.setId(R.id.photo_otdw);
        }else if(key.contains("Other 1")) {
            imageView.setId(R.id.photo_1);
        }else if(key.contains("Other 2")) {
            imageView.setId(R.id.photo_2);
        }
        linearLayout1.addView(titleView);
        linearLayout1.addView(imageView);
        linearLayout.addView(linearLayout1);

        final int id = imageView.getId();
        String url = "http://scari.azurewebsites.net/Content/Photos/"+value;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageView imageView1 = (ImageView)findViewById(id);
                imageView1.setImageBitmap(bitmap);

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });




    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void setBackgroundColor(LinearLayout linearLayout1) {
        if(isColor) {
            linearLayout1.setBackgroundColor(getResources().getColor(R.color.blue));
            isColor = false;
        }else{
            isColor = true;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
