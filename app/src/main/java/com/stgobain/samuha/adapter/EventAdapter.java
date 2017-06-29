package com.stgobain.samuha.adapter;

import android.content.Context;
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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolderEvent> {
    private LayoutInflater mInflater;
    private ArrayList<SamuhaEvent> samuhaEventArrayList = new ArrayList<>();
    public EventAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolderEvent onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_event_row, parent, false);
        ViewHolderEvent viewHolder = new ViewHolderEvent(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderEvent holder, int position) {
        SamuhaEvent currentEvent = samuhaEventArrayList.get(position);
        holder.eventDateTxt.setText(currentEvent.getEventDate());
        holder.eventNameTxt.setText(currentEvent.getEventName());
        holder.eventLocationTxt.setText(currentEvent.getEventLocation());
    }

    @Override
    public int getItemCount() {
        return samuhaEventArrayList.size();
    }

    public void setEvents(ArrayList<SamuhaEvent> eventArraylist) {
        samuhaEventArrayList = eventArraylist;
        notifyDataSetChanged();
    }

    public class ViewHolderEvent extends RecyclerView.ViewHolder {
        CustomFontTextView eventDateTxt;
        CustomFontTextView eventNameTxt;
        CustomFontTextView eventLocationTxt;
        ImageView locationImageView;
        public ViewHolderEvent(View itemView) {
            super(itemView);
            eventDateTxt = (CustomFontTextView)itemView.findViewById(R.id.txtDate);
            eventNameTxt = (CustomFontTextView)itemView.findViewById(R.id.txtEventName);
            eventLocationTxt =(CustomFontTextView)itemView.findViewById(R.id.txtEventLocationName);
            locationImageView = (ImageView)itemView.findViewById(R.id.imgLocationIcon);
        }
    }
}
