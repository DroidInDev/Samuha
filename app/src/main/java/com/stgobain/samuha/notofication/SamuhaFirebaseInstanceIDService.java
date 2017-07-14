package com.stgobain.samuha.notofication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.stgobain.samuha.network.NetworkService;
import com.stgobain.samuha.network.NetworkServiceResultReceiver;
import com.stgobain.samuha.utility.SharedPrefsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import static com.stgobain.samuha.utility.AppUtils.KEY_ERROR;
import static com.stgobain.samuha.utility.AppUtils.KEY_RECIVER;
import static com.stgobain.samuha.utility.AppUtils.KEY_REQUEST_ID;
import static com.stgobain.samuha.utility.AppUtils.KEY_RESULT;
import static com.stgobain.samuha.utility.AppUtils.SKEY_ID;
import static com.stgobain.samuha.utility.AppUtils.STATUS_ERROR;
import static com.stgobain.samuha.utility.AppUtils.STATUS_FINISHED;
import static com.stgobain.samuha.utility.AppUtils.STATUS_RUNNING;

/**
 * Created by vignesh on 13-07-2017.
 */


public class SamuhaFirebaseInstanceIDService extends FirebaseInstanceIdService implements NetworkServiceResultReceiver.Receiver{
    private static final String TAG = "SAMUHA_NOTIFY";
    private NetworkServiceResultReceiver mReceiver;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
     /*   Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);*/
    }

    private void sendRegistrationToServer(final String token) {
    //    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        String app_ver = null;
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        try {
            app_ver= getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // sending gcm token to server
        String userId = SharedPrefsUtils.getStringPreference(this, SKEY_ID);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id", userId);
            jsonObject.put("token_key", token);
            jsonObject.put("version", app_ver);
            jsonObject.put("os_type", "Android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
      //  requestWebservice(jsonObject.toString(), FCM_REGISTRATION_REQ_ID, FCM_REGISTRATION_URL);
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }
    private void requestWebservice(String request, int reqID, String url) {
        this.mReceiver = new NetworkServiceResultReceiver(new Handler());
        this.mReceiver.setReceiver(this);
        Intent intent = new Intent("android.intent.action.SYNC", null,this, NetworkService.class);
        intent.putExtra("url", url);
        intent.putExtra(KEY_RECIVER, this.mReceiver);
        intent.putExtra(KEY_REQUEST_ID, String.valueOf(reqID));
        intent.putExtra("request", request);
        startService(intent);
    }
    private void storeRegIdInPref(String token) {
      /*  SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();*/
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case STATUS_RUNNING:
                Log.d(TAG, "STATUS_RUNNING");
                break;
            case STATUS_FINISHED:
                Log.d(TAG, "FINISHED");
                String result = resultData.getString(KEY_RESULT);
                String status = "0";
                try {
                    status = new JSONObject(result).getString(KEY_ERROR);
                    Log.d(TAG, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status.equals("false")) {



                } else {

                    //     AppUtils.showAlertDialog(getActivity(), "Network Error!. Try Again!");
                }
                Log.d(TAG, "FINISHED status " + status + " ");
                break;
            case STATUS_ERROR:

                Log.d(TAG, "STATUS_ERROR");
                Log.d(TAG, "SERVICE RESPONSE ERROR " + resultData.getString("android.intent.extra.TEXT"));
                break;
        }
    }
}

