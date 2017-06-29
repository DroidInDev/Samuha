package com.stgobain.samuha.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.stgobain.samuha.CustomUserInterface.CustomFontTextView;
import com.stgobain.samuha.Model.Team;
import com.stgobain.samuha.R;

import java.util.ArrayList;

/**
 * Created by vignesh on 25-06-2017.
 */

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolderScore> {
    private LayoutInflater mInflater;
    private ArrayList<Team> samuhaScoreList = new ArrayList<>();
    Context context;
    public ScoreAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ViewHolderScore onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_score_row, parent, false);
        ViewHolderScore viewHolder = new ViewHolderScore(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderScore holder, int position) {
        Team currentEvent = samuhaScoreList.get(position);
        holder.scoreTxt.setText(currentEvent.getScore());
        holder.teamNameTxt.setText(currentEvent.getTeamName());
        holder.eventLocationTxt.setText(currentEvent.getShortDescription());
        Bitmap bitmap;
        switch (position) {
            case 0:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tech_now_flag);
                holder.profileImg.setImageBitmap(bitmap);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.digi_now_flag);
                holder.profileImg.setImageBitmap(bitmap);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cyper_now_flag);
                holder.profileImg.setImageBitmap(bitmap);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.beta_now_flag);
                holder.profileImg.setImageBitmap(bitmap);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return samuhaScoreList.size();
    }

    public void setScore(ArrayList<Team> scoreArraylist) {
        samuhaScoreList = scoreArraylist;
        notifyDataSetChanged();
    }

    public class ViewHolderScore extends RecyclerView.ViewHolder {
        ImageView profileImg;
        CustomFontTextView scoreTxt;
        CustomFontTextView teamNameTxt;
        CustomFontTextView eventLocationTxt;
        ImageView locationImageView;
        public ViewHolderScore(View itemView) {
            super(itemView);
            profileImg = (ImageView)itemView.findViewById(R.id.imgScoreAttr);
            scoreTxt = (CustomFontTextView)itemView.findViewById(R.id.txtScore);
            teamNameTxt = (CustomFontTextView)itemView.findViewById(R.id.txtScoreName);
            eventLocationTxt =(CustomFontTextView)itemView.findViewById(R.id.txtScorelocation);
            locationImageView = (ImageView)itemView.findViewById(R.id.imgLocationIcon);
        }
    }
}
