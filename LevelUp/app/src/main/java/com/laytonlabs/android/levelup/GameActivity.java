/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laytonlabs.android.levelup;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.laytonlabs.android.levelup.game.GameStat;
import com.laytonlabs.android.levelup.game.GameStats;
import com.laytonlabs.android.levelup.game.Level;
import com.laytonlabs.android.levelup.game.Score;

public class GameActivity extends Activity implements GameEventListener {

	private static final String TAG = "GameActivity";
    private GLSurfaceView mGLView;
    private Tracker mTracker;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Obtain the shared Tracker instance.
        mTracker = ((LevelUpApp)getApplication()).getDefaultTracker();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.AppViewBuilder().build());

        //Call the GameStats to get the latest stats initialised.
        GameStats.get(this);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new GameSurfaceView(this);
        setContentView(mGLView);
        handler = new Handler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        if (mGLView != null) {
        	mGLView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        if (mGLView != null) {
        	mGLView.onResume();
        }
    }

	@Override
	public void onGameOver() {
		Intent i = new Intent(GameActivity.this, GameOverActivity.class);
		GameStats.get(this).addGameStat(new GameStat());
		sendGameStats();
    	startActivity(i);
    	mGLView = null;
	}
	
	private void sendGameStats() {
		GameStat latestGameStat = GameStats.get(this).getLatestGameStat();
		String label = "Score";
		if (latestGameStat.getmScoreRank() == 1) {
			label += " - New Best";
		}
		
		mTracker.send(new HitBuilders.EventBuilder()
				.setCategory("Achievement")
				.setAction("Game Over")
				.setLabel(label)
				.setValue(Score.get())
				.build());
		
		label = "Level";
		if (latestGameStat.getmLevelRank() == 1) {
			label += " - New Best";
		}
		
		mTracker.send(new HitBuilders.EventBuilder()
				.setCategory("Achievement")
				.setAction("Game Over")
				.setLabel(label)
				.setValue(Level.get())
				.build());
		
		mTracker.send(new HitBuilders.EventBuilder()
			.setCategory("Achievement")
			.setAction("Game Over")
			.setLabel("Games Played")
			.setValue(GameStats.get(this).getGameStats().size())
			.build());
	}
}