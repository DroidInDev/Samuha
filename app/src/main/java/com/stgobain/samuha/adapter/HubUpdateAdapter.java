package com.stgobain.samuha.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.stgobain.samuha.CustomUserInterface.CustomFontTextView;
import com.stgobain.samuha.Model.HubUpdatesData;
import com.stgobain.samuha.R;

import java.util.ArrayList;

/**
 * Created by vignesh on 29-06-2017.
 */

public class HubUpdateAdapter extends RecyclerView.Adapter<HubUpdateAdapter.ViewHolderUpdate> {
    private LayoutInflater mInflater;
    private ArrayList<HubUpdatesData> hubContestList = new ArrayList<>();
    Context context;
    public HubUpdateAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ViewHolderUpdate onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_hub_updates, parent, false);
        ViewHolderUpdate viewHolder = new ViewHolderUpdate(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderUpdate holder, int position) {
        HubUpdatesData currentEvent = hubContestList.get(position);
        holder.txtDate.setText(currentEvent.getId());
        holder.txtEventLocationName.setText(currentEvent.getUpdates());
        holder.txtEventName.setText(currentEvent.getTitle());
    }

    @Override
    public int getItemCount() {
        return hubContestList.size();
    }



    public void setContests(ArrayList<HubUpdatesData> hubtList) {
        this.hubContestList = hubtList;
        notifyDataSetChanged();
    }

    public void setUpdates(ArrayList<HubUpdatesData> hubtList) {
        this.hubContestList = hubtList;
        notifyDataSetChanged();
    }

    public class ViewHolderUpdate extends RecyclerView.ViewHolder {
        ImageView profileImg;
        CustomFontTextView txtDate;
        CustomFontTextView txtEventName;
        CustomFontTextView txtEventLocationName;
        public ViewHolderUpdate(View itemView) {
            super(itemView);
            profileImg = (ImageView)itemView.findViewById(R.id.imgHubLocationIcon);
            txtDate = (CustomFontTextView)itemView.findViewById(R.id.txtHubDate);
            txtEventName =(CustomFontTextView)itemView.findViewById(R.id.txtEventHubName);
            txtEventLocationName =(CustomFontTextView)itemView.findViewById(R.id.txtHubLoc);
        }
    }
}
