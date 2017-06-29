package com.stgobain.samuha.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stgobain.samuha.CustomUserInterface.CustomFontButton;
import com.stgobain.samuha.R;
import com.stgobain.samuha.activity.HubContestsActivity;
import com.stgobain.samuha.activity.HubUpdatesActivity;

/**
 * Created by vignesh on 22-06-2017.
 */

public class HUBFragment extends Fragment implements View.OnClickListener {
    CustomFontButton contestBtn;
    CustomFontButton updatesBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_hub, container, false);
        contestBtn = (CustomFontButton) layout.findViewById(R.id.btnContests);
        updatesBtn = (CustomFontButton) layout.findViewById(R.id.btnUpdate);
        contestBtn.setOnClickListener(this);
        updatesBtn.setOnClickListener(this);
        return layout;

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnContests) {
            Intent intent = new Intent(getActivity(), HubContestsActivity.class);
            getActivity().startActivity(intent);
        } else if (view.getId() == R.id.btnUpdate) {
            Intent intent = new Intent(getActivity(), HubUpdatesActivity.class);
            getActivity().startActivity(intent);
        }
    }
}
