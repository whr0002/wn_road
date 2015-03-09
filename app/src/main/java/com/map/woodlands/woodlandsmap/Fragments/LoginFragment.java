package com.map.woodlands.woodlandsmap.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.map.woodlands.woodlandsmap.R;

/**
 * Created by Jimmy on 3/9/2015.
 * This is used for Login
 */
public class  LoginFragment extends Fragment{

    public static LoginFragment newInstance(){
        LoginFragment mLoginFragment = new LoginFragment();

        return mLoginFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_login,container, false);
    }
}
