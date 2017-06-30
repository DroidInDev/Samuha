package com.stgobain.samuha.utility;

import android.app.Application;
import android.content.Context;

/**
 * Created by vignesh on 27-06-2017.
 */

public class samuhaApplication extends Application {
    public static Context appContext;
    public static byte[] imageByteArray = null;
    private static samuhaApplication mAppInstance = null;
    public static String strActiveUserRoomID = null;
    public String TAG = "samuhaApplication";

    public static samuhaApplication getInstance() {
        return mAppInstance;
    }

    public static samuhaApplication get() {
        return get(appContext);
    }
    public static Context getAppContext() {
        return mAppInstance.getApplicationContext();
    }
    public static samuhaApplication get(Context context) {
        return (samuhaApplication) context.getApplicationContext();
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