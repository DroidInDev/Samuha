package com.stgobain.samuha.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.stgobain.samuha.CustomUserInterface.CustomEditTextView;
import com.stgobain.samuha.CustomUserInterface.CustomFontTextView;
import com.stgobain.samuha.R;
import com.stgobain.samuha.Utility.AppUtils;
import com.stgobain.samuha.Utility.SharedPrefsUtils;
import com.stgobain.samuha.network.NetworkService;
import com.stgobain.samuha.network.NetworkServiceResultReceiver;
import com.stgobain.samuha.network.NetworkServiceResultReceiver.Receiver;

import org.json.JSONException;
import org.json.JSONObject;

import static com.stgobain.samuha.Utility.AppUtils.CODE_LOGIN_URL;
import static com.stgobain.samuha.Utility.AppUtils.INTENET_ERROR;
import static com.stgobain.samuha.Utility.AppUtils.KEY_ERROR;
import static com.stgobain.samuha.Utility.AppUtils.KEY_IS_LOGED_IN;
import static com.stgobain.samuha.Utility.AppUtils.KEY_RECIVER;
import static com.stgobain.samuha.Utility.AppUtils.KEY_REQUEST_ID;
import static com.stgobain.samuha.Utility.AppUtils.KEY_RESULT;
import static com.stgobain.samuha.Utility.AppUtils.LOGIN_URL;
import static com.stgobain.samuha.Utility.AppUtils.SERVICE_REQUEST_LOGIN;
import static com.stgobain.samuha.Utility.AppUtils.SERVICE_REQUEST_LOGIN_CODE;
import static com.stgobain.samuha.Utility.AppUtils.SKEY_DEP_ID;
import static com.stgobain.samuha.Utility.AppUtils.SKEY_ID;
import static com.stgobain.samuha.Utility.AppUtils.SKEY_NAME;
import static com.stgobain.samuha.Utility.AppUtils.SKEY_TEAM_ID;
import static com.stgobain.samuha.Utility.AppUtils.SKEY_USER_TYPE;
import static com.stgobain.samuha.Utility.AppUtils.STATUS_ERROR;
import static com.stgobain.samuha.Utility.AppUtils.STATUS_FINISHED;
import static com.stgobain.samuha.Utility.AppUtils.STATUS_RUNNING;

/**
 * Created by vignesh on 22-06-2017.
 */

public class LoginActivity extends AppCompatActivity implements Receiver, View.OnClickListener {
    private NetworkServiceResultReceiver mReceiver;
    ProgressDialog progressDialog;
    CustomEditTextView mobileNoEditTextView;
    CustomEditTextView employeeIdEditTextView;
    CustomFontTextView txtCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mobileNoEditTextView = (CustomEditTextView) findViewById(R.id.etxtMobileNo);
        employeeIdEditTextView = (CustomEditTextView) findViewById(R.id.etxtEmployeeId);
        mobileNoEditTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mobileNoEditTextView.setInputType(InputType.TYPE_CLASS_PHONE);
                }
            }
        });
        employeeIdEditTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    employeeIdEditTextView.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
        txtCode =(CustomFontTextView)findViewById(R.id.codeLoginTxt);
        txtCode.setOnClickListener(this);
    }

    public void onLoginClicked(View view) {
        String mobileNo = mobileNoEditTextView.getText().toString().trim();
        String employeId = employeeIdEditTextView.getText().toString().trim();
        if (!TextUtils.isEmpty(mobileNo) && !TextUtils.isEmpty(employeId)) {
            if (isValidMobile(mobileNo)) {
                if (AppUtils.isNetworkAvailable(LoginActivity.this)) {
                    requestLogin();
                } else {
                    AppUtils.showAlertDialog(LoginActivity.this, INTENET_ERROR);
                }
            } else {
                AppUtils.showAlertDialog(LoginActivity.this, getString(R.string.eneter_valid_mobile_no));
            }
        } else if (TextUtils.isEmpty(mobileNo)) {
            String errorMsg = getString(R.string.eneter_valid_mobile_no);
            AppUtils.showAlertDialog(LoginActivity.this, errorMsg);
        } else {
            AppUtils.showAlertDialog(LoginActivity.this, "Employee Id cannot be empty");
        }

    }

    private boolean isValidMobile(String phone) {
        if (phone.length() <= 9) {
            return false;
        }
        return true;

    }
    public void onCodeLoginClicked() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(LoginActivity.this);
        View promptsView = li.inflate(R.layout.alert_dialog_wit_edttxt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                LoginActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final CustomEditTextView userInput = (CustomEditTextView) promptsView
                .findViewById(R.id.etxtCode);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text

                                String codeStr = userInput.getText().toString();
                              validateCode(codeStr);
                            }
                        });
               /* .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });*/

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    private void validateCode(String codeStr) {

        if (this.progressDialog == null) {
            this.progressDialog = AppUtils.createProgressDialog(LoginActivity.this);
            this.progressDialog.show();
        } else {
            this.progressDialog.show();
        }
        JSONObject loginJsonObject = new JSONObject();
        try {
            loginJsonObject.put("code", codeStr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestWebservice(loginJsonObject.toString(), SERVICE_REQUEST_LOGIN_CODE, CODE_LOGIN_URL);
    }

    private void requestLogin() {
        if (this.progressDialog == null) {
            this.progressDialog = AppUtils.createProgressDialog(LoginActivity.this);
            this.progressDialog.show();
        } else {
            this.progressDialog.show();
        }
        JSONObject loginJsonObject = new JSONObject();
        try {
            loginJsonObject.put("emp_id", "001");
            loginJsonObject.put("mobile", "9876543210");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestWebservice(loginJsonObject.toString(), SERVICE_REQUEST_LOGIN, LOGIN_URL);
    }

    private void requestWebservice(String request, int reqID, String url) {
        this.mReceiver = new NetworkServiceResultReceiver(new Handler());
        this.mReceiver.setReceiver(this);
        Intent intent = new Intent("android.intent.action.SYNC", null, LoginActivity.this, NetworkService.class);
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
                int requestId = Integer.valueOf(resultData.getString(KEY_REQUEST_ID));
                String resultString;
                JSONObject response;
                String id = AppUtils.INVALID_STRING_VALUE;
                String name = AppUtils.INVALID_STRING_VALUE;
                String teamId = AppUtils.INVALID_STRING_VALUE;
                String departmentId = AppUtils.INVALID_STRING_VALUE;
                String userType = AppUtils.INVALID_STRING_VALUE;
                switch (requestId) {
                    case SERVICE_REQUEST_LOGIN:

                        try {
                            status = new JSONObject(result).getString(KEY_ERROR);
                            resultString = new JSONObject(result).getString("response");
                            response = new JSONObject(resultString);
                            id = response.getString(SKEY_ID);
                            name = response.getString(SKEY_NAME);
                            teamId = response.getString(SKEY_TEAM_ID);
                            departmentId = response.getString(SKEY_DEP_ID);
                            userType = response.getString(SKEY_USER_TYPE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (status.equals("false")) {
                            SharedPrefsUtils.setBooleanPreference(LoginActivity.this, KEY_IS_LOGED_IN, true);
                            SharedPrefsUtils.setStringPreference(LoginActivity.this, SKEY_ID, id);
                            SharedPrefsUtils.setStringPreference(LoginActivity.this, SKEY_NAME, name);
                            SharedPrefsUtils.setStringPreference(LoginActivity.this, SKEY_TEAM_ID, teamId);
                            SharedPrefsUtils.setStringPreference(LoginActivity.this, SKEY_DEP_ID, departmentId);
                            SharedPrefsUtils.setStringPreference(LoginActivity.this, SKEY_USER_TYPE, userType);
                            Intent i = new Intent(LoginActivity.this, InviteFamilyActivity.class);
                            startActivity(i);
                            if (this.progressDialog == null) {
                                this.progressDialog.dismiss();
                            }
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        } else {
                          /*  if (this.progressDialog == null) {
                                this.progressDialog.dismiss();
                            }*/
                            SharedPrefsUtils.setBooleanPreference(LoginActivity.this, KEY_IS_LOGED_IN, false);
                            AppUtils.showAlertDialog(LoginActivity.this, "Login Failed. Try Again!");
                        }
                        Log.d("LOGIN", "FINISHED status " + status + " ");

                        break;
                    case SERVICE_REQUEST_LOGIN_CODE:
                        String resultval = resultData.getString(KEY_RESULT);
                        try {
                            status = new JSONObject(resultval).getString(KEY_ERROR);
                            resultString = new JSONObject(resultval).getString("response");
                            response = new JSONObject(resultString);
                            id = response.getString(SKEY_ID);
                            name = response.getString(SKEY_NAME);
                            teamId = response.getString(SKEY_TEAM_ID);
                            departmentId = response.getString(SKEY_DEP_ID);
                            userType = response.getString(SKEY_USER_TYPE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (status.equals("false")) {
                            SharedPrefsUtils.setBooleanPreference(LoginActivity.this, KEY_IS_LOGED_IN, true);
                            SharedPrefsUtils.setStringPreference(LoginActivity.this, SKEY_ID, id);
                            SharedPrefsUtils.setStringPreference(LoginActivity.this, SKEY_NAME, name);
                            SharedPrefsUtils.setStringPreference(LoginActivity.this, SKEY_TEAM_ID, teamId);
                            SharedPrefsUtils.setStringPreference(LoginActivity.this, SKEY_DEP_ID, departmentId);
                            SharedPrefsUtils.setStringPreference(LoginActivity.this, SKEY_USER_TYPE, userType);
                            Intent i = new Intent(LoginActivity.this, InviteFamilyActivity.class);
                            startActivity(i);
                            if (this.progressDialog != null) {
                                this.progressDialog.dismiss();
                            }
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        } else {
                            if (this.progressDialog != null) {
                                this.progressDialog.dismiss();
                            }
                            SharedPrefsUtils.setBooleanPreference(LoginActivity.this, KEY_IS_LOGED_IN, false);
                            AppUtils.showAlertDialog(LoginActivity.this, "Login Failed. Try Again!");
                        }
                        break;
                }
                break;
            case STATUS_ERROR:
                AppUtils.showAlertDialog(LoginActivity.this, "Login Failed. Try Again!");
                Log.d("LOGIN", "STATUS_ERROR");
                Log.d("LOGIN", "SERVICE RESPONSE ERROR " + resultData.getString("android.intent.extra.TEXT"));
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.codeLoginTxt){
            onCodeLoginClicked();
        }
    }
}
