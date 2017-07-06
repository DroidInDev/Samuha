package com.stgobain.samuha.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.stgobain.samuha.AppCallbacks.MemoriesActionListener;
import com.stgobain.samuha.CustomUserInterface.CustomFontTextView;
import com.stgobain.samuha.Model.MemeoriesData;
import com.stgobain.samuha.R;
import com.stgobain.samuha.network.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by vignesh on 26-06-2017.
 */

public class MemoryFeedAdapter  extends RecyclerView.Adapter<MemoryFeedAdapter.ViewHolderMemory> {
    private LayoutInflater mInflater;
    private ArrayList<MemeoriesData> samuhaMemoryArrayList = new ArrayList<>();
    ImageLoader imageLoader;
    Context context;
    MemoriesActionListener mActionListener;
    public MemoryFeedAdapter(Context context, MemoriesActionListener memoriesActionListener) {
        mInflater = LayoutInflater.from(context);
        this.imageLoader = VolleySingleton.getInstance().getImageLoader();
        this.context =context;
        mActionListener = memoriesActionListener;
    }

    @Override
    public ViewHolderMemory onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_feed_memories, parent, false);
        ViewHolderMemory viewHolder = new ViewHolderMemory(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderMemory holder, int position) {
        MemeoriesData currentMemory = samuhaMemoryArrayList.get(position);
        holder.memoryDateTxt.setText(currentMemory.getPost_date());
        holder.memoryNameTxt.setText(currentMemory.getEvent_name());
        holder.memoryDescTxt.setText("Total votes :"+currentMemory.getTotal_vote());
        if(currentMemory.getFile_type().equals("video")){
            holder.playImg.setVisibility(View.VISIBLE);
        }else
        {
            holder.playImg.setVisibility(View.INVISIBLE);
        }
        if(currentMemory.getUser_vote_status().equals("true")){
            holder.likeImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.voted_icon));
        }else{
            holder.likeImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.vote_icon));
        }
        String imgUrl = currentMemory.getFile_name();
        setUserPost(imgUrl,holder);
    }
    private void setUserPost(String userPost, final ViewHolderMemory holder) {
        if (!userPost.equals("NA")&& !userPost.equals(null)) {
            this.imageLoader.get(userPost, new ImageLoader.ImageListener() {
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Bitmap bitmap = response.getBitmap();
                    if (bitmap != null) {
               //         holder.progressBar.setVisibility(8);
                    }
                    holder.contnetImg.setImageBitmap(bitmap);
                    if (holder.contnetImg.isAttachedToWindow() || !isImmediate) {
                      //  holder.progressBar.setVisibility(8);
                    }
                }

                public void onErrorResponse(VolleyError error) {
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return samuhaMemoryArrayList.size();
    }

    public void setMemoryFeeds(ArrayList<MemeoriesData> memoryFeedArraylist) {
        int previousDataSize = this.samuhaMemoryArrayList.size();
        this.samuhaMemoryArrayList.addAll(memoryFeedArraylist);
      //  notifyItemRangeInserted(previousDataSize, samuhaMemoryArrayList.size());
        notifyDataSetChanged();

    }

    public class ViewHolderMemory extends RecyclerView.ViewHolder implements View.OnClickListener {
        CustomFontTextView memoryDateTxt;
        CustomFontTextView memoryNameTxt;
        CustomFontTextView memoryDescTxt;
        ImageView likeImageView;
        ImageView contnetImg;
        ImageView playImg;
        public ViewHolderMemory(View itemView) {
            super(itemView);
            memoryDateTxt = (CustomFontTextView)itemView.findViewById(R.id.txtMemoriesDate);
            memoryNameTxt = (CustomFontTextView)itemView.findViewById(R.id.txtEventMemoriesName);
            memoryDescTxt =(CustomFontTextView)itemView.findViewById(R.id.txtMemoriesEventDesc);
            likeImageView = (ImageView)itemView.findViewById(R.id.imgLocationIcon);
            contnetImg = (ImageView)itemView.findViewById(R.id.imgMemory);
            playImg =(ImageView)itemView.findViewById(R.id.imgplay);
            playImg.setOnClickListener(this);
            likeImageView.setOnClickListener(this);
            contnetImg.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            MemeoriesData memeoriesData = samuhaMemoryArrayList.get(position);
            if(view.getId()==R.id.imgLocationIcon){
                if(memeoriesData.getUser_vote_status().equals("false")){
                    samuhaMemoryArrayList.get(position).setUser_vote_status("true");
                    likeImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.voted_icon));
                }
                mActionListener.likeClicked(memeoriesData.getId());
            }else if(view.getId()==R.id.imgplay){
                mActionListener.videoSelected(memeoriesData.getFile_name());
            }else if(view.getId()==R.id.imgMemory){
                String mediaType =   samuhaMemoryArrayList.get(position).getFile_type();
                if(mediaType.equals("image")){
                    String imgUrl = samuhaMemoryArrayList.get(position).getFile_name();
                    mActionListener.imageClicked(imgUrl);
                }
            }
        }
    }

}

