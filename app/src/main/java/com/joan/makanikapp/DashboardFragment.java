package com.joan.makanikapp;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class DashboardFragment extends Fragment {

    @Override
   public ViewGroup onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_main_screen,container,false);
        return root;
    }
}