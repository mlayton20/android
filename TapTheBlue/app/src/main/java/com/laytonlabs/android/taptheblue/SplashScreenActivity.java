package com.laytonlabs.android.taptheblue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kobakei.ratethisapp.RateThisApp;

/**
 * Created by matthewlayton on 24/04/2016.
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
        mTracker = ((TapBlueApp)getApplication()).getDefaultTracker();
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

        // Custom criteria: 3 days and 3 launches
        RateThisApp.Config config = new RateThisApp.Config(3, 3);
        // Custom title and message
        config.setTitle(R.string.rate_title);
        config.setMessage(R.string.rate_text);
        RateThisApp.init(config);

        //We want to send the events to track on analytics.
        RateThisApp.setCallback(new RateThisApp.Callback() {
            @Override
            public void onYesClicked() {
                sendActionEvent("Splash - Rate - Yes");
            }

            @Override
            public void onNoClicked() {
                sendActionEvent("Splash - Rate - No");
            }

            @Override
            public void onCancelClicked() {
                sendActionEvent("Splash - Rate - Cancel");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Monitor launch times and interval from installation
        RateThisApp.onStart(this);
        // If the criteria is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this);
    }

    private void sendActionEvent(String action) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction(action)
                .build());
    }
}
