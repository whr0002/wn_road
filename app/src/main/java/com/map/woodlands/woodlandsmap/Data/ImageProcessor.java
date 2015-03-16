package com.map.woodlands.woodlandsmap.Data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

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
    public ImageProcessor(ImageView imageView, String path, Boolean isCompress){
        this.mImageView = imageView;
        this.mPath = path;
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
        int scaleFactor = 1;
        // Determine how much to scale down the image
//        if(targetW != 0 && targetH != 0) {
        scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//        }

        // Decode the image file into a Bitmap sized to fill the view
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor << 1;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mPath, bmOptions);

        // Indicate whether to compress the image
        if(mIsCompress)
            compressImage(bitmap);


        mImageView.setImageBitmap(bitmap);
    }

    public void compressImage(Bitmap b){
        File file = new File(mPath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}
