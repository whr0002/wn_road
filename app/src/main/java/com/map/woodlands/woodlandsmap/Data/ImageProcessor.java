package com.map.woodlands.woodlandsmap.Data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.ExifInterface;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Jimmy on 3/16/2015.
 */
public class ImageProcessor {
    private ImageView mImageView;
    private String mPath;
    private boolean mIsCompress;
    private LatLng mLatLng;
    public ImageProcessor(ImageView imageView, String path, Boolean isCompress, Location location){
        this.mImageView = imageView;
        this.mPath = path;
        this.mIsCompress = isCompress;
        if(location != null) {
            this.mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
    }

    public void setImageView(){
        // Get the dimensions of the view
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(mPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = 5;
        // Determine how much to scale down the image
        if(targetW != 0 && targetH != 0) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        // Decode the image file into a Bitmap sized to fill the view
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;


        Bitmap bitmap = BitmapFactory.decodeFile(mPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
        // Indicate whether to compress the image
        if(mIsCompress) {
            bmOptions.inSampleSize = 10;
            Bitmap b = BitmapFactory.decodeFile(mPath, bmOptions);
            compressImage(b);

            if(mLatLng != null){
                if(mPath != null) {
                    setGeoTag(mPath, mLatLng);
                }
            }
        }






    }

    public void compressImage(Bitmap b){
        File file = new File(mPath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
//                e.printStackTrace();
            }
        }else{
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
//                e.printStackTrace();
            }
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            if (fos != null) {
                b.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.close();
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public boolean setGeoTag(String path, LatLng geoTag) {
        if (geoTag != null) {
            try {
                ExifInterface exif = new ExifInterface(path);

                double latitude = Math.abs(geoTag.latitude);
                double longitude = Math.abs(geoTag.longitude);

                int num1Lat = (int) Math.floor(latitude);
                int num2Lat = (int) Math.floor((latitude - num1Lat) * 60);
                double num3Lat = (latitude - ((double) num1Lat + ((double) num2Lat / 60))) * 3600000;

                int num1Lon = (int) Math.floor(longitude);
                int num2Lon = (int) Math.floor((longitude - num1Lon) * 60);
                double num3Lon = (longitude - ((double) num1Lon + ((double) num2Lon / 60))) * 3600000;

                String lat = num1Lat + "/1," + num2Lat + "/1," + num3Lat + "/1000";
                String lon = num1Lon + "/1," + num2Lon + "/1," + num3Lon + "/1000";

                if (geoTag.latitude > 0) {
                    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
                } else {
                    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
                }

                if (geoTag.longitude > 0) {
                    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
                } else {
                    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
                }

                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, lat);
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, lon);

                exif.saveAttributes();

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
