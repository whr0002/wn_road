package com.map.woodlands.woodlandsroad.settings;

import android.content.Context;
import android.util.AttributeSet;

import com.map.woodlands.woodlandsroad.R;

/**
 * Created by Jimmy on 4/8/2015.
 */
public class AboutCompany extends DialogDisclaimer
{

    public AboutCompany(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.about_company);

    }
}
