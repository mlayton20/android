package com.laytonlabs.android.levelup;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by matthewlayton on 16/03/2016.
 */
public class SplashScreenActivity extends Activity {

    private ImageButton mPlayButton;
    private ImageButton mStatsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mPlayButton = (ImageButton)findViewById(R.id.splash_play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashScreenActivity.this, GameActivity.class);
                startActivity(i);
            }
        });

        mStatsButton = (ImageButton)findViewById(R.id.splash_leaderboard_button);
        mStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashScreenActivity.this, HighscoreActivity.class);
                startActivity(i);
            }
        });

    }
}
