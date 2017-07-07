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

import com.stgobain.samuha.Model.HubUpdatesData;
import com.stgobain.samuha.Model.Parser;
import com.stgobain.samuha.R;
import com.stgobain.samuha.utility.AppUtils;
import com.stgobain.samuha.utility.SharedPrefsUtils;
import com.stgobain.samuha.adapter.HubUpdateAdapter;
import com.stgobain.samuha.network.NetworkService;
import com.stgobain.samuha.network.NetworkServiceResultReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.stgobain.samuha.utility.AppUtils.HUB_UPDATESS_URL;
import static com.stgobain.samuha.utility.AppUtils.KEY_ERROR;
import static com.stgobain.samuha.utility.AppUtils.KEY_RECIVER;
import static com.stgobain.samuha.utility.AppUtils.KEY_REQUEST_ID;
import static com.stgobain.samuha.utility.AppUtils.KEY_RESULT;
import static com.stgobain.samuha.utility.AppUtils.SERVICE_REQUEST_HUB_UPDATES;
import static com.stgobain.samuha.utility.AppUtils.SKEY_ID;
import static com.stgobain.samuha.utility.AppUtils.STATUS_ERROR;
import static com.stgobain.samuha.utility.AppUtils.STATUS_FINISHED;
import static com.stgobain.samuha.utility.AppUtils.STATUS_RUNNING;

/**
 * Created by vignesh on 28-06-2017.
 */

public class HubUpdatesActivity extends AppCompatActivity implements NetworkServiceResultReceiver.Receiver {
    private NetworkServiceResultReceiver mReceiver;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private HubUpdateAdapter hubUpdateAdapter;
    ArrayList<HubUpdatesData> hubUpdateList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_updates);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        String activityTittle = "Updates";
        getSupportActionBar().setTitle(activityTittle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recylerviewHubUpdates);
        recyclerView.setLayoutManager(new LinearLayoutManager(HubUpdatesActivity.this));
        hubUpdateAdapter = new HubUpdateAdapter(HubUpdatesActivity.this);
        recyclerView.setAdapter(hubUpdateAdapter);
    }

    @Override
    protected void onResume() {

        super.onResume();
        requestUpdates();
    }

    private void requestUpdates() {
        if (this.progressDialog == null) {
            this.progressDialog = AppUtils.createProgressDialog(HubUpdatesActivity.this);
            this.progressDialog.show();
        } else {
            this.progressDialog.show();
        }
        String userId = SharedPrefsUtils.getStringPreference(HubUpdatesActivity.this, SKEY_ID);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "update");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestWebservice(jsonObject.toString(), SERVICE_REQUEST_HUB_UPDATES, HUB_UPDATESS_URL);
    }

    private void requestWebservice(String request, int reqID, String url) {
        this.mReceiver = new NetworkServiceResultReceiver(new Handler());
        this.mReceiver.setReceiver(this);
        Intent intent = new Intent("android.intent.action.SYNC", null, HubUpdatesActivity.this, NetworkService.class);
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
                String resultString;
                JSONObject response;

                try {
                    status = new JSONObject(result).getString(KEY_ERROR);
                    resultString = new JSONObject(result).getString("response");
                    response = new JSONObject(resultString);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status.equals("false")) {
                    hubUpdateList = Parser.getHubUpdates(result);
                    hubUpdateAdapter.setUpdates(hubUpdateList);
                    //   recyclerView.setVisibility(View.VISIBLE);
                    if (this.progressDialog != null) {
                        this.progressDialog.dismiss();
                    }
                } else {
                    if (this.progressDialog != null) {
                        this.progressDialog.dismiss();
                    }
                   /* SharedPrefsUtils.setBooleanPreference(LoginActivity.this, KEY_IS_LOGED_IN, false);
                    AppUtils.showAlertDialog(LoginActivity.this, "Login Failed. Try Again!");*/
                    AppUtils.showAlertDialog(HubUpdatesActivity.this, "No Data Found!");
                }
                Log.d("LOGIN", "FINISHED status " + status + " ");
                break;
            case STATUS_ERROR:
                if (this.progressDialog != null) {
                    this.progressDialog.dismiss();
                }
                AppUtils.showAlertDialog(HubUpdatesActivity.this, "Network Error. Try Again!");
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
