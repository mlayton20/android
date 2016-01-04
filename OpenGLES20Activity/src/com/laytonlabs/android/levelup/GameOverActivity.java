package com.laytonlabs.android.levelup;

import com.laytonlabs.android.levelup.game.GameStat;
import com.laytonlabs.android.levelup.game.GameStats;
import com.laytonlabs.android.levelup.game.Level;
import com.laytonlabs.android.levelup.game.Score;
import com.laytonlabs.android.levelup.game.Time;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends Activity {

	private TextView mScoreTextView;
	private TextView mLevelTextView;
	private TextView mTimeTextView;
	private Button mRestartButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gameover);
		
		GameStat latestGameStat = GameStats.get(this).getLatestGameStat();
		mScoreTextView = (TextView)findViewById(R.id.gameover_score);
		mScoreTextView.setText(Score.getLabel() + getLeaderboardStat(latestGameStat.getmScoreRank()));		
		
		mLevelTextView = (TextView)findViewById(R.id.gameover_level);
		mLevelTextView.setText(Level.getLabel() + getLeaderboardStat(latestGameStat.getmLevelRank()));
		
		mTimeTextView = (TextView)findViewById(R.id.gameover_time);
		mTimeTextView.setText(Time.getTimeElapsedLabel());
		
		mRestartButton = (Button)findViewById(R.id.gameover_restart);
		mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GameOverActivity.this, GameActivity.class);
            	startActivity(i);
            }
        });
	}

	private String getLeaderboardStat(int stat) {
		String output = "";
		//if there's been more than 1 game played, show best stat if possible
		if (GameStats.get(this).getGameStats().size() > 2) {
			//If its the best then show it
			if (stat == 1) {
				output += "(PB!)";
			//If its top 10 effort
			} else if (stat <= 10) {
				output += "(" + stat + ")";
			//Otherwise just show its above top 10.
			} else {
				output += "(10+)";
			}
		}
		return output;
	}
}
