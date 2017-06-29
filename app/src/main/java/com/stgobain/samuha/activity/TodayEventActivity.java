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
import com.stgobain.samuha.Utility.AppUtils;
import com.stgobain.samuha.Utility.SharedPrefsUtils;
import com.stgobain.samuha.adapter.TodayEventAdapter;
import com.stgobain.samuha.network.NetworkService;
import com.stgobain.samuha.network.NetworkServiceResultReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.stgobain.samuha.Utility.AppUtils.EVENTS_URL;
import static com.stgobain.samuha.Utility.AppUtils.KEY_ERROR;
import static com.stgobain.samuha.Utility.AppUtils.KEY_RECIVER;
import static com.stgobain.samuha.Utility.AppUtils.KEY_REQUEST_ID;
import static com.stgobain.samuha.Utility.AppUtils.KEY_RESULT;
import static com.stgobain.samuha.Utility.AppUtils.SERVICE_REQUEST_TODAY_EVENT;
import static com.stgobain.samuha.Utility.AppUtils.SKEY_EVENT_DATE;
import static com.stgobain.samuha.Utility.AppUtils.SKEY_ID;
import static com.stgobain.samuha.Utility.AppUtils.STATUS_ERROR;
import static com.stgobain.samuha.Utility.AppUtils.STATUS_FINISHED;
import static com.stgobain.samuha.Utility.AppUtils.STATUS_RUNNING;

/**
 * Created by vignesh on 25-06-2017.
 */

public class TodayEventActivity extends AppCompatActivity implements NetworkServiceResultReceiver.Receiver {
    private NetworkServiceResultReceiver mReceiver;
    private ProgressDialog progressDialog;
    private ArrayList<SamuhaEvent> todayEventList = new ArrayList<>();
    private TodayEventAdapter todayEventAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        String activityTittle = "Today's Event";
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
        requestScore();
    }

    private void requestScore() {
        if (this.progressDialog == null) {
            this.progressDialog = AppUtils.createProgressDialog(TodayEventActivity.this);
            this.progressDialog.show();
        } else {
            this.progressDialog.show();
        }
        String userId = SharedPrefsUtils.getStringPreference(TodayEventActivity.this, SKEY_ID);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SKEY_ID, userId);
            jsonObject.put(SKEY_EVENT_DATE, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestWebservice(jsonObject.toString(), SERVICE_REQUEST_TODAY_EVENT, EVENTS_URL);
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
                   /* SharedPrefsUtils.setBooleanPreference(LoginActivity.this, KEY_IS_LOGED_IN, false);
                    AppUtils.showAlertDialog(LoginActivity.this, "Login Failed. Try Again!");*/
                }
                Log.d("LOGIN", "FINISHED status " + status + " " );
                break;
            case STATUS_ERROR:
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
