package com.map.woodlands.woodlandsroad.Data;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.map.woodlands.woodlandsroad.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jimmy on 6/9/2015.
 */
public class CameraHandler {

    public static final int CAMERA_REQUEST_CODE = 10;
    public ArrayList<Photo> newCreatedPhotos, allPhotos, removedPhotos;
    private Activity mActivity;
    private Photo currentPhoto;


    public CameraHandler(Activity activity){
        mActivity = activity;
        newCreatedPhotos = new ArrayList<Photo>();
        allPhotos = new ArrayList<Photo>();
        removedPhotos = new ArrayList<Photo>();
    }

    public void onTakenPhoto(int requestCode, int resultCode, Intent data, LinearLayout currentLayout, ImageProcessor imageProcessor){


        if(requestCode == CAMERA_REQUEST_CODE){
            // back from camera intent
            Log.i("debug", "back from camera");

            if(currentPhoto != null && currentLayout != null) {

                if(imageProcessor.isImageFound(currentPhoto.path)) {

                    imageProcessor.shrinkImage(currentPhoto.path);

                    Photo p = new Photo();
                    p.path = currentPhoto.path;
                    p.formType = currentPhoto.formType;
                    p.classification = currentPhoto.classification;
                    p.description = currentPhoto.description;

                    currentPhoto = null;

                    newCreatedPhotos.add(p);
                    allPhotos.add(p);
                    addGalleryItem(allPhotos.size() - 1, p, currentLayout, allPhotos, removedPhotos);

                }else{
                    Log.i("debug", "image or layout does not exist");
                }

            }
        }
    }

    public void openCamera(String formType, String classification, LinearLayout layout, int requestCode, ImageProcessor imageProcessor){

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(cameraIntent.resolveActivity(mActivity.getPackageManager()) != null){
            // image capture app exists
            String path = generateImagePath(formType+"_"+ classification +"_", ".jpg");

            // set up Photo properties and the layout to be added in
            currentPhoto = new Photo();
            currentPhoto.path = path;
            currentPhoto.classification = classification;
            currentPhoto.formType = formType;

//            currentLayout = layout;


            if(path != null){

                File file = new File(path);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                mActivity.startActivityForResult(cameraIntent, requestCode);
            }else{
                Toast.makeText(mActivity, "Could not generate image path", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void addGalleryItem(int id, Photo photo, final LinearLayout galleryLayout, final ArrayList<Photo> photos, final ArrayList<Photo> removedPhotos){
        LinearLayout linearLayout = new LinearLayout(mActivity);
        linearLayout.setId(id);
        LinearLayout.LayoutParams layoutParams = new LinearLayout
                .LayoutParams(150
                , 200);
        linearLayout.setPadding(5,5,5,5);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setWeightSum(1.0f);

        linearLayout.setLayoutParams(layoutParams);

        TextView textView = new TextView(mActivity);

        if(photo.description != null){
            textView.setText(photo.description);
        }else {
            textView.setText("Image title goes here");
        }

        textView.setId(R.id.image_desc);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        textViewParams.weight = 0.2f;
        textViewParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(textViewParams);
        textView.setSingleLine(true);


        ImageView imageView = new ImageView(mActivity);
        imageView.setId(R.id.image);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        imageViewParams.weight = 0.8f;
        imageView.setLayoutParams(imageViewParams);




        // Attach views
        linearLayout.addView(imageView);
        linearLayout.addView(textView);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePopup imagePopup = new ImagePopup(mActivity, view, photos, removedPhotos, galleryLayout);
                imagePopup.showPopup();
            }
        });

        galleryLayout.addView(linearLayout);



        // Decode bitmap and set it
        PathView pv = new PathView();
        pv.imagePath = photo.path;
        pv.imageView = imageView;
        DecodeImageAsync decodeImageAsync = new DecodeImageAsync();
        decodeImageAsync.execute(pv);

    }

    public String generateImagePath(String prefix, String ext){

        if(prefix == null){
            prefix = RoadForm.FORM_TYPE + "_";
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String dir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                + File.separator + "picupload" + File.separator;

        File dirFile = new File(dir);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }

        String fullPath = dir + prefix + timeStamp + ext;

        Log.i("debug", "Full image path: " + fullPath);

        return fullPath;


    }

    public void clearTempImages(List<Photo> removedPhotos){

        if(removedPhotos != null && removedPhotos.size() > 0){

            for(Photo photo : removedPhotos){

                if(photo != null) {
                    File file = new File(photo.path);

                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }
    }

    public ArrayList<Photo> getCurrentPhotos(){
        ArrayList<Photo> currentPhotos = new ArrayList<Photo>();
        for(Photo p1 : allPhotos){
            boolean isAvailable = true;
            for(Photo p2 : removedPhotos){
                if(p1.path.equals(p2.path)){
                    isAvailable = false;
                    break;
                }
            }

            if(isAvailable){
                currentPhotos.add(p1);
            }
        }



        return currentPhotos;
    }
}
