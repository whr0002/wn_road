package com.map.woodlands.woodlandsmap.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.map.woodlands.woodlandsmap.Data.Form;
import com.map.woodlands.woodlandsmap.R;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jimmy on 3/11/2015.
 */
public class FormActivity extends ActionBarActivity implements View.OnClickListener{
    static final int DATE_DIALOG_ID = 999;

    public int year;
    public int month;
    public int day;
    public String mCurrentPhotoPath;

    public TextView dateView;
    public ImageButton dateButton;
    public ImageView photoView1;
    public ImageView photoView2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setView();






    }

    protected void setView() {
        setCurrentDateView();
        setDatePicker();

        setPhotoViews();
    }



    private void setPhotoViews() {
        photoView1 = (ImageView) findViewById(R.id.inlet1);
        photoView2 = (ImageView) findViewById(R.id.inlet2);

        photoView1.setOnClickListener(this);
        photoView2.setOnClickListener(this);
    }

    private void setDatePicker() {
        dateButton = (ImageButton) findViewById(R.id.date_picker);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    private void setCurrentDateView() {
        dateView = (TextView) findViewById(R.id.dateView);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        dateView.setText(new StringBuilder()
                .append(month + 1).append("-").append(day)
                .append("-").append(year));



    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case DATE_DIALOG_ID:

                return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener(){

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay){
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Set selected date to date view
            dateView.setText(new StringBuilder()
                    .append(month + 1).append("-").append(day)
                    .append("-").append(year));
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                // Discard this form, open confirmation box
                finish();
                return true;

            case R.id.save:
                // Save form
                Form form = getForm();
                if(validateForms(form)){
                    // Form is complete
                    saveData(form);
                    finish();
                }else{
                    // Form is not complete

                }
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void saveData(Form form) {
        ArrayList<Form> formList;
        SharedPreferences sp = getSharedPreferences("Data",0);
        SharedPreferences.Editor spEditor = sp.edit();
        String json = sp.getString("FormData","");
        Gson gson = new Gson();


        if(json.equals("")){
            // No form data, add now
            formList = new ArrayList<Form>();

        }else{
            // Have old data
            Type listType = new TypeToken<ArrayList<Form>>(){}.getType();
            formList = gson.fromJson(json, listType);

        }

        formList.add(form);
        json = gson.toJson(formList);

        spEditor.putString("FormData", json);
        spEditor.commit();

        Log.i("debug","json2: "+ json);

    }

    public Form getForm(){
        SharedPreferences sp = getSharedPreferences("Data",0);
        SharedPreferences.Editor spEditor = sp.edit();
        int id = sp.getInt("ID", 0);

        Form f = new Form();
        f.ID = id;
        f.INSP_DATE = dateView.getText().toString();



        spEditor.putInt("ID", id+1);
        spEditor.commit();

        return f;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form_details, menu);
        return true;
    }


    /*
    * Validate a form
    * */
    public boolean validateForms(Form form){
        // Assume it is valid now
        form.STATUS = "Ready to submit";
        return true;
    }

    public void setPhotoView(ImageView photoView){
        // Get the dimensions of the view
        int targetW = photoView.getWidth();
        int targetH = photoView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the view
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor << 1;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

//        Matrix mtx = new Matrix();
//        mtx.postRotate(90);
//        // Rotating Bitmap
//        Bitmap rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);
//
//        if (rotatedBMP != bitmap)
//            bitmap.recycle();

        photoView.setImageBitmap(bitmap);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
//            Bundle bundle = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) bundle.get("data");

            switch (requestCode) {
                case 1:
//                    photoView1.setImageBitmap(imageBitmap);
                    setPhotoView(photoView1);
                    break;


                case 2:
                    setPhotoView(photoView2);
//                    photoView2.setImageBitmap(imageBitmap);
                    break;

            }
        }

    }

    @Override
    public void onClick(View v) {
//        Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
        switch(v.getId()){
            case R.id.inlet1:
//                startActivityForResult(i, 1);
                dispatchTakePictureIntent(1);
                break;

            case R.id.inlet2:
//                startActivityForResult(i, 2);
                dispatchTakePictureIntent(2);
                break;

            default:
                break;
        }
    }

    public void dispatchTakePictureIntent(int requestCode){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch(IOException ex){
                // Error occurred while creating file
                Toast.makeText(this, "Error occurred while creating file", Toast.LENGTH_SHORT).show();
            }

            // Continue only if the File was successfully created
            if(photoFile != null){
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, requestCode);
            }
        }else{
            Toast.makeText(this, "No applications handle camera", Toast.LENGTH_SHORT).show();
        }


    }

    private File createImageFile() throws IOException{
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        String storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/picupload";
        File dir = new File(storageDir);

        if(!dir.exists()){
            dir.mkdir();
        }

        File image = new File(storageDir + "/" + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("debug", "Image path: " + mCurrentPhotoPath);
        return image;
    }
}
