package com.map.woodlands.woodlandsroad.Data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.map.woodlands.woodlandsroad.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Jimmy on 5/22/2015.
 */
public class ImagePopup {

    public Context mContext;
    public View mView;
    public Photo mPhoto;
    public ArrayList<Photo> mPhotos;
    public ArrayList<Photo> mRemovedPhotos;
    public LinearLayout mGallery;

    public ImagePopup(Context context, View view, ArrayList<Photo> photos, ArrayList<Photo> removedPhotos, LinearLayout gallery){

        mContext = context;

        mView = view;

        mPhotos = photos;

        mRemovedPhotos = removedPhotos;


        mPhoto = photos.get(view.getId());

        mGallery = gallery;

    }

    public void showPopup(){
        PopupMenu popupMenu = new PopupMenu(mContext, mView);

        popupMenu.getMenuInflater().inflate(R.menu.image_popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.add_desc:
//                        Toast.makeText(mContext, "Add Description " + mView.getId(), Toast.LENGTH_SHORT).show();
                        showDialog();
                        break;

                    case R.id.delete:

                        mRemovedPhotos.add(mPhoto);
//                        mPhotos.remove(mPhoto);
                        mGallery.removeView(mView);
//                        removePhotoFile(mPhoto);
                        Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                        break;

                }

                return true;
            }
        });

        popupMenu.show();
    }


    private void showDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Description");

        final EditText editText = new EditText(mContext);
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);

        editText.setLayoutParams(params);
        if(mPhoto.description != null) {
            editText.setText(mPhoto.description);
        }

        builder.setView(editText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPhoto.description = editText.getText().toString();
                Toast.makeText(mContext, mPhoto.description, Toast.LENGTH_SHORT).show();

                TextView textView = (TextView) mView.findViewById(R.id.image_desc);
                textView.setText(mPhoto.description);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    public void removePhotoFile(Photo p){
        if(p != null){
            try {
                File file = new File(p.path);
                file.delete();

            }catch (Exception e){

            }
        }
    }
}
