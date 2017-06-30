package com.stgobain.samuha.network;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.stgobain.samuha.R;
import com.stgobain.samuha.utility.AppUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.stgobain.samuha.utility.AppUtils.KEY_RECIVER;
import static com.stgobain.samuha.utility.AppUtils.KEY_REQUEST_ID;
import static com.stgobain.samuha.utility.AppUtils.KEY_RESULT;
import static com.stgobain.samuha.utility.AppUtils.STATUS_ERROR;
import static com.stgobain.samuha.utility.AppUtils.STATUS_FINISHED;
import static com.stgobain.samuha.utility.AppUtils.STATUS_RUNNING;

/**
 * Created by vignesh on 22-06-2017.
 */
public class NetworkService extends IntentService {

    private static final String TAG = "NetworkService";

    public NetworkService() {
        super(NetworkService.class.getName());
    }

    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(TAG, "Service Started!");
            ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra(KEY_RECIVER);
            String url = intent.getStringExtra("url");
            String request = intent.getStringExtra("request");
            String requestID = intent.getStringExtra(KEY_REQUEST_ID);
            Bundle bundle = new Bundle();
            if (!TextUtils.isEmpty(url)) {
                receiver.send(STATUS_RUNNING, Bundle.EMPTY);
                try {
                    if (AppUtils.isNetworkAvailable(getApplicationContext())) {
                        String result = callingOKhttp3Request(url, request);
                        if (result != null) {
                            bundle.putString(KEY_RESULT, result);
                            bundle.putString(KEY_REQUEST_ID, requestID);
                            receiver.send(STATUS_FINISHED, bundle);
                        }
                    } else {
                        bundle.putString("android.intent.extra.TEXT", getResources().getString(R.string.alert_no_internet).toString());
                        receiver.send(STATUS_ERROR, bundle);
                    }
                } catch (Exception e) {
                    bundle.putString("android.intent.extra.TEXT", e.toString());
                    receiver.send(STATUS_ERROR, bundle);
                }
            }
        }
        Log.d(TAG, "Service Stopping!");
        stopSelf();
    }

    private String callingOKhttp3Request(String url, String json) throws IOException {
        Builder builder = new Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS);
        return builder.build().newCall(new Request.Builder().url(url).post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)).build()).execute().body().string();
    }
}