package com.map.woodlands.woodlandsmap.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.map.woodlands.woodlandsmap.Activities.pdfActivity;
import com.map.woodlands.woodlandsmap.Data.DirectoryChooserDialog;
import com.map.woodlands.woodlandsmap.Data.PDFChooser;
import com.map.woodlands.woodlandsmap.R;

import net.sf.andpdf.pdfviewer.PdfViewerActivity;

/**
 * Created by Jimmy on 4/30/2015.
 */
public class PDFFragment extends Fragment{
    private Activity mContext;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showOptions(mContext.findViewById(0));
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void showOptions(View v) {
        PopupMenu popupMenu = new PopupMenu(mContext,v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_layers, popupMenu.getMenu());
        Menu menu = popupMenu.getMenu();
        menu.add(1, 0, 0, "Choose...");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 0:
                        PDFChooser pdfChooser = new PDFChooser(mContext,
                                new DirectoryChooserDialog.ChosenDirectoryListener() {
                            @Override
                            public void onChosenDir(String chosenDir) {
                                Toast.makeText(mContext,
                                        "Choosen file: "+chosenDir,
                                        Toast.LENGTH_LONG).show();
                                openPdfActivity(chosenDir);

                            }
                        });

                        pdfChooser.setNewFolderEnabled(false);
                        pdfChooser.chooseDirectory("");

                        break;

                    default:
                        break;


                }

                return true;

            }
        });

        popupMenu.show();

    }

    public void openPdfActivity(String path){
        try {
            Intent intent = new Intent(mContext, pdfActivity.class);
            intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME, path);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0,0,0,"OPEN").setIcon(R.drawable.file_add)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pdf, container, false);
        this.mContext = this.getActivity();
        setHasOptionsMenu(true);
        return v;

    }
}
