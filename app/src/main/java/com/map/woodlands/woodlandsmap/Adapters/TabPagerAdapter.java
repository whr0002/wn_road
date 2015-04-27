package com.map.woodlands.woodlandsmap.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.map.woodlands.woodlandsmap.Fragments.FormFragment;
import com.map.woodlands.woodlandsmap.Fragments.LoginFragment;
import com.map.woodlands.woodlandsmap.Fragments.MapFragment;
import com.map.woodlands.woodlandsmap.MainActivity;
import com.map.woodlands.woodlandsmap.R;

import java.util.Locale;

/**
 * Created by Jimmy on 3/9/2015.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {
    private LoginFragment mLoginFragment;
    private FormFragment mFormFragment;
    private MapFragment mMapFragment;
    private Context mCtx;
    public TabPagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        this.mCtx = ctx;

        this.mFormFragment = FormFragment.newInstance();
        this.mMapFragment = MapFragment.newInstance();
        this.mLoginFragment = LoginFragment.newInstance(mMapFragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position){
            case 0:
                return mCtx.getString(R.string.title_section1).toUpperCase(l);

            case 1:
                return mCtx.getString(R.string.title_section2).toUpperCase(l);

            case 2:
                return mCtx.getString(R.string.title_section3).toUpperCase(l);
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return mLoginFragment;

            case 1:
                return mMapFragment;

            case 2:
                return FormFragment.newInstance();


        }
        return MainActivity.PlaceholderFragment.newInstance(position+1);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
