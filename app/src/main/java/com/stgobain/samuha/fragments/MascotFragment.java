package com.stgobain.samuha.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stgobain.samuha.R;

/**
 * Created by vignesh on 15-06-2017.
 */
/*home - image
theme ""
mascots home
team -mascots -remove team name
event- internal events coming soon

logo */

public class MascotFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mascot, container, false);
    }
}

