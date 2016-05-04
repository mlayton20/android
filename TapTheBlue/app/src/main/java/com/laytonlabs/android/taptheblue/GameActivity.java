package com.laytonlabs.android.taptheblue;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.laytonlabs.android.taptheblue.game.Colors;
import com.laytonlabs.android.taptheblue.game.GameStat;
import com.laytonlabs.android.taptheblue.game.GameStats;
import com.laytonlabs.android.taptheblue.game.Move;
import com.laytonlabs.android.taptheblue.game.Score;
import com.laytonlabs.android.taptheblue.game.Time;

import java.util.ArrayList;

/**
 * Created by matthewlayton on 18/04/2016.
 */
public class GameActivity extends Activity {

    private static final String TAG = "GameActivity";
    private static final String GAMEOVER_VIA_TIMEOUT = "GameOver Via Timeout";
    private static final String GAMEOVER_VIA_MISS = "GameOver Via Miss";
    private Tracker mTracker;
    private InterstitialAd mInterstitialAd;
    private String iRestartVia;
    private ArrayList<Integer> occupiedCells;
    private RelativeLayout gameBoard;
    private int currentBlueLocation = -1;
    private TextView mScoreTextView;
    private TextView mTimeTextView;
    private boolean isGameComplete;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            Time.update();

            if (Time.isTimeUp()) {
                onGameOver(GAMEOVER_VIA_TIMEOUT);
                timerHandler.removeCallbacks(this);
            }

            if (Time.isTimeAlmostUp()) {
                mTimeTextView.setTextColor(Color.RED);
            } else {
                mTimeTextView.setTextColor(Color.WHITE);
            }

            mTimeTextView.setText(Time.getTimeRemainingLabel());

            timerHandler.postDelayed(this, 30);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Obtain the shared Tracker instance.
        mTracker = ((TapBlueApp)getApplication()).getDefaultTracker();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.AppViewBuilder().build());

        //Google Ads call to get a new full screen banner ad after game ends.
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                goToGameOverScreen();
            }
        });

        requestNewInterstitial();

        //This is for when the user plays multiple sessions in one go.
        if (getIntent().getExtras() != null) {
            iRestartVia = getIntent().getExtras().getString(Constants.INTENT_RESART_VIA);
        }

        isGameComplete = false;

        gameBoard = (RelativeLayout)findViewById(R.id.game_board);
        mScoreTextView = (TextView)findViewById(R.id.game_score);
        mTimeTextView = (TextView)findViewById(R.id.game_time);

        //Reset Game
        Score.reset();
        Move.reset();
        Colors.reset();

        //Start the timer.
        Time.initialise();
        timerHandler.postDelayed(timerRunnable, 0);

        initBoard(gameBoard);
        clearBoard(gameBoard);
        placeBlueCell(gameBoard);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("5E68FCB4DE8AFC4C4DE55B90D685F618")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Need to update timeRemaining when game is paused because prevtime will be way ahead of timeRemaining
        Time.onResume();

        timerHandler.postDelayed(timerRunnable, 0);
    }

    private void placeBlueCell(RelativeLayout gameBoard) {
        currentBlueLocation = generateRandomNumber(gameBoard, -1);
        placeCell(gameBoard,
                currentBlueLocation,
                Colors.BLUE);
    }

    private void placeBlueCell(RelativeLayout gameBoard, int prevId) {
        currentBlueLocation = generateRandomNumber(gameBoard, prevId);
        placeCell(gameBoard,
                currentBlueLocation,
                Colors.BLUE);
    }

    private void placeCell(RelativeLayout gameBoard, int i, int color) {
        Log.d(TAG, "Cell will be placed here: " + i);
        if(android.os.Build.VERSION.SDK_INT < 16) {
            //TODO - Fix this to be makeSelector call
            ((Button)gameBoard.getChildAt(i)).setBackgroundResource(color);
        } else {
            ((Button)gameBoard.getChildAt(i)).setBackground(makeSelector(color));
        }
    }

    private int generateRandomNumber(RelativeLayout gameBoard, int prevId) {
        int selectedCell;
        boolean isGoodNumber = false;

        do {
            selectedCell = (int)(Math.random() * gameBoard.getChildCount());
            //Check to see if the cell is currently free.
            if (isFreeCell(selectedCell)) {
                //Now we want to check if the cell is not the same as it was before.
                if (selectedCell != prevId) {
                    isGoodNumber = true;
                }
            }
        } while (!isGoodNumber);

        occupiedCells.add(selectedCell);
        return selectedCell;
    }

    private boolean isFreeCell(int selectedCell) {
        for (int value : occupiedCells) {
            if (value == selectedCell) {
                return false;
            }
        }
        return true;
    }

    private void initBoard(RelativeLayout gameBoard) {
        for (int i = 0; i < gameBoard.getChildCount(); i++) {
            View v = gameBoard.getChildAt(i);
            Button button = (Button) v;

            //Assign the non centre cells a value
            if (button.getId() == -1) {
                button.setId(i);
            }

            //Add a click listener for when a user presses the cell.
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Cell pressed: " + getButtonId(v.getId()));
                    onCellTouch(getButtonId(v.getId()));
                }
            });
        }
    }

    private boolean clearBoard(RelativeLayout gameBoard) {
        occupiedCells = new ArrayList<>();
        for (int i = 0; i < gameBoard.getChildCount(); i++) {
            View v = gameBoard.getChildAt(i);
            if (v instanceof Button) {
                Button button = (Button) v;

                //Reset the background
                if(android.os.Build.VERSION.SDK_INT < 16) {
                    //TODO - Fix this for older versions to be makeSelector call
                    button.setBackgroundResource(R.drawable.round_button);
                } else {
                    //button.setBackground(ContextCompat.getDrawable(this, R.drawable.round_button));
                    button.setBackground(makeSelector(Colors.getRandomColor()));
                }

            } else {
                return false;
            }
        }
        return true;
    }

    public static StateListDrawable makeSelector(int color) {
        StateListDrawable res = new StateListDrawable();
        res.addState(new int[]{android.R.attr.state_pressed}, getButtonShape(color, 200));
        res.addState(new int[]{android.R.attr.state_enabled}, getButtonShape(color, 255));
        return res;
    }

    private static GradientDrawable getButtonShape(int color, int opacity) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(color);
        shape.setAlpha(opacity);
        return shape;
    }

    private void onCellTouch(int buttonId) {
        //We only want to continue if the blue cell was selected. Otherwise it's game over.
        if (buttonId != currentBlueLocation) {
            Log.d(TAG, "Game Over!");
            onGameOver(GAMEOVER_VIA_MISS);
            return;
        }
        //The blue cell was selected, so move to next level.
        correctMove(buttonId);
    }

    private void correctMove(int buttonId) {
        //If there's been 10 moves, start lowering the time per go.
        if (Move.get() % 10 == 0) {
            Time.decreaseCorrectAnswerTime();
        }

        Time.resetTimeRemaining();
        clearBoard(gameBoard);
        placeBlueCell(gameBoard, buttonId);
        updateScore();
        Move.increment();
        Colors.incrementMaxColorRange();
    }

    private void onGameOver(String gameOverReason) {
        isGameComplete = true;
        Time.updateGameTime();
        GameStats.get(this).addGameStat(new GameStat());
        sendGameStats(gameOverReason);

        //TODO - Go to gameover screen or call interstital ad.
        showInterstitial();
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            goToGameOverScreen();
        }
    }

    private void goToGameOverScreen() {
        Intent i = new Intent(GameActivity.this, GameOverActivity.class);
        startActivity(i);
    }

    private void updateScore() {
        Score.increment();
        mScoreTextView.setText(Score.getLabel());
    }

    private int getButtonId(int id) {
        //Since the centre button is allocated id in XML, we want to change that to be 0 for the game.
        return id >= gameBoard.getChildCount() ? 0 : id;
    }

    private void sendGameStats(String gameOverReason) {
        GameStat latestGameStat = GameStats.get(this).getLatestGameStat();
        String label = "Score";
        int gamesPlayed = GameStats.get(this).getGameStats().size();
        if (latestGameStat.getmScoreRank() == 1) {
            label += " - New Best";
        }

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Achievement")
                .setAction("Game Over")
                .setLabel(label)
                .setValue(Score.get())
                .build());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Achievement")
                .setAction("Game Over")
                .setLabel(gameOverReason)
                .setValue(Score.get())
                .build());

        //Complete Game for First Time
        if (gamesPlayed == 1) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Usage")
                    .setAction("Games Complete")
                    .setLabel("Completed First Game - " + (Score.get() > 0 ? "With Score" : "No Score"))
                    .setValue(Score.get())
                    .build());
            //Played 5/10/15/... games
        } else if (gamesPlayed % 5 == 0) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Usage")
                    .setAction("Games Complete")
                    .setLabel("Completed " + gamesPlayed + " games")
                    .build());
        }

        //Complete game after restart
        if (iRestartVia != null && !iRestartVia.equals("")) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Usage")
                    .setAction("Games Complete")
                    .setLabel("Completed game after restart - " + (Score.get() > 0 ? "With Score" : "No Score") + " (Via " + iRestartVia + ")")
                    .setValue(Score.get())
                    .build());
        }
    }

    @Override
    public void onStop() {
        //Only send report when the game is stopped before game completes.
        if (!isGameComplete) {
            sendStateReport("Stopped");
        }
        super.onStop();
    }

    @Override
    public void onRestart() {
        sendStateReport("Resumed");
        super.onRestart();
    }

    private void sendStateReport(String state) {
        //Check if its the first ever game
        if (GameStats.get(this).getGameStats().size() == 0) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Usage")
                    .setAction("Abort")
                    .setLabel(state + " first ever game - " + (Score.get() > 0 ? "With Score" : "No Score"))
                    .setValue(Score.get())
                    .build());
        }

        //Check if its a consecutive game in same session
        if (iRestartVia != null && !iRestartVia.equals("")) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Usage")
                    .setAction("Abort")
                    .setLabel(state + " game after restart - " + (Score.get() > 0 ? "With Score" : "No Score") + " (Via " + iRestartVia + ")")
                    .setValue(Score.get())
                    .build());
        }
    }
}
