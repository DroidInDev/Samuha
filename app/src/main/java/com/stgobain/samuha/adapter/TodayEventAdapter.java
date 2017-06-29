package com.stgobain.samuha.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stgobain.samuha.CustomUserInterface.CustomFontTextView;
import com.stgobain.samuha.Model.SamuhaEvent;
import com.stgobain.samuha.R;

import java.util.ArrayList;

/**
 * Created by vignesh on 25-06-2017.
 */

public class TodayEventAdapter extends RecyclerView.Adapter<TodayEventAdapter.ViewHoldertodayEvent> {
    private LayoutInflater mInflater;
    private ArrayList<SamuhaEvent> todayEventList = new ArrayList<>();
    public TodayEventAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHoldertodayEvent onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_row_today_event, parent, false);
        ViewHoldertodayEvent viewHolder = new ViewHoldertodayEvent(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHoldertodayEvent holder, int position) {
        SamuhaEvent currentEvent = todayEventList.get(position);
        holder.todayEventTxt.setText(currentEvent.getEventName());
        holder.todayEventDatetxt.setText(currentEvent.getEventDate());
        holder.todayEventLocationTxt.setText(currentEvent.getEventLocation());
    }

    @Override
    public int getItemCount() {
        return todayEventList.size();
    }

    public void setScore(ArrayList<SamuhaEvent> scoreArraylist) {
        todayEventList = scoreArraylist;
        notifyDataSetChanged();
    }

    public void setTodayEvents(ArrayList<SamuhaEvent> todaysEventList) {
        todayEventList = todaysEventList;
        notifyDataSetChanged();
    }

    public class ViewHoldertodayEvent extends RecyclerView.ViewHolder {

        CustomFontTextView todayEventTxt;
        CustomFontTextView todayEventDatetxt;
        CustomFontTextView todayEventLocationTxt;
        public ViewHoldertodayEvent(View itemView) {
            super(itemView);
            todayEventTxt = (CustomFontTextView)itemView.findViewById(R.id.txttodayEventName);
            todayEventDatetxt = (CustomFontTextView)itemView.findViewById(R.id.txttodayEventDate);
            todayEventLocationTxt =(CustomFontTextView)itemView.findViewById(R.id.txttodayEventLocation);

        }
    }
}

