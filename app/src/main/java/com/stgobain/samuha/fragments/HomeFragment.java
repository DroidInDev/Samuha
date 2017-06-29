package com.stgobain.samuha.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.stgobain.samuha.R;
import com.stgobain.samuha.activity.AnnouncementActivity;
import com.stgobain.samuha.activity.ScoreActivity;
import com.stgobain.samuha.activity.TodayEventActivity;

/**
 * Created by vignesh on 15-06-2017.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_home, container, false);
        LinearLayout scoreLinearLayout = (LinearLayout)layout.findViewById(R.id.scoreLayout);
        LinearLayout eventLinearLayout = (LinearLayout)layout.findViewById(R.id.eventLayout);
        LinearLayout announceLinearLayout = (LinearLayout)layout.findViewById(R.id.announceLayout);
        scoreLinearLayout.setOnClickListener(this);
        eventLinearLayout.setOnClickListener(this);
        announceLinearLayout.setOnClickListener(this);
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return layout;
    }
    public void onScoreClicked(View view){

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.scoreLayout)
        {
            Intent intent = new Intent(getActivity(), ScoreActivity.class);
            getActivity().startActivity(intent);
        }else if(view.getId()==R.id.eventLayout){
            Intent intent = new Intent(getActivity(), TodayEventActivity.class);
            getActivity().startActivity(intent);
        }else if(view.getId()==R.id.announceLayout)
        {
            Intent intent = new Intent(getActivity(), AnnouncementActivity.class);
            getActivity().startActivity(intent);
        }else{

        }

    }
}
