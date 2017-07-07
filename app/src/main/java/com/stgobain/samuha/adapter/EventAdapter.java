package com.stgobain.samuha.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.stgobain.samuha.CustomUserInterface.CustomFontTextView;
import com.stgobain.samuha.Model.SamuhaEvent;
import com.stgobain.samuha.R;

import java.util.ArrayList;

/**
 * Created by vignesh on 15-06-2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHoldertodayEvent> {
private LayoutInflater mInflater;
    private Context context;
    private ArrayList<SamuhaEvent> todayEventList = new ArrayList<>();
public EventAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    this.context = context;
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

public void setEvents(ArrayList<SamuhaEvent> eventArrayList) {
        todayEventList.clear();
        todayEventList.addAll(eventArrayList);
        notifyDataSetChanged();
        }

public class ViewHoldertodayEvent extends RecyclerView.ViewHolder implements View.OnClickListener {

    CustomFontTextView todayEventTxt;
    CustomFontTextView todayEventDatetxt;
    CustomFontTextView todayEventLocationTxt;
    ImageView imgLoactionIcon;
    public ViewHoldertodayEvent(View itemView) {
        super(itemView);
        todayEventTxt = (CustomFontTextView)itemView.findViewById(R.id.txttodayEventName);
        todayEventDatetxt = (CustomFontTextView)itemView.findViewById(R.id.txttodayEventDate);
        todayEventLocationTxt =(CustomFontTextView)itemView.findViewById(R.id.txttodayEventLocation);
        imgLoactionIcon = (ImageView)itemView.findViewById(R.id.imgusericon);
        imgLoactionIcon.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        if(view.getId()==R.id.imgusericon) {
            String locationUrl =todayEventList.get(position).getLocUrl();
            Uri gmmIntentUri = Uri.parse(locationUrl);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(mapIntent);
            }
        }else if(view.getId()==R.id.layoutAnnounce){

        }
    }
}
}

