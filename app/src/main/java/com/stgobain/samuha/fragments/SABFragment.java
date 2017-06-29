package com.stgobain.samuha.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stgobain.samuha.CustomUserInterface.CustomFontButton;
import com.stgobain.samuha.R;
import com.stgobain.samuha.activity.SabAuditionActivity;
import com.stgobain.samuha.activity.SabGalleryActivity;

/**
 * Created by vignesh on 22-06-2017.
 */

public class SABFragment extends Fragment implements View.OnClickListener {
    CustomFontButton auditionBtn;
    CustomFontButton galeryBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_sab, container, false);
        auditionBtn  = (CustomFontButton) layout.findViewById(R.id.btnAuditioins);
        galeryBtn = (CustomFontButton) layout.findViewById(R.id.btnSabGallery);
        galeryBtn.setOnClickListener(this);
        auditionBtn.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAuditioins) {
            Intent intent = new Intent(getActivity(), SabAuditionActivity.class);
            getActivity().startActivity(intent);
        } else if (view.getId() == R.id.btnSabGallery) {
            Intent intent = new Intent(getActivity(), SabGalleryActivity.class);
            getActivity().startActivity(intent);
        }
    }
}
