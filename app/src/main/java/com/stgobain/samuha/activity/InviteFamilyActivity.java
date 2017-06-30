package com.stgobain.samuha.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.stgobain.samuha.CustomUserInterface.CustomEditTextView;
import com.stgobain.samuha.R;
import com.stgobain.samuha.utility.AppUtils;
import com.stgobain.samuha.utility.SharedPrefsUtils;
import com.stgobain.samuha.network.NetworkService;
import com.stgobain.samuha.network.NetworkServiceResultReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import static com.stgobain.samuha.utility.AppUtils.INTENET_ERROR;
import static com.stgobain.samuha.utility.AppUtils.INVITE_FAMILY_URL;
import static com.stgobain.samuha.utility.AppUtils.KEY_ERROR;
import static com.stgobain.samuha.utility.AppUtils.KEY_RECIVER;
import static com.stgobain.samuha.utility.AppUtils.KEY_REQUEST_ID;
import static com.stgobain.samuha.utility.AppUtils.KEY_RESPONSE;
import static com.stgobain.samuha.utility.AppUtils.KEY_RESULT;
import static com.stgobain.samuha.utility.AppUtils.SERVICE_REQUEST_INVITE_FAMILY;
import static com.stgobain.samuha.utility.AppUtils.SKEY_ID;
import static com.stgobain.samuha.utility.AppUtils.SKEY_USER_ID;
import static com.stgobain.samuha.utility.AppUtils.STATUS_ERROR;
import static com.stgobain.samuha.utility.AppUtils.STATUS_FINISHED;
import static com.stgobain.samuha.utility.AppUtils.STATUS_RUNNING;

/**
 * Created by vignesh on 23-06-2017.
 */

public class InviteFamilyActivity extends AppCompatActivity implements NetworkServiceResultReceiver.Receiver {
    private NetworkServiceResultReceiver mReceiver;
    CustomEditTextView mobileEditTextView1;
    CustomEditTextView mobileEditTextView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_family);
        mobileEditTextView1 = (CustomEditTextView) findViewById(R.id.etxtmobileno1);
        mobileEditTextView2 = (CustomEditTextView) findViewById(R.id.etxtmobileno2);
    }

    public void onInviteClicked(View v) {
        String mobileNo1 = mobileEditTextView1.getText().toString().trim();
        String mobileNo2 = mobileEditTextView2.getText().toString().trim();
        if (!TextUtils.isEmpty(mobileNo1) && !TextUtils.isEmpty(mobileNo2)) {
            if (isValidMobile(mobileNo1) && isValidMobile(mobileNo2)) {
                if (AppUtils.isNetworkAvailable(InviteFamilyActivity.this)) {
                    if (isValidMobile(mobileNo1) && isValidMobile(mobileNo2)) {
                        inviteFamily(mobileNo1, mobileNo2);
                        Toast.makeText(InviteFamilyActivity.this,"BOTH VALID",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AppUtils.showAlertDialog(InviteFamilyActivity.this, INTENET_ERROR);
                }
            } else {
                if (!isValidMobile(mobileNo1)&&!isValidMobile(mobileNo2)) {
              //      mobileEditTextView1.setError(getString(R.string.eneter_valid_mobile_no));
                    AppUtils.showAlertDialog(InviteFamilyActivity.this, getString(R.string.eneter_valid_mobile_no));
                }
              //  AppUtils.showAlertDialog(InviteFamilyActivity.this, getString(R.string.mobile_not_empty));
            }
        } else if (TextUtils.isEmpty(mobileNo1) && TextUtils.isEmpty(mobileNo2)) {
            AppUtils.showAlertDialog(InviteFamilyActivity.this, getString(R.string.enter_atleast_one_number));
        } else {
            if (!TextUtils.isEmpty(mobileNo1)&&TextUtils.isEmpty(mobileNo2)) {
                if(isValidMobile(mobileNo1)){
                inviteFamily(mobileNo1, "");}
                else{
                    AppUtils.showAlertDialog(InviteFamilyActivity.this, getString(R.string.eneter_valid_mobile_no));
                }
                Toast.makeText(InviteFamilyActivity.this,"mobileNo1",Toast.LENGTH_SHORT).show();
            } else {
                if (!TextUtils.isEmpty(mobileNo2) && TextUtils.isEmpty(mobileNo1)) {
                    if (isValidMobile(mobileNo2)) {
                        inviteFamily("", mobileNo1);
                    } else {
                        AppUtils.showAlertDialog(InviteFamilyActivity.this, getString(R.string.eneter_valid_mobile_no));
                    }
                }
            }
        }
    }

    private void inviteFamily(String mobile1, String mobile2) {
        String userId= SharedPrefsUtils.getStringPreference(InviteFamilyActivity.this, SKEY_ID);
        JSONObject inviteJsonObject = new JSONObject();
        try {
            inviteJsonObject.put(SKEY_USER_ID, userId);
            inviteJsonObject.put("mobile_1", mobile1);
            inviteJsonObject.put("mobile_2", mobile2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestWebservice(inviteJsonObject.toString(), SERVICE_REQUEST_INVITE_FAMILY, INVITE_FAMILY_URL);
    }

    private void requestWebservice(String request, int reqID, String url) {
        this.mReceiver = new NetworkServiceResultReceiver(new Handler());
        this.mReceiver.setReceiver(this);
        Intent intent = new Intent("android.intent.action.SYNC", null, InviteFamilyActivity.this, NetworkService.class);
        intent.putExtra("url", url);
        intent.putExtra(KEY_RECIVER, this.mReceiver);
        intent.putExtra(KEY_REQUEST_ID, String.valueOf(reqID));
        intent.putExtra("request", request);
        startService(intent);
    }

    private boolean isValidMobile(String phone) {
        if (phone.length() <= 9) {
            return false;
        }
        return true;
    }

    public void onSkipClicked(View v) {
        // SharedPrefsUtils.setBooleanPreference(LoginActivity.this,KEY_IS_LOGED_IN,true);
        Intent i = new Intent(InviteFamilyActivity.this, BaseActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
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
                String response = "";
                try {
                    status = new JSONObject(result).getString(KEY_ERROR);
                    response = new JSONObject(result).getString(KEY_RESPONSE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status.equals("false")) {
                    // SharedPrefsUtils.setBooleanPreference(InviteFamilyActivity.this,KEY_IS_LOGED_IN,true);
                    Intent i = new Intent(InviteFamilyActivity.this, BaseActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    AppUtils.showAlertDialog(InviteFamilyActivity.this, "InviteFailed. Try Again!");
                }
                Log.d("LOGIN", "FINISHED status " + status + response);
                break;
            case STATUS_ERROR:
                AppUtils.showAlertDialog(InviteFamilyActivity.this, "Login Failed. Try Again!");
                Log.d("LOGIN", "STATUS_ERROR");
                Log.d("LOGIN", "SERVICE RESPONSE ERROR " + resultData.getString("android.intent.extra.TEXT"));
                break;
        }
    }
}
