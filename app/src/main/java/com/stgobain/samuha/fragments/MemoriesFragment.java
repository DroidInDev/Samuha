package com.stgobain.samuha.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.stgobain.samuha.R;
import com.stgobain.samuha.activity.FeedMemoriesActivity;
import com.stgobain.samuha.activity.MediaUploadActivity;

import static com.stgobain.samuha.R.id.btnGallery;
import static com.stgobain.samuha.R.id.btnUpload;

/**
 * Created by vignesh on 22-06-2017.
 */

public class MemoriesFragment extends Fragment implements View.OnClickListener {
    Button BtnUpload;
    Button BtnGallery;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_memories, container, false);
        BtnUpload = (Button)layout.findViewById(btnUpload);
        BtnUpload.setOnClickListener(this);
        BtnGallery = (Button)layout.findViewById(btnGallery);
        BtnGallery.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.btnUpload) {
            Intent intent = new Intent(getActivity(), MediaUploadActivity.class);
            getActivity().startActivity(intent);
        }
        else if(view.getId()== R.id.btnGallery){
            Intent intent = new Intent(getActivity(), FeedMemoriesActivity.class);
            getActivity().startActivity(intent);
        }
        /*
        if(view.getId()==R.id.openBtn){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View promptView = layoutInflater.inflate(R.layout.custom_mobile_no_input_dialog, null);

            final AlertDialog alertD = new AlertDialog.Builder(getActivity(),R.style.AppTheme).create();
            alertD.setTitle("Invite Family");
            CustomEditTextView mobileno2 = (CustomEditTextView) promptView.findViewById(R.id.etxtmobileno2);
            CustomEditTextView mobileno1 = (CustomEditTextView) promptView.findViewById(R.id.etxtmobileno1);

            Button btnAdd1 = (Button) promptView.findViewById(R.id.btnInvite);

            Button btnAdd2 = (Button) promptView.findViewById(R.id.btnSkip);

            btnAdd1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    // btnAdd1 has been clicked
                    alertD.dismiss();
                }
            });

            btnAdd2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    alertD.dismiss();
                    // btnAdd2 has been clicked

                }
            });

            alertD.setView(promptView);
            alertD.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            mobileno1.requestFocus();
            alertD.show();
        }*/
    }
}
