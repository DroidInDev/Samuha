package com.stgobain.samuha.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.stgobain.samuha.CustomUserInterface.CustomFontTextView;
import com.stgobain.samuha.Model.Team;
import com.stgobain.samuha.R;
import com.stgobain.samuha.utility.SharedPrefsUtils;
import com.stgobain.samuha.network.NetworkServiceResultReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.stgobain.samuha.utility.AppUtils.SKEY_EVENT_DATE;
import static com.stgobain.samuha.utility.AppUtils.SKEY_ID;

/**
 * Created by vignesh on 15-06-2017.
 */
//implements NetworkServiceResultReceiver.Receiver
public class TeamDescriptionActivity extends AppCompatActivity  {
    private NetworkServiceResultReceiver mReceiver;
    private ProgressDialog progressDialog;
    ListView departmentListView;
    // CustomFontTextView teamNameTxt;
    CustomFontTextView captainNameTxt;
    CustomFontTextView viceCaptainNameTxt;
    CustomFontTextView scoreTxt;
    ArrayAdapter<String> listAdapter;
    ImageView logoImg;
    Bitmap bitmap;
    int position = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_describtion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        String teamTittle = "";
        Team team = null;
        if (extras != null) {
            teamTittle = extras.getString("Tittle");
            position = extras.getInt("Position");
            team = (Team) extras.getParcelable("TeamObject");
            //  Toast.makeText(TeamDescriptionActivity.this,teamTittle,Toast.LENGTH_SHORT).show();
        }
        getSupportActionBar().setTitle(teamTittle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        logoImg = (ImageView) findViewById(R.id.imgTeamLogo);
        switch (position) {
            case 0:
                bitmap = BitmapFactory.decodeResource(TeamDescriptionActivity.this.getResources(), R.drawable.tech_now_flag);
                logoImg.setImageBitmap(bitmap);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(TeamDescriptionActivity.this.getResources(), R.drawable.digi_now_flag);
                logoImg.setImageBitmap(bitmap);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(TeamDescriptionActivity.this.getResources(), R.drawable.cyper_now_flag);
                logoImg.setImageBitmap(bitmap);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(TeamDescriptionActivity.this.getResources(), R.drawable.beta_now_flag);
                logoImg.setImageBitmap(bitmap);
                break;
            default:
                break;
        }
        //teamNameTxt = (CustomFontTextView)findViewById(R.id.txtTeamNameInput);
        captainNameTxt = (CustomFontTextView) findViewById(R.id.txtTeamCaptainNameInput);
        viceCaptainNameTxt = (CustomFontTextView) findViewById(R.id.txtViceCaptainNameInput);
        scoreTxt = (CustomFontTextView) findViewById(R.id.txtScoreNameInput);
        departmentListView = (ListView) findViewById(R.id.listDepartment);
        displayTeam(team);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (this.progressDialog == null) {
            this.progressDialog = AppUtils.createProgressDialog(TeamDescriptionActivity.this);
            this.progressDialog.show();
        } else {
            this.progressDialog.show();
        }*/
     //  requestTeam();
    }

    private void requestTeam() {
        String userId = SharedPrefsUtils.getStringPreference(TeamDescriptionActivity.this, SKEY_ID);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SKEY_ID, userId);
            jsonObject.put(SKEY_EVENT_DATE, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //requestWebservice(jsonObject.toString(), SERVICE_REQUEST_TEAMS, TEAMS_URL);
    }

    private void displayTeam(Team choosenTeam) {
     //   Team choosenTeam = loadTeamFromAsset(position);
        captainNameTxt.setText(choosenTeam.getCaptainName());
        // teamNameTxt.setText(choosenTeam.getTeamName());
        viceCaptainNameTxt.setText(choosenTeam.getViceCaptainName());
        scoreTxt.setText(choosenTeam.getScore());
        if(choosenTeam.getDepartment().size()!=0) {
            String[] departmentArray = new String[choosenTeam.getDepartment().size()];
            choosenTeam.getDepartment().toArray(departmentArray);
            listAdapter = new ArrayAdapter<String>(this, R.layout.item_department_row, departmentArray);
            departmentListView.setAdapter(listAdapter);
        }
    }


   /* private void requestWebservice(String request, int reqID, String url) {
        this.mReceiver = new NetworkServiceResultReceiver(new Handler());
        this.mReceiver.setReceiver(this);
        Intent intent = new Intent("android.intent.action.SYNC", null, TeamDescriptionActivity.this, NetworkService.class);
        intent.putExtra("url", url);
        intent.putExtra(KEY_RECIVER, this.mReceiver);
        intent.putExtra(KEY_REQUEST_ID, String.valueOf(reqID));
        intent.putExtra("request", request);
       startService(intent);
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        JSONArray response = null;
        JSONObject eventObj;
        switch (resultCode) {
            case STATUS_RUNNING:
                Log.d("LOGIN", "STATUS_RUNNING");
                break;
            case STATUS_FINISHED:
                Log.d("LOGIN", "FINISHED");
                String result = resultData.getString(KEY_RESULT);
                String status = "0";
                try {
                    status = new JSONObject(result).getString(KEY_ERROR);
                    Log.d("TEAMS",result);
                   } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status.equals("false")) {
                    Team t = Parser.getTeam(result,position);
                    if(t!=null)
                    displayTeam(t);
                    if (this.progressDialog != null) {
                        this.progressDialog.dismiss();
                    }
                } else {

                    AppUtils.showAlertDialog(TeamDescriptionActivity.this, "Network Error!. Try Again!");
                }
                Log.d("LOGIN", "FINISHED status " + status + " ");
                break;
            case STATUS_ERROR:
                if (this.progressDialog != null) {
                    progressDialog.dismiss();
                }
                AppUtils.showAlertDialog(TeamDescriptionActivity.this, "Network Error!. Try Again!");
                Log.d("LOGIN", "STATUS_ERROR");
                Log.d("LOGIN", "SERVICE RESPONSE ERROR " + resultData.getString("android.intent.extra.TEXT"));
                break;
        }
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public Team loadTeamFromAsset(int position) {
        Team team = new Team();
        String json = null;
        try {
            InputStream is = getAssets().open("tech_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("team");

            for (int i = 0; i < m_jArry.length(); i++) {
                if (i == position) {
                    JSONObject jo_inside = m_jArry.getJSONObject(i);
                    team.setTeamName(jo_inside.getString("team_name"));
                    team.setCaptainName(jo_inside.getString("captain_name"));
                    team.setViceCaptainName(jo_inside.getString("vice_captain_name"));
                    JSONArray departmentArray = jo_inside.getJSONArray("department");
                    ArrayList<String> deparmentArrayList = new ArrayList<>();
                    for (int j = 0; j < departmentArray.length(); j++) {
                        deparmentArrayList.add(departmentArray.getString(j));
                    }
                    team.setDepartment(deparmentArrayList);
                    //Add your values in your `ArrayList` as below:
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return team;
    }
}

