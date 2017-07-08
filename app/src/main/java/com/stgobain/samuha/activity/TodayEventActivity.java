package com.stgobain.samuha.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.stgobain.samuha.Model.Parser;
import com.stgobain.samuha.Model.SamuhaEvent;
import com.stgobain.samuha.R;
import com.stgobain.samuha.utility.AppUtils;
import com.stgobain.samuha.utility.SharedPrefsUtils;
import com.stgobain.samuha.adapter.TodayEventAdapter;
import com.stgobain.samuha.network.NetworkService;
import com.stgobain.samuha.network.NetworkServiceResultReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.stgobain.samuha.utility.AppUtils.EVENTS_URL;
import static com.stgobain.samuha.utility.AppUtils.EVENTS_URL_FAMILY;
import static com.stgobain.samuha.utility.AppUtils.KEY_ERROR;
import static com.stgobain.samuha.utility.AppUtils.KEY_RECIVER;
import static com.stgobain.samuha.utility.AppUtils.KEY_REQUEST_ID;
import static com.stgobain.samuha.utility.AppUtils.KEY_RESULT;
import static com.stgobain.samuha.utility.AppUtils.SERVICE_REQUEST_TODAY_EVENT;
import static com.stgobain.samuha.utility.AppUtils.SKEY_EVENT_DATE;
import static com.stgobain.samuha.utility.AppUtils.SKEY_ID;
import static com.stgobain.samuha.utility.AppUtils.STATUS_ERROR;
import static com.stgobain.samuha.utility.AppUtils.STATUS_FINISHED;
import static com.stgobain.samuha.utility.AppUtils.STATUS_RUNNING;

/**
 * Created by vignesh on 25-06-2017.
 */

public class TodayEventActivity extends AppCompatActivity implements NetworkServiceResultReceiver.Receiver {
    private NetworkServiceResultReceiver mReceiver;
    private ProgressDialog progressDialog;
    private ArrayList<SamuhaEvent> todayEventList = new ArrayList<>();
    private TodayEventAdapter todayEventAdapter;
    private RecyclerView recyclerView;
    String fromEventType ="";
    String eventType ="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String activityTittle = "Today's Events";

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            activityTittle = extras.getString("Tittle");
            fromEventType = extras.getString("From");
            eventType = extras.getString("Type");
        }
        if(fromEventType.equals("Event"))
        {
         //   todayEventList = extras.getParcelableArrayList("EventList");
        }

        getSupportActionBar().setTitle(activityTittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recylerviewTodayEVent);
        recyclerView.setLayoutManager(new LinearLayoutManager(TodayEventActivity.this));
        todayEventAdapter = new TodayEventAdapter(TodayEventActivity.this);
        recyclerView.setAdapter(todayEventAdapter);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fromEventType.equals("Event")) {

            requestFamilyEvent(EVENTS_URL_FAMILY,eventType);
        }else {
            requestOtherEvents(EVENTS_URL,"");
        }
    }

    private void requestOtherEvents(String url,String eType) {
        if (this.progressDialog == null) {
            this.progressDialog = AppUtils.createProgressDialog(TodayEventActivity.this);
            this.progressDialog.show();
        } else {
            this.progressDialog.show();
        }
        String userId = SharedPrefsUtils.getStringPreference(TodayEventActivity.this, SKEY_ID);
        String sDate = getDate();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SKEY_EVENT_DATE,sDate);
            jsonObject.put("type",eType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("TODAY_EVENT ", jsonObject.toString());
        requestWebservice(jsonObject.toString(), SERVICE_REQUEST_TODAY_EVENT, url);
    }
    private void requestFamilyEvent(String url,String eType) {
        if (this.progressDialog == null) {
            this.progressDialog = AppUtils.createProgressDialog(TodayEventActivity.this);
            this.progressDialog.show();
        } else {
            this.progressDialog.show();
        }
        String userId = SharedPrefsUtils.getStringPreference(TodayEventActivity.this, SKEY_ID);
        String sDate = getDate();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type",eType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("TODAY_EVENT ", jsonObject.toString());
        requestWebservice(jsonObject.toString(), SERVICE_REQUEST_TODAY_EVENT, url);
    }
    private void requestWebservice(String request, int reqID, String url) {
        this.mReceiver = new NetworkServiceResultReceiver(new Handler());
        this.mReceiver.setReceiver(this);
        Intent intent = new Intent("android.intent.action.SYNC", null, TodayEventActivity.this, NetworkService.class);
        intent.putExtra("url", url);
        intent.putExtra(KEY_RECIVER, this.mReceiver);
        intent.putExtra(KEY_REQUEST_ID, String.valueOf(reqID));
        intent.putExtra("request", request);
        startService(intent);
    }

    private String getDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTime());
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
                JSONObject response;
                Log.d("TODAY_EVENT ", result);
                try {
                    status = new JSONObject(result).getString(KEY_ERROR);
                    //resultString = new JSONObject(result).getString("response");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status.equals("false")) {
                    JSONObject resultString = null;
                    try {
                        resultString = new JSONObject(result).getJSONObject("response");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    todayEventList = Parser.getEventArrayList(result);
                    todayEventAdapter.setTodayEvents(todayEventList);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (this.progressDialog != null) {
                        this.progressDialog.dismiss();
                    }
                } else {
                    AppUtils.showAlertDialog(TodayEventActivity.this, "Events will be updated Shortly!");
                   /* SharedPrefsUtils.setBooleanPreference(LoginActivity.this, KEY_IS_LOGED_IN, false);
                    AppUtils.showAlertDialog(LoginActivity.this, "Login Failed. Try Again!");*/
                }
                if (this.progressDialog != null) {
                    this.progressDialog.dismiss();
                }
                Log.d("LOGIN", "FINISHED status " + status + " " );
                break;
            case STATUS_ERROR:
                if (this.progressDialog != null) {
                    this.progressDialog.dismiss();
                }
                AppUtils.showAlertDialog(TodayEventActivity.this, AppUtils.NETWORK_ERROR);
                Log.d("LOGIN", "STATUS_ERROR");
                Log.d("LOGIN", "SERVICE RESPONSE ERROR " + resultData.getString("android.intent.extra.TEXT"));
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
