package com.stgobain.samuha.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.stgobain.samuha.CustomUserInterface.CustomFontTextView;
import com.stgobain.samuha.R;
import com.stgobain.samuha.utility.SharedPrefsUtils;

import static com.stgobain.samuha.utility.AppUtils.KEY_IS_LOGED_IN;

public class SamuhaSplashActivity extends AppCompatActivity {
    MediaPlayer music;
    private static final int SPLASH_TIME_OUT = 1000;
    CustomFontTextView versionNameTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samuha_splash);
        versionNameTxt = (CustomFontTextView)findViewById(R.id.txtVersionName);
        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                   // music = MediaPlayer.create(SamuhaSplashActivity.this, R.raw.freebird_solo);
                    //  music.start();
                    try
                    {
                        String app_ver = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                        versionNameTxt.setText(app_ver);
                    }
                    catch (PackageManager.NameNotFoundException e)
                    {
                        Log.e("Samuha", e.getMessage());
                    }
                    sleep(500);

                } catch (InterruptedException e) {

                } finally {
                    boolean isLoginDone = SharedPrefsUtils.getBooleanPreference(SamuhaSplashActivity.this, KEY_IS_LOGED_IN, false);
                    //  Toast.makeText(SamuhaSplashActivity.this,"LOGIN "+ isLoginDone,Toast.LENGTH_SHORT).show();
                    Log.d("LOGIN", "LOGIN " + isLoginDone);
                    if (isLoginDone) {
                        Intent i = new Intent(SamuhaSplashActivity.this, BaseActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } else {
                        Intent i = new Intent(SamuhaSplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }

                    // close this activity
                    finish();
                }
            }
        };

        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (music != null)
            music.release();
    }
}
