package com.laytonlabs.android.taptheblue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.laytonlabs.android.taptheblue.game.GameStat;
import com.laytonlabs.android.taptheblue.game.GameStats;
import com.laytonlabs.android.taptheblue.game.Score;

import java.util.ArrayList;

/**
 * Created by matthewlayton on 24/04/2016.
 */
public class GameOverActivity extends Activity {

    private static final String TAG = "GameOverActivity";
    private Tracker mTracker;

    private TextView mScoreTextView;
    private TextView mScoreRankTextView;
    private ImageButton mRestartButton;
    private ImageButton mShareButton;
    private ImageButton mMenuButton;
    private RelativeLayout mHighscoreWidget;
    private TextView mHighscoreFirst;
    private TextView mHighscoreSecond;
    private TextView mHighscoreThird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);

        // Obtain the shared Tracker instance.
        mTracker = ((TapBlueApp)getApplication()).getDefaultTracker();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.AppViewBuilder().build());

        GameStat latestGameStat = GameStats.get(this).getLatestGameStat();
        mScoreTextView = (TextView)findViewById(R.id.gameover_score);
        mScoreTextView.setText(Score.getLabel());

        mScoreRankTextView = (TextView)findViewById(R.id.gameover_score_rank);
        mScoreRankTextView.setText(getLeaderboardStat(latestGameStat.getmScoreRank()));

        //Highscore Widget
        mHighscoreWidget = (RelativeLayout)findViewById(R.id.gameover_highscore_widget);
        mHighscoreFirst = (TextView)findViewById(R.id.gameover_hs_first_value);
        mHighscoreSecond = (TextView)findViewById(R.id.gameover_hs_second_value);
        mHighscoreThird = (TextView)findViewById(R.id.gameover_hs_third_value);

        //Show top 3 stats if 3 games have been played by now. Otherwise hide widget.
        ArrayList<GameStat> top3Stats = GameStats.get(this).getTopXStatsByScore(3);
        if (top3Stats.size() == 3) {
            mHighscoreFirst.setText(top3Stats.get(0).getmScoreText());
            mHighscoreSecond.setText(top3Stats.get(1).getmScoreText());
            mHighscoreThird.setText(top3Stats.get(2).getmScoreText());

            //Change the colour of the text if they just got a top 3 score.
            mHighscoreFirst.setTextColor(getHighscoreTextColor(1, latestGameStat));
            mHighscoreSecond.setTextColor(getHighscoreTextColor(2,latestGameStat));
            mHighscoreThird.setTextColor(getHighscoreTextColor(3,latestGameStat));

            mHighscoreWidget.setVisibility(View.VISIBLE);
        } else {
            mHighscoreWidget.setVisibility(View.GONE);
        }

        mRestartButton = (ImageButton)findViewById(R.id.gameover_restart);
        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActionEvent("GameOver - Restart");
                restartGame(Constants.INTENT_RESART_VIA_RESART_BTN);
            }
        });

        mMenuButton = (ImageButton)findViewById(R.id.gameover_mainmenu);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActionEvent("GameOver - Menu");
                Intent i = new Intent(GameOverActivity.this, SplashScreenActivity.class);
                startActivity(i);
            }
        });

        mShareButton = (ImageButton)findViewById(R.id.gameover_share);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActionEvent("GameOver - Share");
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.gameover_share_subject,
                                Score.getLabel()));
                i.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_url));
                startActivity(i);
            }
        });
    }

    private int getHighscoreTextColor(int podiumNumber, GameStat latestGameStat) {
        if (podiumNumber == latestGameStat.getmScoreRank()) {
            return ContextCompat.getColor(this, R.color.yellow);
        } else {
            return ContextCompat.getColor(this, R.color.white);
        }
    }

    private void sendActionEvent(String action) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction(action)
                .build());
    }

    @Override
    public void onBackPressed() {
        sendActionEvent("GameOver - Restart - Back Button");
        restartGame(Constants.INTENT_RESART_VIA_BACK_BTN);
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

    private void restartGame(String input) {
        Intent i = new Intent(GameOverActivity.this, GameActivity.class);
        i.putExtra(Constants.INTENT_RESART_VIA, input);
        startActivity(i);
    }

    @Override
    public void onPause() {
        super.onPause();
        //Save game stats before the activity ends
        GameStats.get(this).saveGameStats();
    }
}
