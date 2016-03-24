package com.laytonlabs.android.levelup;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by matthewlayton on 16/03/2016.
 */
public class SplashScreenActivity extends Activity {

    private static final String TAG = "SplashScreenActivity";
    private Tracker mTracker;
    private ImageButton mPlayButton;
    private ImageButton mStatsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Obtain the shared Tracker instance.
        mTracker = ((LevelUpApp)getApplication()).getDefaultTracker();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.AppViewBuilder().build());

        mPlayButton = (ImageButton)findViewById(R.id.splash_play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActionEvent("Splash - Play");
                Intent i = new Intent(SplashScreenActivity.this, GameActivity.class);
                startActivity(i);
            }
        });

        mStatsButton = (ImageButton)findViewById(R.id.splash_leaderboard_button);
        mStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActionEvent("Splash - Stats");
                Intent i = new Intent(SplashScreenActivity.this, HighscoreActivity.class);
                startActivity(i);
            }
        });

    }

    private void sendActionEvent(String action) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction(action)
                .build());
    }
}
