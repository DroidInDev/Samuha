package com.stgobain.samuha.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stgobain.samuha.CustomUserInterface.CustomFontTextView;
import com.stgobain.samuha.Model.Announcement;
import com.stgobain.samuha.R;

import java.util.ArrayList;

/**
 * Created by vignesh on 25-06-2017.
 */

public class AnnouncementAdapter  extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolderAnnouncemnet> {
    private LayoutInflater mInflater;
    private ArrayList<Announcement> samuhaAnnounceList = new ArrayList<>();
    public AnnouncementAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolderAnnouncemnet onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_row_announcement, parent, false);
        ViewHolderAnnouncemnet viewHolder = new ViewHolderAnnouncemnet(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderAnnouncemnet holder, int position) {
        Announcement currentEvent = samuhaAnnounceList.get(position);
        holder.announceNameTxt.setText(currentEvent.getTitle());
        holder.anounceDescriptiontxt.setText(currentEvent.getUpdates());

    }

    @Override
    public int getItemCount() {
      //  return samuhaAnnounceList == null ? 0 : samuhaAnnounceList.size();
        return this.samuhaAnnounceList.size();
    }

    public void setAnnouncements(ArrayList<Announcement> AnnounceArraylist) {
        this.samuhaAnnounceList.clear();
        this.samuhaAnnounceList.addAll(AnnounceArraylist);
        notifyDataSetChanged();
    }

    public class ViewHolderAnnouncemnet extends RecyclerView.ViewHolder {

        CustomFontTextView announceNameTxt;
        CustomFontTextView anounceDescriptiontxt;

        public ViewHolderAnnouncemnet(View itemView) {
            super(itemView);
            announceNameTxt = (CustomFontTextView)itemView.findViewById(R.id.txtAnnounceName);
            anounceDescriptiontxt = (CustomFontTextView)itemView.findViewById(R.id.txtAnnounceDescription);


        }
    }
}


