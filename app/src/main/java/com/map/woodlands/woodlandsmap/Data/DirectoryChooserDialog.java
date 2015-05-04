package com.map.woodlands.woodlandsmap.Data;

/**
 * Created by Jimmy on 4/8/2015.
 * Used for displaying a file chooser.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.map.woodlands.woodlandsmap.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DirectoryChooserDialog
{
    private boolean m_isNewFolderEnabled = true;
    private String m_sdcardDirectory = "";
    private Context m_context;
    private TextView m_titleView;

    protected String m_dir = "";
    private List<String> m_subdirs = null;
    protected ChosenDirectoryListener m_chosenDirectoryListener = null;
    private ArrayAdapter<String> m_listAdapter = null;
    protected AlertDialog dirsDialog;
    //////////////////////////////////////////////////////
    // Callback interface for selected directory
    //////////////////////////////////////////////////////
    public interface ChosenDirectoryListener
    {
        public void onChosenDir(String chosenDir);
    }

    public DirectoryChooserDialog(Context context, ChosenDirectoryListener chosenDirectoryListener)
    {
        m_context = context;
        m_sdcardDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        m_chosenDirectoryListener = chosenDirectoryListener;

        try
        {
            m_sdcardDirectory = new File(m_sdcardDirectory).getCanonicalPath();
        }
        catch (IOException ioe)
        {
        }
    }

    ///////////////////////////////////////////////////////////////////////
    // setNewFolderEnabled() - enable/disable new folder button
    ///////////////////////////////////////////////////////////////////////

    public void setNewFolderEnabled(boolean isNewFolderEnabled)
    {
        m_isNewFolderEnabled = isNewFolderEnabled;
    }

    public boolean getNewFolderEnabled()
    {
        return m_isNewFolderEnabled;
    }

    ///////////////////////////////////////////////////////////////////////
    // chooseDirectory() - load directory chooser dialog for initial
    // default sdcard directory
    ///////////////////////////////////////////////////////////////////////

    public void chooseDirectory()
    {
        // Initial directory is sdcard directory
        chooseDirectory(m_sdcardDirectory);
    }

    ////////////////////////////////////////////////////////////////////////////////
    // chooseDirectory(String dir) - load directory chooser dialog for initial
    // input 'dir' directory
    ////////////////////////////////////////////////////////////////////////////////

    public void chooseDirectory(String dir)
    {
        File dirFile = new File(dir);
        if (! dirFile.exists() || ! dirFile.isDirectory())
        {
            dir = m_sdcardDirectory;
        }

        try
        {
            dir = new File(dir).getCanonicalPath();
        }
        catch (IOException ioe)
        {
            return;
        }

        m_dir = dir;
        m_subdirs = getDirectories(dir);

        class DirectoryOnClickListener implements DialogInterface.OnClickListener
        {
            public void onClick(DialogInterface dialog, int item)
            {
                // Navigate into the sub-directory
                m_dir += "/" + ((AlertDialog) dialog).getListView().getAdapter().getItem(item);
                updateDirectory();
            }
        }

        AlertDialog.Builder dialogBuilder =
                createDirectoryChooserDialog(dir, m_subdirs, new DirectoryOnClickListener());

        dialogBuilder.setNegativeButton("Cancel", null);
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Current directory chosen
//                if (m_chosenDirectoryListener != null) {
//                    // Call registered listener supplied with the chosen directory
//                    m_chosenDirectoryListener.onChosenDir(m_dir);
//                }
//            }
//        });
//

        dirsDialog = dialogBuilder.create();

//        dirsDialog.setOnKeyListener(new OnKeyListener()
//        {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
//            {
//                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
//                {
//                    // Back button pressed
//                    if ( m_dir.equals(m_sdcardDirectory) )
//                    {
//                        // The very top level directory, do nothing
//                        return false;
//                    }
//                    else
//                    {
//                        // Navigate back to an upper directory
//                        m_dir = new File(m_dir).getParent();
//                        updateDirectory();
//                    }
//
//                    return true;
//                }
//                else
//                {
//                    return false;
//                }
//            }
//        });

        // Show directory chooser dialog
        dirsDialog.show();
    }

    private boolean createSubDir(String newDir)
    {
        File newDirFile = new File(newDir);
        if (! newDirFile.exists() )
        {
            return newDirFile.mkdir();
        }

        return false;
    }

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
//                if ( file.isDirectory() )
//                {
//                    dirs.add( file.getName() );
//                }
                String fileName = file.getName();
                if(file.isFile()){
                    // Filter files
                    int dotIndex = fileName.lastIndexOf(".");
                    if(dotIndex > -1){
                        String ext = fileName.substring(dotIndex).toLowerCase();
                        if(ext.contains("doc")
                                || ext.contains("docx")
                                || ext.contains("pdf")
                                || ext.contains("jpg")
                                || ext.contains("jpeg")
                                || ext.contains("png")
                                || ext.contains("xlsx")
                                || ext.contains("pst")
                                || ext.contains("flv")
                                || ext.contains("wmv")
                                || ext.contains("webm")
                                || ext.contains("mkv")
                                || ext.contains("vob")
                                || ext.contains("ogv")
                                || ext.contains("ogg")
                                || ext.contains("drc")
                                || ext.contains("mng")
                                || ext.contains("avi")
                                || ext.contains("mov")
                                || ext.contains("yuv")
                                || ext.contains("rm")
                                || ext.contains("rmvb")
                                || ext.contains("asf")
                                || ext.contains("mp4")
                                || ext.contains("m4p")
                                || ext.contains("m4v")
                                || ext.contains("mpg")
                                || ext.contains("mp2")
                                || ext.contains("mpeg")
                                || ext.contains("mpe")
                                || ext.contains("mpv")
                                || ext.contains("m2v")
                                || ext.contains("svi"))
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

        Collections.sort(dirs, new Comparator<String>()
        {
            public int compare(String o1, String o2)
            {
                return o1.compareTo(o2);
            }
        });

        return dirs;
    }

    private AlertDialog.Builder createDirectoryChooserDialog(String title, List<String> listItems,
                                                             DialogInterface.OnClickListener onClickListener)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(m_context);

        // Create custom view for AlertDialog title containing
        // current directory TextView and possible 'New folder' button.
        // Current directory TextView allows long directory path to be wrapped to multiple lines.
        LinearLayout titleLayout = new LinearLayout(m_context);
        titleLayout.setOrientation(LinearLayout.VERTICAL);


        LinearLayout topLayout = new LinearLayout(m_context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                            , LayoutParams.WRAP_CONTENT);
        topLayout.setWeightSum(1.0f);
        topLayout.setLayoutParams(layoutParams);
        topLayout.setOrientation(LinearLayout.HORIZONTAL);
//        topLayout.setBackgroundColor(m_context.getResources().getColor(R.color.blue));



        m_titleView = new TextView(m_context);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        textViewParams.weight = 0.8f;
        m_titleView.setLayoutParams(textViewParams);
        m_titleView.setTextAppearance(m_context, android.R.style.TextAppearance_Large);
        m_titleView.setTextColor( m_context.getResources().getColor(R.color.cblue) );
        m_titleView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        m_titleView.setText(title);

        Button backBtn = new Button(m_context);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT);
        btnParams.weight = 0.2f;
        backBtn.setLayoutParams(btnParams);
        backBtn.setText("Back");
        backBtn.setBackgroundColor(m_context.getResources().getColor(R.color.blue));
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to an upper directory
                m_dir = new File(m_dir).getParent();
                updateDirectory();
            }
        });




//        Button newDirButton = new Button(m_context);
//        newDirButton.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//        newDirButton.setText("New folder");
//        newDirButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                final EditText input = new EditText(m_context);
//
//                // Show new folder name input dialog
//                new AlertDialog.Builder(m_context).
//                        setTitle("New folder name").
//                        setView(input).setPositiveButton("OK", new DialogInterface.OnClickListener()
//                {
//                    public void onClick(DialogInterface dialog, int whichButton)
//                    {
//                        Editable newDir = input.getText();
//                        String newDirName = newDir.toString();
//                        // Create new directory
//                        if ( createSubDir(m_dir + "/" + newDirName) )
//                        {
//                            // Navigate into the new directory
//                            m_dir += "/" + newDirName;
//                            updateDirectory();
//                        }
//                        else
//                        {
//                            Toast.makeText(
//                                    m_context, "Failed to create '" + newDirName +
//                                            "' folder", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }).setNegativeButton("Cancel", null).show();
//            }
//        });
//
//        if (! m_isNewFolderEnabled)
//        {
//            newDirButton.setVisibility(View.GONE);
//        }

        topLayout.addView(m_titleView);
        topLayout.addView(backBtn);
        titleLayout.addView(topLayout);
//        titleLayout.addView(m_titleView);
//        titleLayout.addView(newDirButton);

        dialogBuilder.setCustomTitle(titleLayout);

        m_listAdapter = createListAdapter(listItems);

        dialogBuilder.setSingleChoiceItems(m_listAdapter, -1, onClickListener);
//        dialogBuilder.setCancelable(false);

        return dialogBuilder;
    }

    private void updateDirectory()
    {
        m_subdirs.clear();
        m_subdirs.addAll( getDirectories(m_dir) );
        m_titleView.setText(m_dir);

        m_listAdapter.notifyDataSetChanged();
    }

    private ArrayAdapter<String> createListAdapter(List<String> items)
    {
        return new ArrayAdapter<String>(m_context,
                android.R.layout.select_dialog_item, android.R.id.text1, items)
        {
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);

                if (v instanceof TextView)
                {
                    // Enable list item (directory) text wrapping
                    TextView tv = (TextView) v;
                    tv.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
                    tv.setEllipsize(null);
                }
                return v;
            }
        };
    }
}