package com.example.moviesrappi;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import static com.example.moviesrappi.httpconects.Api.api_key_youtube;

public class YouTubeActivity extends YouTubeBaseActivity {
    String VIDEO_ID;
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // attaching layout xml
        setContentView(R.layout.activity_youtube_view);
        VIDEO_ID = getIntent().getStringExtra("VIDEO_ID");
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Initializing YouTube player view
        youTubePlayerView.initialize(api_key_youtube, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(null== youTubePlayer) return;

                // Start buffering
                if (!b) {
                    youTubePlayer.cueVideo(VIDEO_ID);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                //Toast.makeText(this, "Failed to initialize.", Toast.LENGTH_LONG).show();
            }
        });
    }
}