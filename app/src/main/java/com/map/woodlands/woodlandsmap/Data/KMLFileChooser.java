package com.map.woodlands.woodlandsmap.Data;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jimmy on 4/29/2015.
 */
public class KMLFileChooser extends DirectoryChooserDialog{
    public KMLFileChooser(Context context, ChosenDirectoryListener chosenDirectoryListener) {
        super(context, chosenDirectoryListener);
    }

    @Override
    protected List<String> getDirectories(String dir)
    {
        List<String> dirs = new ArrayList<String>();

        try
        {
            File dirFile = new File(dir);
            if(dirFile.exists() && dirFile.isFile()){
                dirsDialog.dismiss();
                m_chosenDirectoryListener.onChosenDir(m_dir);
                return dirs;
            }
            if (! dirFile.exists() || ! dirFile.isDirectory())
            {
                return dirs;
            }

            for (File file : dirFile.listFiles())
            {
                String fileName = file.getName();
                if(file.isFile()){
                    // Filter files
                    int dotIndex = fileName.lastIndexOf(".");
                    if(dotIndex > -1){
                        String ext = fileName.substring(dotIndex).toLowerCase();
                        if(ext.contains("kml") || ext.contains("zip"))
                        {
                            dirs.add(fileName);
                        }
                    }

                }else if(file.isDirectory()) {
                    dirs.add(fileName);
                }
            }
        }
        catch (Exception e)
        {
        }

        Collections.sort(dirs, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        return dirs;
    }
}
