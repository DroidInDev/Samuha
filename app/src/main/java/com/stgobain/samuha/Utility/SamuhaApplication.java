package com.stgobain.samuha.Utility;

import android.app.Application;
import android.content.Context;

/**
 * Created by vignesh on 27-06-2017.
 */

public class SamuhaApplication extends Application {
    public static Context appContext;
    public static byte[] imageByteArray = null;
    private static SamuhaApplication mAppInstance = null;
    public static String strActiveUserRoomID = null;
    public String TAG = "SamuhaApplication";

    public static SamuhaApplication getInstance() {
        return mAppInstance;
    }

    public static SamuhaApplication get() {
        return get(appContext);
    }
    public static Context getAppContext() {
        return mAppInstance.getApplicationContext();
    }
    public static SamuhaApplication get(Context context) {
        return (SamuhaApplication) context.getApplicationContext();
    }

    public static byte[] getImageByteArray() {
        return imageByteArray;
    }

    public static void setImageByteArray(byte[] imageByteArray) {
        imageByteArray = imageByteArray;
    }



    public void onCreate() {
        super.onCreate();
        mAppInstance = this;
        appContext = getApplicationContext();
    }

}