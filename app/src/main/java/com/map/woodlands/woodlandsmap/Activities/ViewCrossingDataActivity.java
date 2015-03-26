package com.map.woodlands.woodlandsmap.Activities;

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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossing);

        this.linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        this.actionBar = this.getSupportActionBar();
        this.actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            json = extras.getString("json");
            parseJson();
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
            StringBuilder sb = new StringBuilder();
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

    private void addView(String key, String value){
        if(key.toLowerCase().contains("water crossing")){
            actionBar.setTitle(value);
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
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
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
        textViewParams.gravity = Gravity.CENTER_VERTICAL;
        titleView.setGravity(Gravity.LEFT);
        titleView.setLayoutParams(textViewParams);

        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(textViewParams);
        if(key.equals("PHOTO_INUP")) {
            imageView.setId(R.id.photo_inup);
        }else if(key.equals("PHOTO_INDW")) {
            imageView.setId(R.id.photo_indw);
        }else if(key.equals("PHOTO_OTUP")) {
            imageView.setId(R.id.photo_otup);
        }else if(key.equals("PHOTO_OTDW")) {
            imageView.setId(R.id.photo_otdw);
        }else if(key.equals("PHOTO_1")) {
            imageView.setId(R.id.photo_1);
        }else if(key.equals("PHOTO_1")) {
            imageView.setId(R.id.photo_2);
        }
        linearLayout1.addView(titleView);
        linearLayout1.addView(imageView);
        linearLayout.addView(linearLayout1);

        final int id = imageView.getId();
        String url = "http://woodlandsnorth.azurewebsites.net/Content/Photos/"+value;
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

    private void setBackgroundColor(LinearLayout linearLayout1) {
        if(isColor) {
            linearLayout1.setBackgroundColor(getResources().getColor(R.color.blue));
            isColor = false;
        }else{
            isColor = true;
        }
    }


}
