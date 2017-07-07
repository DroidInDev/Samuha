package com.stgobain.samuha.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stgobain.samuha.AppCallbacks.MemoriesActionListener;
import com.stgobain.samuha.CustomUserInterface.CustomFontTextView;
import com.stgobain.samuha.Model.Parser;
import com.stgobain.samuha.Model.SabData;
import com.stgobain.samuha.R;
import com.stgobain.samuha.adapter.SabFeedAdapter;
import com.stgobain.samuha.network.NetworkService;
import com.stgobain.samuha.network.NetworkServiceResultReceiver;
import com.stgobain.samuha.paginate.LoadingListItemCreator;
import com.stgobain.samuha.paginate.LoadingListItemSpanLookup;
import com.stgobain.samuha.paginate.Paginate;
import com.stgobain.samuha.utility.AppUtils;
import com.stgobain.samuha.utility.SharedPrefsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.stgobain.samuha.utility.AppUtils.FEED_SAB_LIKE_URL;
import static com.stgobain.samuha.utility.AppUtils.KEY_ERROR;
import static com.stgobain.samuha.utility.AppUtils.KEY_RECIVER;
import static com.stgobain.samuha.utility.AppUtils.KEY_REQUEST_ID;
import static com.stgobain.samuha.utility.AppUtils.KEY_RESULT;
import static com.stgobain.samuha.utility.AppUtils.SAB_FEED_URL;
import static com.stgobain.samuha.utility.AppUtils.SERVICE_REQUEST_FEED_SAB;
import static com.stgobain.samuha.utility.AppUtils.SERVICE_REQUEST_SAB_FEED_LIKE;
import static com.stgobain.samuha.utility.AppUtils.SKEY_ID;
import static com.stgobain.samuha.utility.AppUtils.STATUS_ERROR;
import static com.stgobain.samuha.utility.AppUtils.STATUS_FINISHED;
import static com.stgobain.samuha.utility.AppUtils.STATUS_RUNNING;

/**
 * Created by vignesh on 28-06-2017.
 */

public class SabGalleryActivity extends AppCompatActivity implements NetworkServiceResultReceiver.Receiver, Paginate.Callbacks, MemoriesActionListener {
    private NetworkServiceResultReceiver mReceiver;
    private ProgressDialog progressDialog;
    ArrayList<SabData> sabFeedArraylist = new ArrayList<>();
    CustomFontTextView internalEvtTxt;
    CustomFontTextView comingSoonTxt;
    //Pagination
    private int page = 0;
    private Paginate paginate;
    protected int threshold = 5;
    protected int totalPages = 100;
    private boolean addLoadingRow = true;
    protected boolean customLoadingListItem = false;
    private static final int GRID_SPAN = 3;
    private String isGrandFinalmemoryFeedActive;
    private RecyclerView memoryRecyclerView;
    private SabFeedAdapter memoryAdaptor;
    private boolean loading = false;
    private boolean isDataAvailable = true;
    boolean isSearchDataRequested = false;
    private int itemsAddPagination = 1;
    protected int itemsPerPage = 30;
    private LinearLayoutManager layoutManager;
    private int previousTotal = 0;
    private boolean hasLoadedAll = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_memories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        String teamTittle = "Gallery";
        getSupportActionBar().setTitle(teamTittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        memoryRecyclerView = (RecyclerView) findViewById(R.id.recylerviewMemories);
        layoutManager = new LinearLayoutManager(this);
        //  memoryRecyclerView.setLayoutManager(new LinearLayoutManager(SabGalleryActivity.this));
        memoryRecyclerView.setLayoutManager(layoutManager);
        memoryAdaptor = new SabFeedAdapter(SabGalleryActivity.this, this);
        memoryRecyclerView.setAdapter(memoryAdaptor);
        memoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = memoryRecyclerView.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemIndex = layoutManager.findFirstVisibleItemPosition();

                //synchronizew loading state when item count changes
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = true;
                        previousTotal = totalItemCount;
                    }
                }
                if (dy < 0) {
                    loading = true;
                    isDataAvailable = false;
                    // Recycle view scrolling up...

                } else if (dy > 0) {
                    // Recycle view scrolling down...
                }
                if (!loading)
                    if ((totalItemCount - visibleItemCount) <= firstVisibleItemIndex) {
                        // Loading NOT in progress and end of list has been reached
                        // also triggered if not enough items to fill the screen
                        // if you start loading
                        loading = false;
                        isDataAvailable = true;
                    } else if (firstVisibleItemIndex == 0) {
                        // top of list reached
                        // if you start loading
                        loading = true;
                    }

            }
        });
        setUpPagination();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void requestmemoryFeeds(int itemsAddPagination) {

        String userId = SharedPrefsUtils.getStringPreference(SabGalleryActivity.this, SKEY_ID);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", userId);
            jsonObject.put("page", itemsAddPagination);
            jsonObject.put("page_limit", itemsPerPage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestWebservice(jsonObject.toString(), SERVICE_REQUEST_FEED_SAB, SAB_FEED_URL);
    }

    private void setUpPagination() {
        paginate = Paginate.with(this.memoryRecyclerView, this).setLoadingTriggerThreshold(this.threshold).addLoadingListItem(this.addLoadingRow).setLoadingListItemCreator(this.customLoadingListItem ? new CustomLoadingListItemCreator() : null).setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
            @Override
            public int getSpanSize() {
                return GRID_SPAN;
            }
        }).build();
    }


    private class CustomLoadingListItemCreator implements LoadingListItemCreator {
        private CustomLoadingListItemCreator() {
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_loading_list_item, parent, false));
        }

        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((VH) holder).tvLoading.setText(String.format("Total items loaded: %d.\nLoading more...", new Object[]{Integer.valueOf(SabGalleryActivity.this.memoryAdaptor.getItemCount())}));
        }
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvLoading;

        public VH(View itemView) {
            super(itemView);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading_text);
        }
    }

    @Override
    public void likeClicked(String memoriesId) {
        postUserLike(memoriesId);
    }

    private void postUserLike(String memoriesId) {
        String userId = SharedPrefsUtils.getStringPreference(SabGalleryActivity.this, SKEY_ID);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", userId);
            jsonObject.put("sab_id", memoriesId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestWebservice(jsonObject.toString(), SERVICE_REQUEST_SAB_FEED_LIKE, FEED_SAB_LIKE_URL);
    }

    @Override
    public void videoSelected(String videoUrl) {
        Intent intent = new Intent(SabGalleryActivity.this, VideoPrecviewActivity.class);
        intent.putExtra("videoUrl",videoUrl);
        startActivity(intent);

    }

    @Override
    public void imageClicked(String imgUrl) {
        Intent intent = new Intent(SabGalleryActivity.this, ImageViewActivity.class);
        intent.putExtra("IMGURL", imgUrl);
        startActivity(intent);
    }

    private void requestWebservice(String request, int reqID, String url) {
        this.mReceiver = new NetworkServiceResultReceiver(new Handler());
        this.mReceiver.setReceiver(this);
        Intent intent = new Intent("android.intent.action.SYNC", null, SabGalleryActivity.this, NetworkService.class);
        intent.putExtra("url", url);
        intent.putExtra(KEY_RECIVER, this.mReceiver);
        intent.putExtra(KEY_REQUEST_ID, String.valueOf(reqID));
        intent.putExtra("request", request);
        SabGalleryActivity.this.startService(intent);
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
                int requestId = Integer.valueOf(resultData.getString(KEY_REQUEST_ID));
                String resultString;
                JSONObject response;
                String totalRecords;
                int recordsCount = 0;
                String status = "0";
                int totalCount = 0;
                try {
                    status = new JSONObject(result).getString(KEY_ERROR);
                    resultString = new JSONObject(result).getString("response");
                    response = new JSONObject(resultString);
                    totalRecords = response.getString("total_records");
                    totalCount = Integer.valueOf(totalRecords);
                    recordsCount = Integer.valueOf(totalRecords) - 5;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                switch (requestId) {
                    case SERVICE_REQUEST_FEED_SAB:

                        if (status.equals("false")) {
                            //  memoryFeedArraylist = loadmemoryFeedsFromAsset();
                            sabFeedArraylist = Parser.getsabFeedArraylist(result);
                            memoryAdaptor.setSabFeeds(sabFeedArraylist);
                            page++;
                            loading = false;
                            if (totalCount == 0||itemsAddPagination>=totalCount) {
                                hasLoadedAll = true;
                                paginate.setHasMoreDataToLoad(false);
                            } else {
                                hasLoadedAll = false;
                            }
                        } else {

                            hasLoadedAll = true;
                            paginate.setHasMoreDataToLoad(false);
                            loading = true;
                            AppUtils.showAlertDialog(SabGalleryActivity.this, "No Data Found!");
                        }
                        break;
                    case SERVICE_REQUEST_SAB_FEED_LIKE:
                        if (status.equals("false")) {
                            if (this.progressDialog != null) {
                                this.progressDialog.dismiss();
                                //  memoryFeedArraylist = loadmemoryFeedsFromAsset();

                            }
                        } else {
                            if (this.progressDialog != null) {
                                this.progressDialog.dismiss();
                                //  memoryFeedArraylist = loadmemoryFeedsFromAsset();

                            }

                            // AppUtils.showAlertDialog(FeedMemoriesActivity.this, "Network Error!. Try Again!");
                        }
                        break;
                }
                break;
            case STATUS_ERROR:
                if (this.progressDialog != null) {
                    progressDialog.dismiss();
                }
                try {
                    AppUtils.showAlertDialog(SabGalleryActivity.this, "Network Error!. Try Again!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("LOGIN", "STATUS_ERROR");
                Log.d("LOGIN", "SERVICE RESPONSE ERROR " + resultData.getString("android.intent.extra.TEXT"));
                break;
        }
    }

    @Override
    public void onLoadMore() {
        if (this.isDataAvailable) {
            this.loading = true;
            requestmemoryFeeds(itemsAddPagination);
            this.itemsAddPagination += itemsPerPage;
        }

    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return hasLoadedAll;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

