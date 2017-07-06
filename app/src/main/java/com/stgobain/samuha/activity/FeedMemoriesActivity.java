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
import com.stgobain.samuha.Model.MemeoriesData;
import com.stgobain.samuha.Model.Parser;
import com.stgobain.samuha.R;
import com.stgobain.samuha.adapter.MemoryFeedAdapter;
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

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import static com.stgobain.samuha.utility.AppUtils.FEED_MEMORIES_LIKE_URL;
import static com.stgobain.samuha.utility.AppUtils.FEED_MEMORIES_URL;
import static com.stgobain.samuha.utility.AppUtils.KEY_ERROR;
import static com.stgobain.samuha.utility.AppUtils.KEY_MESSAGE;
import static com.stgobain.samuha.utility.AppUtils.KEY_RECIVER;
import static com.stgobain.samuha.utility.AppUtils.KEY_REQUEST_ID;
import static com.stgobain.samuha.utility.AppUtils.KEY_RESULT;
import static com.stgobain.samuha.utility.AppUtils.SERVICE_REQUEST_FEED_MEMORIES;
import static com.stgobain.samuha.utility.AppUtils.SERVICE_REQUEST_MEMORY_FEED_LIKE;
import static com.stgobain.samuha.utility.AppUtils.SKEY_ID;
import static com.stgobain.samuha.utility.AppUtils.STATUS_ERROR;
import static com.stgobain.samuha.utility.AppUtils.STATUS_FINISHED;
import static com.stgobain.samuha.utility.AppUtils.STATUS_RUNNING;

/**
 * Created by vignesh on 26-06-2017.
 */

public class FeedMemoriesActivity extends AppCompatActivity implements NetworkServiceResultReceiver.Receiver, Paginate.Callbacks, MemoriesActionListener {
    private NetworkServiceResultReceiver mReceiver;
    private ProgressDialog progressDialog;
    ArrayList<MemeoriesData> memoryFeedArraylist = new ArrayList<>();
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
    private MemoryFeedAdapter memoryAdaptor;
    private boolean loading = false;
    private boolean isDataAvailable = true;
    boolean isSearchDataRequested = false;
    private int itemsAddPagination = 1;
    protected int itemsPerPage = 2;
    private LinearLayoutManager layoutManager;
    private int previousTotal = 0;
    static CircularProgressBar circularProgressBar;
    private boolean hasLoadedAll = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_memories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        String teamTittle = "Memories";
        getSupportActionBar().setTitle(teamTittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        memoryRecyclerView = (RecyclerView) findViewById(R.id.recylerviewMemories);
        layoutManager = new LinearLayoutManager(this);
        //  memoryRecyclerView.setLayoutManager(new LinearLayoutManager(FeedMemoriesActivity.this));
        memoryRecyclerView.setLayoutManager(layoutManager);
        memoryAdaptor = new MemoryFeedAdapter(FeedMemoriesActivity.this, this);
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
       /* if (this.progressDialog == null) {
            this.progressDialog = AppUtils.createProgressDialog(FeedMemoriesActivity.this);
            this.progressDialog.show();
        } else {
            this.progressDialog.show();
        }*/
        String userId = SharedPrefsUtils.getStringPreference(FeedMemoriesActivity.this, SKEY_ID);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", userId);
            jsonObject.put("page", itemsAddPagination);
            jsonObject.put("page_limit", itemsPerPage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestWebservice(jsonObject.toString(), SERVICE_REQUEST_FEED_MEMORIES, FEED_MEMORIES_URL);
    }

    private void setUpPagination() {
        paginate = Paginate.with(this.memoryRecyclerView, this).setLoadingTriggerThreshold(this.threshold).addLoadingListItem(this.addLoadingRow).setLoadingListItemCreator(this.customLoadingListItem ? new CustomLoadingListItemCreator() : null).setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
            @Override
            public int getSpanSize() {
                return GRID_SPAN;
            }
        }).build();
    }

    @Override
    public void likeClicked(String memoriesId) {
        postUserLike(memoriesId);
    }

    private void postUserLike(String memoriesId) {
        String userId = SharedPrefsUtils.getStringPreference(FeedMemoriesActivity.this, SKEY_ID);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", userId);
            jsonObject.put("memories_id", memoriesId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestWebservice(jsonObject.toString(), SERVICE_REQUEST_MEMORY_FEED_LIKE, FEED_MEMORIES_LIKE_URL);
    }

    @Override
    public void videoSelected(String videoUrl) {
        Intent intent = new Intent(FeedMemoriesActivity.this, VideoPrecviewActivity.class);
        startActivity(intent);

    }

    @Override
    public void imageClicked(String imgUrl) {
     /*   Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }*/
        Intent intent = new Intent(FeedMemoriesActivity.this, ImageViewActivity.class);
        intent.putExtra("IMGURL", imgUrl);
        startActivity(intent);
    }

    private class CustomLoadingListItemCreator implements LoadingListItemCreator {
        private CustomLoadingListItemCreator() {
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_loading_list_item, parent, false));
        }

        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((VH) holder).tvLoading.setText(String.format("Total items loaded: %d.\nLoading more...", new Object[]{Integer.valueOf(FeedMemoriesActivity.this.memoryAdaptor.getItemCount())}));
        }
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvLoading;

        public VH(View itemView) {
            super(itemView);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading_text);
           /* circularProgressBar = (CircularProgressBar)itemView.findViewById(R.id.recyleProgress);*/
        }
    }

    private void requestWebservice(String request, int reqID, String url) {
        this.mReceiver = new NetworkServiceResultReceiver(new Handler());
        this.mReceiver.setReceiver(this);
        Intent intent = new Intent("android.intent.action.SYNC", null, FeedMemoriesActivity.this, NetworkService.class);
        intent.putExtra("url", url);
        intent.putExtra(KEY_RECIVER, this.mReceiver);
        intent.putExtra(KEY_REQUEST_ID, String.valueOf(reqID));
        intent.putExtra("request", request);
        startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        JSONObject memoryFeedObj;
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
                int totalCount = 0;
                String status = "0";
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
                    case SERVICE_REQUEST_FEED_MEMORIES:
                        if (status.equals("false")) {
                            memoryFeedArraylist = Parser.getmemoryFeedArrayList(result);
                            memoryAdaptor.setMemoryFeeds(memoryFeedArraylist);
                            //  memoryFeedArraylist = loadmemoryFeedsFromAsset();
                            page++;
                            loading = false;
                            if (totalCount == 0) {
                                hasLoadedAll = true;
                                paginate.setHasMoreDataToLoad(false);
                            } else if (itemsAddPagination >= recordsCount) {
                                hasLoadedAll = true;
                                paginate.setHasMoreDataToLoad(false);
                            } else {
                                hasLoadedAll = false;
                            }
                        } else {

                            hasLoadedAll = true;
                            String message = "";
                            try {
                                message = new JSONObject(result).getString(KEY_MESSAGE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            loading = true;
                            addLoadingRow = false;
                            hasLoadedAll = true;
                            paginate.setHasMoreDataToLoad(false);
                            AppUtils.showAlertDialog(FeedMemoriesActivity.this, "No Data Found!");
                        }
                        break;
                    case SERVICE_REQUEST_MEMORY_FEED_LIKE:
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
                Log.d("LOGIN", "FINISHED status " + status + " ");
                break;
            case STATUS_ERROR:
                if (this.progressDialog != null) {
                    progressDialog.dismiss();
                }
                AppUtils.showAlertDialog(FeedMemoriesActivity.this, "Network Error!. Try Again!");
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

