package com.lapic.thomas.remote_control_app;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by thomasmarquesbrandaoreis on 27/09/16.
 */

public class RCViewFragment extends Fragment {

    public static RCViewFragment newInstance(int index) {
        RCViewFragment fragment = new RCViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments().getInt("index", 0) == 0) {
            View view = inflater.inflate(R.layout.fragment_main_rc, container, false);
            return view;
        } else if (getArguments().getInt("index", 0) == 1) {
            View view = inflater.inflate(R.layout.fragment_lineartv_rc, container, false);
            return view;
        } else if (getArguments().getInt("index", 0) == 2) {
            View view = inflater.inflate(R.layout.fragment_ondemand_rc, container, false);
            return view;
        } else  {
            View view = inflater.inflate(R.layout.fragment_input_rc, container, false);
            return view;
        }
    }

}
