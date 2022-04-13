package com.appdroid.ssbtproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.appdroid.ssbtproject.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoPlayer extends AppCompatActivity {
    YouTubePlayerView youTubePlayerView;
    ImageView fullScreanBTN;
    boolean fullscreen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        String link = getIntent().getStringExtra("link");
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        fullScreanBTN = findViewById(R.id.fullScreanBTN);

        getLifecycle().addObserver(youTubePlayerView);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(link, 0);
            }
        });

        fullScreanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullscreen) {
                    makeFullScreen();
                }else{
                    exitFromFullScreen();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }

    private void makeFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        if(getSupportActionBar() != null){
            getSupportActionBar().show();
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fullscreen = false;
    }

    private void exitFromFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        fullscreen = true;
    }
}