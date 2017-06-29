package com.stgobain.samuha.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.stgobain.samuha.CustomUserInterface.CustomFontTextView;
import com.stgobain.samuha.Model.HubContestsData;
import com.stgobain.samuha.R;

import java.util.ArrayList;

/**
 * Created by vignesh on 28-06-2017.
 */

public class HubContestAdapter extends RecyclerView.Adapter<HubContestAdapter.ViewHolderContest> {
    private LayoutInflater mInflater;
    private ArrayList<HubContestsData> hubContestList = new ArrayList<>();
    Context context;
    public HubContestAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ViewHolderContest onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_hub_contests_row, parent, false);
        ViewHolderContest viewHolder = new ViewHolderContest(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderContest holder, int position) {
        HubContestsData currentEvent = hubContestList.get(position);
        holder.txtContextName.setText(currentEvent.getName());
        holder.txtDate.setText(currentEvent.getDateString());
        holder.txtEventLocationName.setText(currentEvent.getContestLocation());
        holder.txtEventName.setText(currentEvent.getContestType());
    }

    @Override
    public int getItemCount() {
        return hubContestList.size();
    }

    public void setScore(ArrayList<HubContestsData> scoreArraylist) {
        hubContestList = scoreArraylist;
        notifyDataSetChanged();
    }

    public void setContests(ArrayList<HubContestsData> hubtList) {
        this.hubContestList = hubtList;
        notifyDataSetChanged();
    }

    public class ViewHolderContest extends RecyclerView.ViewHolder {
        ImageView profileImg;
        CustomFontTextView txtContextName;
        CustomFontTextView txtDate;
        CustomFontTextView txtEventName;
        CustomFontTextView txtEventLocationName;
        ImageView locationImageView;
        public ViewHolderContest(View itemView) {
            super(itemView);
            profileImg = (ImageView)itemView.findViewById(R.id.imgScoreAttr);
            txtContextName = (CustomFontTextView)itemView.findViewById(R.id.txtContestName);
            txtDate = (CustomFontTextView)itemView.findViewById(R.id.txtDate);
            txtEventName =(CustomFontTextView)itemView.findViewById(R.id.txtEventName);
            txtEventLocationName =(CustomFontTextView)itemView.findViewById(R.id.txtEventLocationName);
            locationImageView = (ImageView)itemView.findViewById(R.id.imgLocationIcon);
        }
    }
}
