package com.allexamnotes.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,YouTubePlayer.OnFullscreenListener {

    // YouTube player view
    private YouTubePlayerView youTubeView;
    private YouTubePlayer mPlayer;
    private String videoId,videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Config.FORCE_RTL) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_youtube_player);

        if (getIntent()!=null){
            videoId = getIntent().getStringExtra("videoId");
            videoUrl = getIntent().getStringExtra("videoUrl");
        }

        if (videoId==null){
            videoId = extractYTId(videoUrl);
        }

        youTubeView = findViewById(R.id.youtubePlayerView);

        // Initializing video player with developer key
        youTubeView.initialize(Config.YT_API_KEY, this);
    }

    public static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            vId = matcher.group(1);
        }
        return vId;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            mPlayer = youTubePlayer;
            mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            mPlayer.setFullscreen(true);
            mPlayer.loadVideo(videoId);
            mPlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, 1).show();
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.YT_API_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtubePlayerView);
    }

    @Override
    public void onFullscreen(boolean b) {

    }
}
