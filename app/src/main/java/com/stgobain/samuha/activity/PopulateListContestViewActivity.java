package com.stgobain.samuha.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.stgobain.samuha.CustomUserInterface.CustomFontTextView;
import com.stgobain.samuha.Model.ContextData;
import com.stgobain.samuha.Model.Parser;
import com.stgobain.samuha.R;
import com.stgobain.samuha.Utility.AppUtils;
import com.stgobain.samuha.Utility.SharedPrefsUtils;
import com.stgobain.samuha.adapter.PopulateListAdapter;
import com.stgobain.samuha.network.NetworkService;
import com.stgobain.samuha.network.NetworkServiceResultReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.stgobain.samuha.Utility.AppUtils.GET_CONTEXT_TO_UPLOAD_URL;
import static com.stgobain.samuha.Utility.AppUtils.GET_EVENTS_TO_UPLOAD_URL;
import static com.stgobain.samuha.Utility.AppUtils.KEY_ERROR;
import static com.stgobain.samuha.Utility.AppUtils.KEY_RECIVER;
import static com.stgobain.samuha.Utility.AppUtils.KEY_REQUEST_ID;
import static com.stgobain.samuha.Utility.AppUtils.KEY_RESULT;
import static com.stgobain.samuha.Utility.AppUtils.SERVICE_REQUEST_GET_CONTEXT_TO_UPLOAD;
import static com.stgobain.samuha.Utility.AppUtils.SERVICE_REQUEST_GET_EVENTS_TO_UPLOAD;
import static com.stgobain.samuha.Utility.AppUtils.SERVICE_REQUEST_POST_MEMORIES;
import static com.stgobain.samuha.Utility.AppUtils.SKEY_ID;
import static com.stgobain.samuha.Utility.AppUtils.STATUS_ERROR;
import static com.stgobain.samuha.Utility.AppUtils.STATUS_FINISHED;
import static com.stgobain.samuha.Utility.AppUtils.STATUS_RUNNING;

/**
 * Created by vignesh on 26-06-2017.
 */

public class PopulateListContestViewActivity extends AppCompatActivity implements NetworkServiceResultReceiver.Receiver {
    private static final String CONTEST = "contest";
    private NetworkServiceResultReceiver mReceiver;
    private ProgressDialog progressDialog;
    private static final String RESULT_CONTEXTODE = "contestlistitem";
    private ArrayList<ContextData> contestList;
    ListView contestListView;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_populate_list);
        CustomFontTextView tittleTxt =(CustomFontTextView)findViewById(R.id.txtListTittle);
        linearLayout = (LinearLayout)findViewById(R.id.layoutContestList);
        contestListView = (ListView)findViewById(R.id.listContest);
        Bundle b =getIntent().getExtras();
        String activityTittle = "";
        String type ="";
        if(b!=null){
            activityTittle = b.getString("tittle");
            type = b.getString("type");
        }
        tittleTxt.setText(activityTittle);
        linearLayout.setVisibility(View.INVISIBLE);
        if(type.equals("EVENT")){
            requestEventList();
        }else {
            getContests();
        }
    }
    private void loadListData(){
        linearLayout.setVisibility(View.VISIBLE);
        ArrayAdapter<ContextData> adapter = new PopulateListAdapter(this, contestList);
        final ArrayList<ContextData> finalContestList = contestList;
        contestListView.setAdapter(adapter);
        contestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContextData c = finalContestList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_CONTEXTODE, c.getName());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }
   /* private void loadListData(){
        ArrayAdapter<ContextData> adapter = new PopulateListAdapter(this, contestList);
        setListAdapter(adapter);
        final ArrayList<ContextData> finalContestList = contestList;
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContextData c = finalContestList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_CONTEXTODE, c.getName());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }*/
    private void getContests(){
        if (this.progressDialog == null) {
            this.progressDialog = AppUtils.createProgressDialog(PopulateListContestViewActivity.this);
            this.progressDialog.show();
        } else {
            this.progressDialog.show();
        }
        String userId = SharedPrefsUtils.getStringPreference(PopulateListContestViewActivity.this, SKEY_ID);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("type","contest");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestWebservice(jsonObject.toString(), SERVICE_REQUEST_GET_CONTEXT_TO_UPLOAD, GET_CONTEXT_TO_UPLOAD_URL);
    }
    private void requestEventList() {
        if (this.progressDialog == null) {
            this.progressDialog = AppUtils.createProgressDialog(PopulateListContestViewActivity.this);
            this.progressDialog.show();
        } else {
            this.progressDialog.show();
        }
        String userId = SharedPrefsUtils.getStringPreference(PopulateListContestViewActivity.this, SKEY_ID);
        JSONObject jsonObject = new JSONObject();
        try {
            //   jsonObject.put("event_date","2017-06-22");
            jsonObject.put("id",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestWebservice(jsonObject.toString(), SERVICE_REQUEST_GET_EVENTS_TO_UPLOAD, GET_EVENTS_TO_UPLOAD_URL);
    }
    private void requestWebservice(String request, int reqID, String url) {
        this.mReceiver = new NetworkServiceResultReceiver(new Handler());
        this.mReceiver.setReceiver(this);
        Intent intent = new Intent("android.intent.action.SYNC", null, PopulateListContestViewActivity.this, NetworkService.class);
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
                Log.d(CONTEST, "STATUS_RUNNING");
                break;
            case STATUS_FINISHED:

                String result = resultData.getString(KEY_RESULT);
                Log.d(CONTEST, "FINISHED"+result);
                int requestId = Integer.valueOf(resultData.getString(KEY_REQUEST_ID));
                String status = "0";
                try {
                    status = new JSONObject(result).getString(KEY_ERROR);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status.equals("true")) {
                    AppUtils.showAlertDialog(PopulateListContestViewActivity.this, "Login Failed. Try Again!");
                }
                Log.d(CONTEST, "FINISHED status " + status + " ");
                switch (requestId) {
                    case SERVICE_REQUEST_POST_MEMORIES:
                        if (status.equals("false")) {
                            if (this.progressDialog != null) {
                                this.progressDialog.dismiss();
                            }
                        }
                        Log.d(CONTEST, "FINISHED status " + status + " ");
                        break;
                    case SERVICE_REQUEST_GET_CONTEXT_TO_UPLOAD:
                        if (status.equals("false")) {
                            contestList = Parser.getContestToUpload(result);
                            if (this.progressDialog != null) {
                                this.progressDialog.dismiss();
                            }
                            loadListData();

                        }
                        Log.d(CONTEST, "FINISHED status " + status + "CONTESTS "+contestList.size());
                        break;
                    case SERVICE_REQUEST_GET_EVENTS_TO_UPLOAD:
                        if (status.equals("false")) {
                            contestList = Parser.getEventsToUpload(result);
                            if (this.progressDialog != null) {
                                this.progressDialog.dismiss();
                            }
                            loadListData();

                        }
                        Log.d(CONTEST, "FINISHED status " + status + "CONTESTS "+contestList.size());
                        break;

                }
                break;

            case STATUS_ERROR:
                AppUtils.showAlertDialog(PopulateListContestViewActivity.this, "Login Failed. Try Again!");
                Log.d("LOGIN", "STATUS_ERROR");
                Log.d("LOGIN", "SERVICE RESPONSE ERROR " + resultData.getString("android.intent.extra.TEXT"));
                break;
        }
    }

}
