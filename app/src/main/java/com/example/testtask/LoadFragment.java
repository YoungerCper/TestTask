package com.example.testtask;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wang.avi.AVLoadingIndicatorView;

public class LoadFragment extends Fragment {

    private AVLoadingIndicatorView avi;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.load_fragment,
                container, false);
        this.avi = view.findViewById(R.id.loader);
        avi.show();
        return view;
    }
}
