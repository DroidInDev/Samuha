package com.stgobain.samuha.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.stgobain.samuha.R;

/**
 * Created by vignesh on 28-06-2017.
 */

public class VideoPrecviewActivity extends AppCompatActivity implements OnPreparedListener, OnCompletionListener {
    private VideoView videoView;
    String videoUrl ="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);
        Bundle B= getIntent().getExtras();

        if(B!=null){
            videoUrl = B.getString("videoUrl");
        }
        setupVideoView();
    }
    private void setupVideoView() {
        // Make sure to use the correct VideoView import
        videoView = (VideoView)findViewById(R.id.video_view);
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        //For now we just picked an arbitrary item to play
        videoView.setVideoURI(Uri.parse(videoUrl));
    }

    @Override
    public void onPrepared() {
        //Starts the video playback as soon as it is ready
        videoView.start();
    }

    @Override
    public void onCompletion() {
        videoView.restart();
    }
}
