package com.appdroid.ssbtproject.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdroid.ssbtproject.R;

import androidx.fragment.app.Fragment;

public class ViewPager2Fragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_pager2, container, false);
        return view;
    }
}
