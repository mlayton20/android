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
	private TextView mScoreRankTextView;
	private TextView mLevelTextView;
	private TextView mLevelRankTextView;
	private TextView mTimeTextView;
	private Button mRestartButton;
	private Button mShareButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gameover);
		
		GameStat latestGameStat = GameStats.get(this).getLatestGameStat();
		mScoreTextView = (TextView)findViewById(R.id.gameover_score);
		mScoreTextView.setText(Score.getLabel());
		
		mScoreRankTextView = (TextView)findViewById(R.id.gameover_score_rank);
		mScoreRankTextView.setText(getLeaderboardStat(latestGameStat.getmScoreRank()));
		
		mLevelTextView = (TextView)findViewById(R.id.gameover_level);
		mLevelTextView.setText(Level.getLabel());
		
		mLevelRankTextView = (TextView)findViewById(R.id.gameover_level_rank);
		mLevelRankTextView.setText(getLeaderboardStat(latestGameStat.getmLevelRank()));
		
		mTimeTextView = (TextView)findViewById(R.id.gameover_time);
		mTimeTextView.setText(Time.getTimeElapsedLabel());
		
		mRestartButton = (Button)findViewById(R.id.gameover_restart);
		mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });
		
		mShareButton = (Button)findViewById(R.id.gameover_share);
		mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_SUBJECT, 
						getString(R.string.share_gameover_subject, 
								Score.getLabel(), 
								Level.getLabel()));
				//TODO change the below to point to my play store app when its created
				i.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_game_url));
				startActivity(i);
            }
        });
	}
	
	@Override
	public void onBackPressed() {
		restartGame();
	}

	private String getLeaderboardStat(int stat) {
		String output = "";
		//if there's been more than 1 game played, show best stat if possible
		if (GameStats.get(this).getGameStats().size() > 2) {
			//If its the best then show it
			if (stat == 1) {
				output += getOrdinalSuffix(stat) + "!";
			//If its top 10 effort
			} else if (stat <= 10) {
				output += getOrdinalSuffix(stat);
			//Otherwise just show its above top 10.
			} else {
				output += "10+";
			}
		}
		return output;
	}
	
	private String getOrdinalSuffix(int position) {
	    int j = position % 10;
	    int k = position % 100;
	    if (j == 1 && k != 11) {
	        return position + "st";
	    }
	    if (j == 2 && k != 12) {
	        return position + "nd";
	    }
	    if (j == 3 && k != 13) {
	        return position + "rd";
	    }
	    return position + "th";
	}

	private void restartGame() {
		Intent i = new Intent(GameOverActivity.this, GameActivity.class);
		startActivity(i);
	}
}
