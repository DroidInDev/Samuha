package com.stgobain.samuha.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.stgobain.samuha.CustomUserInterface.CustomFontTextView;
import com.stgobain.samuha.Model.Parser;
import com.stgobain.samuha.Model.SamuhaEvent;
import com.stgobain.samuha.R;
import com.stgobain.samuha.activity.TodayEventActivity;
import com.stgobain.samuha.adapter.EventAdapter;
import com.stgobain.samuha.network.NetworkService;
import com.stgobain.samuha.network.NetworkServiceResultReceiver;
import com.stgobain.samuha.paginate.Paginate;
import com.stgobain.samuha.utility.AppUtils;
import com.stgobain.samuha.utility.SharedPrefsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.stgobain.samuha.utility.AppUtils.EVENTS_URL;
import static com.stgobain.samuha.utility.AppUtils.GRAND_FINAL_EVENTS_URL;
import static com.stgobain.samuha.utility.AppUtils.KEY_ERROR;
import static com.stgobain.samuha.utility.AppUtils.KEY_RECIVER;
import static com.stgobain.samuha.utility.AppUtils.KEY_REQUEST_ID;
import static com.stgobain.samuha.utility.AppUtils.KEY_RESULT;
import static com.stgobain.samuha.utility.AppUtils.SERVICE_REQUEST_EVENTS;
import static com.stgobain.samuha.utility.AppUtils.SERVICE_REQUEST_GRAND_FINAL_EVENTS;
import static com.stgobain.samuha.utility.AppUtils.SKEY_ID;
import static com.stgobain.samuha.utility.AppUtils.STATUS_ERROR;
import static com.stgobain.samuha.utility.AppUtils.STATUS_FINISHED;
import static com.stgobain.samuha.utility.AppUtils.STATUS_RUNNING;

/**
 * Created by vignesh on 15-06-2017.
 */

public class EventFragmet extends Fragment implements NetworkServiceResultReceiver.Receiver, Paginate.Callbacks {
    private NetworkServiceResultReceiver mReceiver;
    private ProgressDialog progressDialog;
    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdaptor;
    ArrayList<SamuhaEvent> eventArraylist = new ArrayList<>();
    ArrayList<SamuhaEvent> grandEventArraylist = new ArrayList<>();
   // LinearLayout eventLayout;
    CustomFontTextView internalEvtTxt;
    CustomFontTextView comingSoonTxt;
    ImageView familyday1;
    //Pagination
    private int page = 0;
    private Paginate paginate;
    protected int threshold = 15;
    protected int totalPages = 100;
    private boolean addLoadingRow = true;
    protected boolean customLoadingListItem = false;
    private static final int GRID_SPAN = 3;
    private String isGrandFinalEventActive;
    private boolean isDataFetched = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        View layout = inflater.inflate(R.layout.frgment_event, container, false);
        eventRecyclerView = (RecyclerView) layout.findViewById(R.id.recylerviewEvent);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventAdaptor = new EventAdapter(getActivity());
        eventRecyclerView.setAdapter(eventAdaptor);
      //  eventLayout = (LinearLayout) layout.findViewById(R.id.layoutEventFragment);
        internalEvtTxt = (CustomFontTextView) layout.findViewById(R.id.txtInternalEvnt);
       // comingSoonTxt = (CustomFontTextView) layout.findViewById(R.id.txtInternalEvntCn);
        familyday1 = (ImageView)layout.findViewById(R.id.imgGrandArrow);
       // internalEvtTxt.setVisibility(View.INVISIBLE);
        //comingSoonTxt.setVisibility(View.INVISIBLE);
      //  openGrandEvents.setVisibility(View.INVISIBLE);
      /*  familyday1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TodayEventActivity.class);
                intent.putExtra("Tittle","Family Day 1");
                intent.putExtra("From","Event");
                intent.putExtra("Type","family_day_1");
           //     intent.putExtra("EventList",grandEventArraylist);
                getActivity().startActivity(intent);
            }
        });*/

        ImageView familyDay2 = (ImageView)layout.findViewById(R.id.imgGrandArrow1);
        /* familyDay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TodayEventActivity.class);
                intent.putExtra("Tittle","Family Day 2");
                intent.putExtra("Type","family_day_2");
                intent.putExtra("From","Event");
             //   intent.putExtra("EventList",grandEventArraylist);
                getContext().startActivity(intent);
            }
        });*/
        ImageView grandevent = (ImageView)layout.findViewById(R.id.imgGrandArrow2);
       grandevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TodayEventActivity.class);
                intent.putExtra("Tittle","Grand Finale");
                intent.putExtra("From","Event");
                intent.putExtra("Type","grand_final");
            //    intent.putExtra("EventList",grandEventArraylist);
                getActivity().startActivity(intent);
            }
        });

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void requestEvents() {
        String userId = SharedPrefsUtils.getStringPreference(getActivity(), SKEY_ID);
        JSONObject jsonObject = new JSONObject();
       /* try {
            jsonObject.put(SKEY_ID, userId);
            jsonObject.put(SKEY_EVENT_DATE, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        requestWebservice(jsonObject.toString(), SERVICE_REQUEST_EVENTS, EVENTS_URL);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // setUpPagination();
        if (isVisibleToUser) {
            if (getContext() != null) {
                if (this.progressDialog == null) {
                    this.progressDialog = AppUtils.createProgressDialog(getContext());
                    this.progressDialog.show();
                } else {
                    this.progressDialog.show();
                }
                requestEvents();
             //  requestEvents();
            }

        }
    }

    /*private void setUpPagination() {
        this.paginate = Paginate.with(this.eventRecyclerView, this).setLoadingTriggerThreshold(this.threshold).addLoadingListItem(this.addLoadingRow).setLoadingListItemCreator(this.customLoadingListItem ? new CustomLoadingListItemCreator() : null).setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup(){
            @Override
            public int getSpanSize() {
                return GRID_SPAN;
            }
        })
                .build();
    }
    private class CustomLoadingListItemCreator implements LoadingListItemCreator {
        private CustomLoadingListItemCreator() {
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_loading_list_item, parent, false));
        }

        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((VH) holder).tvLoading.setText(String.format("Total items loaded: %d.\nLoading more...", new Object[]{Integer.valueOf(EventFragmet.this.eventAdaptor.getItemCount())}));
        }
    }
    static class VH extends RecyclerView.ViewHolder {
        TextView tvLoading;

        public VH(View itemView) {
            super(itemView);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading_text);
        }
    }*/
    private void requestWebservice(String request, int reqID, String url) {
        this.mReceiver = new NetworkServiceResultReceiver(new Handler());
        this.mReceiver.setReceiver(this);
        Intent intent = new Intent("android.intent.action.SYNC", null, getContext(), NetworkService.class);
        intent.putExtra("url", url);
        intent.putExtra(KEY_RECIVER, this.mReceiver);
        intent.putExtra(KEY_REQUEST_ID, String.valueOf(reqID));
        intent.putExtra("request", request);
       getContext().startService(intent);
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
                int requestId = Integer.valueOf(resultData.getString(KEY_REQUEST_ID));
                String status = "0";
                try {
                    status = new JSONObject(result).getString(KEY_ERROR);
                    isGrandFinalEventActive = new JSONObject(result).getString("grand_final_status");
                    Log.d("EVENTS", isGrandFinalEventActive);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                switch (requestId) {
                    case SERVICE_REQUEST_EVENTS:
                        if (status.equals("false")) {
                            if (this.progressDialog != null) {
                                this.progressDialog.dismiss();
                                //  eventArraylist = loadEventsFromAsset();
                                eventArraylist = Parser.getEventArrayList(result);
                                eventAdaptor.setEvents(eventArraylist);
                                internalEvtTxt.setVisibility(View.VISIBLE);
                              //  comingSoonTxt.setVisibility(View.VISIBLE);

                             /*   if (isGrandFinalEventActive.equals("true")) {
                                  //  openGrandEvents.setVisibility(View.VISIBLE);
                                 //   comingSoonTxt.setVisibility(View.INVISIBLE);
                                    requestGrandFinalEvents();
                                }*/
                            }
                        } else {
                            if (this.progressDialog != null) {
                                progressDialog.dismiss();
                            }
                       //     AppUtils.showAlertDialog(getActivity(), "Internet Error!. Try Again!");
                        }
                        break;
                    case SERVICE_REQUEST_GRAND_FINAL_EVENTS:
                        if (status.equals("false")) {
                            if (this.progressDialog != null) {
                                this.progressDialog.dismiss();
                                //  eventArraylist = loadEventsFromAsset();
                              //  openGrandEvents.setVisibility(View.VISIBLE);
                                grandEventArraylist =Parser.getEventArrayList(result);
                            }
                        } else {
                            if (this.progressDialog != null) {
                                progressDialog.dismiss();
                            }
                          //  AppUtils.showAlertDialog(getActivity(), "Internet Error!. Try Again!");
                        }
                        break;
                }
                break;
            case STATUS_ERROR:
                if (this.progressDialog != null) {
                    progressDialog.dismiss();
                }
             //   AppUtils.showAlertDialog(getActivity(), "Network Error!. Try Again!");
                Log.d("LOGIN", "STATUS_ERROR");
                Log.d("LOGIN", "SERVICE RESPONSE ERROR " + resultData.getString("android.intent.extra.TEXT"));
                break;
        }
    }

    private void requestGrandFinalEvents() {
        String userId = SharedPrefsUtils.getStringPreference(getActivity(), SKEY_ID);
        JSONObject jsonObject = new JSONObject();
       /* try {
            jsonObject.put(SKEY_ID, userId);
            jsonObject.put(SKEY_EVENT_DATE, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        requestWebservice(jsonObject.toString(), SERVICE_REQUEST_GRAND_FINAL_EVENTS, GRAND_FINAL_EVENTS_URL);
    }

   /* public ArrayList<SamuhaEvent> loadEventsFromAsset() {
        ArrayList<SamuhaEvent> samuhaEvents = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("event_list.json");
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
            JSONArray m_jArry = obj.getJSONArray("events");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                SamuhaEvent event = new SamuhaEvent();
                event.setId(jo_inside.getString("id"));
                event.setEventDate(jo_inside.getString("event_date"));
                event.setEventName(jo_inside.getString("event_name"));
                event.setEventLocation(jo_inside.getString("event_location"));
                //Add your values in your `ArrayList` as below:
                samuhaEvents.add(event);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return samuhaEvents;
    }*/

    @Override
    public void onLoadMore() {

    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return false;
    }
}
