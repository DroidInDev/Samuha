package com.stgobain.samuha.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.stgobain.samuha.R;

/**
 * Created by vignesh on 22-06-2017.
 */

public class AppUtils {

    //TODO:: change for production
    private  static String base_url="live";
    //Key Response
    public static final String SKEY_ID = "id";
    public static final String SKEY_NAME ="name";
    public static final String SKEY_TEAM_ID = "team_id";
    public static final String SKEY_DEP_ID = "department_id";
    public static final String SKEY_USER_TYPE ="user_type";
    public static final String SKEY_USER_ID ="user_id";
    public static final String SKEY_EVENT_DATE ="event_date";
    public static final String SKEY_LOCATION ="location";
    public static final String SKEY_IMAGE_URL ="location";
    public static final String SKEY_DESCRIPTION ="short_description";

    //constants
    public static final String INVALID_STRING_VALUE ="" ;
    public static final String NETWORK_ERROR ="Network Error!. Try Again!" ;
    public static final int STATUS_ERROR = 2;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_RUNNING = 0;
    public static final String KEY_RECIVER = "receiver";
    public static final String KEY_REQUEST_ID = "requestId";
    public static final String KEY_RESULT = "result";
    public static final String KEY_REQUEST_TYPE = "requestType";
    public static final String KEY_ERROR ="error";
    public static final String KEY_MESSAGE ="message";
    public static final String KEY_RESPONSE ="response";
    //App Messages
    public static final String INTENET_ERROR ="Kindly check your Intenet connection and Try Again";
    //Prefernce keys
    public static final String KEY_IS_LOGED_IN ="is_logged_in";
    //Request code
    public static final int SERVICE_REQUEST_LOGIN  =1001;
    public static final int SERVICE_REQUEST_INVITE_FAMILY  =1002;
    public static final int SERVICE_REQUEST_EVENTS  =1003;
    public static final int  SERVICE_REQUEST_TEAMS  =1004;
    public static final int  SERVICE_REQUEST_TEAMS_SCORE  =1005;
    public static final int  SERVICE_REQUEST_TODAY_EVENT  =1006;
    public static final int  SERVICE_REQUEST_ANNOUNCEMENT  =1007;
    public static final int  SERVICE_REQUEST_POST_MEMORIES  =1008;
    public static final int  SERVICE_REQUEST_POST_SAB  =10014;
    public static final int  SERVICE_REQUEST_GET_CONTEXT_TO_UPLOAD  =1009;
    public static final int  SERVICE_REQUEST_GET_EVENTS_TO_UPLOAD  =1010;
    public static final int  SERVICE_REQUEST_FEED_MEMORIES  =1011;
    public static final int  SERVICE_REQUEST_FEED_SAB  =1012;
    public static final int  SERVICE_REQUEST_MEMORY_FEED_LIKE  =1013;
    public static final int  SERVICE_REQUEST_SAB_FEED_LIKE  =1017;
    public static final int  SERVICE_REQUEST_LOGIN_CODE  =1015;
    public static final int  SERVICE_REQUEST_HUB_CONTESTS  =1016;
    public static final int  SERVICE_REQUEST_HUB_UPDATES =1018;
    public static final int  SERVICE_REQUEST_GRAND_FINAL_EVENTS =1019;
    //Urls
    public static final String LOGIN_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/login";
    public static final String INVITE_FAMILY_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/invite";
    public static final String EVENTS_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/getevents";
    public static final String EVENTS_URL_FAMILY= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/getevents";
    public static final String TEAMS_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/getteams";
    public static final String ANNOUNCEMENT_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/getannonucement";
    public static final String POST_MEMORIES= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/postmemories";
    public static final String GET_CONTEXT_TO_UPLOAD_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/getcontests";
    public static final String GET_EVENTS_TO_UPLOAD_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/getevents";
    public static final String FEED_MEMORIES_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/listmemories";
    public static final String FEED_MEMORIES_LIKE_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/votepostmemories";
    public static final String SAB_FEED_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/listsab";
    public static final String POST_SAB= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/postsab";
    public static final String FEED_SAB_LIKE_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/votepostsab";
    public static final String CODE_LOGIN_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/guestlogin";
    public static final String HUB_CONTESTS_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/getcontests";
    public static final String HUB_UPDATESS_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/getupdates";
    public static final String GRAND_FINAL_EVENTS_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/getgrandfinalevents";

    public static final String GET_EVENTS_AND_CONTEXTS_URL= "http://www.thanjavurkingslionsclub.com/"+base_url+"/v1/getdropdowneventcontest";


    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showAlertDialog(Context context, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity

                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.progressdialog);
        return dialog;
    }
}
