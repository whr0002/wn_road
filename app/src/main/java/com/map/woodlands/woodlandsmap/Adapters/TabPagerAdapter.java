package com.map.woodlands.woodlandsmap.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.map.woodlands.woodlandsmap.Fragments.LoginFragment;
import com.map.woodlands.woodlandsmap.MainActivity;
import com.map.woodlands.woodlandsmap.R;

import java.util.Locale;

/**
 * Created by Jimmy on 3/9/2015.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {
    private LoginFragment mLoginFragment;
    private Context mCtx;
    public TabPagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        this.mCtx = ctx;
        this.mLoginFragment = LoginFragment.newInstance();
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

            case 3:
                return mCtx.getString(R.string.title_section4).toUpperCase(l);
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return mLoginFragment;

            case 1:
                return MainActivity.PlaceholderFragment.newInstance(position + 1);

            case 2:
                return MainActivity.PlaceholderFragment.newInstance(position + 1);

            case 3:
                return MainActivity.PlaceholderFragment.newInstance(position + 1);
        }
        return MainActivity.PlaceholderFragment.newInstance(position+1);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
