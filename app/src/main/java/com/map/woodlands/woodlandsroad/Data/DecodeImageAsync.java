package com.map.woodlands.woodlandsroad.Data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * Created by Jimmy on 5/26/2015.
 */
public class DecodeImageAsync extends AsyncTask<PathView, Void, DecodeImageAsync.BitmapView> {


    @Override
    protected BitmapView doInBackground(PathView... pathViews) {

        if(pathViews != null && pathViews[0] != null) {

            BitmapView bitmapView = new BitmapView();
            ImageView imageView = pathViews[0].imageView;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathViews[0].imagePath, options);

            // Calculate inSampleSize


            if(imageView.getWidth() == 0 || imageView.getHeight() == 0){
                while (imageView.getWidth() == 0 || imageView.getHeight() == 0){

                }
            }

            options.inSampleSize = calculateInSampleSize(options,
                    imageView.getWidth(), imageView.getHeight());
            // Decode bitmap with inSampleSize
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(pathViews[0].imagePath, options);

            bitmapView.bitmap = bitmap;
            bitmapView.imageView = imageView;

//            Log.i("debug", "Width: " + pathViews[0].imageView.getMeasuredWidth() + " Height: " + pathViews[0].imageView.getMeasuredHeight());

            return bitmapView;
        }

        return null;
    }



    @Override
    protected void onPostExecute(BitmapView bv) {

        if(bv != null){

            bv.imageView.setImageBitmap(bv.bitmap);
        }

    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if(height > reqHeight || width > reqWidth){

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth){
                inSampleSize *= 2;
            }

        }


        return inSampleSize;

    }

    class BitmapView {
        public Bitmap bitmap;
        public ImageView imageView;
    }
}
