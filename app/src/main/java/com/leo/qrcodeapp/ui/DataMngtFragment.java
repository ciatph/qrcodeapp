package com.leo.qrcodeapp.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leo.qrcodeapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataMngtFragment extends Fragment {


    public DataMngtFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_mngt, container, false);
    }

}
