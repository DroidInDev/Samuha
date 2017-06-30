package com.stgobain.samuha.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.stgobain.samuha.CustomUserInterface.CustomFontTextView;
import com.stgobain.samuha.CustomUserInterface.CustomGrid;
import com.stgobain.samuha.Model.Parser;
import com.stgobain.samuha.Model.Team;
import com.stgobain.samuha.R;
import com.stgobain.samuha.activity.TeamDescriptionActivity;
import com.stgobain.samuha.network.NetworkService;
import com.stgobain.samuha.network.NetworkServiceResultReceiver;
import com.stgobain.samuha.utility.AppUtils;
import com.stgobain.samuha.utility.SharedPrefsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.stgobain.samuha.utility.AppUtils.KEY_ERROR;
import static com.stgobain.samuha.utility.AppUtils.KEY_RECIVER;
import static com.stgobain.samuha.utility.AppUtils.KEY_REQUEST_ID;
import static com.stgobain.samuha.utility.AppUtils.KEY_RESULT;
import static com.stgobain.samuha.utility.AppUtils.SERVICE_REQUEST_TEAMS;
import static com.stgobain.samuha.utility.AppUtils.SKEY_EVENT_DATE;
import static com.stgobain.samuha.utility.AppUtils.SKEY_ID;
import static com.stgobain.samuha.utility.AppUtils.STATUS_ERROR;
import static com.stgobain.samuha.utility.AppUtils.STATUS_FINISHED;
import static com.stgobain.samuha.utility.AppUtils.STATUS_RUNNING;
import static com.stgobain.samuha.utility.AppUtils.TEAMS_URL;

/**
 * Created by vignesh on 15-06-2017.
 */

public class TeamFragment extends Fragment implements NetworkServiceResultReceiver.Receiver, View.OnClickListener {
    private NetworkServiceResultReceiver mReceiver;
    private ProgressDialog progressDialog;
    GridView grid;
    String[] teams = {
            "Tech Now",
            "Digi Now",
            "Cyber Now",
            "Beta Now"
    };
    String[] score = new String[4];
    int[] imageId = {
            R.drawable.tech_now_flag,
            R.drawable.digi_now_flag,
            R.drawable.cyper_now_flag,
            R.drawable.beta_now_flag,
    };
    ArrayList<Team> teamsList = new ArrayList<>();
    CustomGrid adapter;
    CustomFontTextView techNowTxt;
    CustomFontTextView digiNowTxt;
    CustomFontTextView cyberNowTxt;
    CustomFontTextView betaNowTxt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_team, container, false);
        ImageView techNowImg = (ImageView)layout.findViewById(R.id.imgtechNow);
        ImageView digiNowImg = (ImageView)layout.findViewById(R.id.imgDigiNow);
        ImageView cyberNowImg = (ImageView)layout.findViewById(R.id.imgCyberNow);
        ImageView betaNowImg = (ImageView)layout.findViewById(R.id.imgBetsNow);
        techNowImg.setOnClickListener(this);
        digiNowImg.setOnClickListener(this);
        cyberNowImg.setOnClickListener(this);
        betaNowImg.setOnClickListener(this);
        techNowTxt = (CustomFontTextView)layout.findViewById(R.id.txtTechNow);
        digiNowTxt = (CustomFontTextView)layout.findViewById(R.id.txtDigiNow);
        cyberNowTxt = (CustomFontTextView)layout.findViewById(R.id.txtCyberNow);
        betaNowTxt = (CustomFontTextView)layout.findViewById(R.id.txtBetsNow);
     /*   grid = (GridView) layout.findViewById(R.id.grid);
        // grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Team team = null;
                for (int i = 0; i < teamsList.size(); i++) {
                    if(i==position)
                    team = teamsList.get(i);
                }
                //  Toast.makeText(getActivity(), "You Clicked at " +teams[+ position], Toast.LENGTH_SHORT).show();


            }
        });*/

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return layout;
    }
    private void startTeamDescActivity(Team team,int position){
        Intent intent = new Intent(getActivity(), TeamDescriptionActivity.class);
        intent.putExtra("Tittle", teams[+position]);
        intent.putExtra("Position", position);
        intent.putExtra("TeamObject",team);
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      //  requestTeam();
    }

    private void requestWebservice(String request, int reqID, String url) {
        this.mReceiver = new NetworkServiceResultReceiver(new Handler());
        this.mReceiver.setReceiver(this);
        Intent intent = new Intent("android.intent.action.SYNC", null, getActivity(), NetworkService.class);
        intent.putExtra("url", url);
        intent.putExtra(KEY_RECIVER, this.mReceiver);
        intent.putExtra(KEY_REQUEST_ID, String.valueOf(reqID));
        intent.putExtra("request", request);
        getActivity().startService(intent);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // setUpPagination();
        if (isVisibleToUser) {
            if (getActivity() != null) {
                if (this.progressDialog == null) {
                    this.progressDialog = AppUtils.createProgressDialog(getActivity());
                    this.progressDialog.show();
                } else {
                    this.progressDialog.show();
                }
                requestTeam();
            }

        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case STATUS_RUNNING:
                Log.d("LOGIN", "STATUS_RUNNING");
                break;
            case STATUS_FINISHED:
                Log.d("LOGIN", "FINISHED");
                String result = resultData.getString(KEY_RESULT);
                String status = "0";
                try {
                    status = new JSONObject(result).getString(KEY_ERROR);
                    Log.d("TEAMS", result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status.equals("false")) {
                    teamsList = Parser.getTeams(result);
                    for (int i = 0; i < teamsList.size(); i++) {
                        score[i] = teamsList.get(i).getScore();
                        switch (i){
                            case 0:
                                techNowTxt.setText(score[i]);
                                break;
                            case 1:
                                digiNowTxt.setText(score[i]);
                                break;
                            case 2:
                                cyberNowTxt.setText(score[i]);
                                break;
                            case 3:
                                betaNowTxt.setText(score[i]);
                                break;
                        }
                    }
                 //   displayTeam();
                 /*   Team t = Parser.getTeam(result, position);
                    if (t != null)
                        displayTeam(t);*/
                    if (this.progressDialog != null) {
                        this.progressDialog.dismiss();
                    }
                } else {

                    AppUtils.showAlertDialog(getActivity(), "Network Error!. Try Again!");
                }
                Log.d("LOGIN", "FINISHED status " + status + " ");
                break;
            case STATUS_ERROR:
                if (this.progressDialog != null) {
                    progressDialog.dismiss();
                }
                AppUtils.showAlertDialog(getActivity(), "Network Error!. Try Again!");
                Log.d("LOGIN", "STATUS_ERROR");
                Log.d("LOGIN", "SERVICE RESPONSE ERROR " + resultData.getString("android.intent.extra.TEXT"));
                break;
        }
    }

    private void displayTeam() {
        adapter = new CustomGrid(getActivity(), score, imageId);
        grid.setAdapter(adapter);
    }

    private void requestTeam() {
        String userId = SharedPrefsUtils.getStringPreference(getActivity(), SKEY_ID);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SKEY_ID, userId);
            jsonObject.put(SKEY_EVENT_DATE, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestWebservice(jsonObject.toString(), SERVICE_REQUEST_TEAMS, TEAMS_URL);
    }

    @Override
    public void onClick(View view) {
        Team team = null;
        if(view.getId()==R.id.imgtechNow){
            team = teamsList.get(0);
            startTeamDescActivity(team,0);
        }else if(view.getId()==R.id.imgDigiNow){
            team = teamsList.get(1);
            startTeamDescActivity(team,1);
        }else if(view.getId()==R.id.imgCyberNow){
            team = teamsList.get(2);
            startTeamDescActivity(team,2);
        }else if(view.getId()==R.id.imgBetsNow){
            team = teamsList.get(3);
            startTeamDescActivity(team,3);
        }
    }
}
