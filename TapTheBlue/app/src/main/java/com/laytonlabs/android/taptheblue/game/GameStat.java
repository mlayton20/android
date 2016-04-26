package com.laytonlabs.android.taptheblue.game;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by matthewlayton on 24/04/2016.
 */
public class GameStat {

    private static final String JSON_ID = "id";
    private static final String JSON_DATE = "date";
    private static final String JSON_SCORE = "score";
    private static final String JSON_SCORE_RANK = "scoreRank";
    private static final String JSON_MOVE = "move";
    private static final String JSON_GAME_TIME = "gameTime";

    private UUID mId;
    private Date mDate;
    private int mScore;
    private int mScoreRank;
    private int mMove;
    private long mGameTime;

    public GameStat() {
        // Generate Unique identifier
        mId = UUID.randomUUID();
        mDate = new Date();
        mScore = Score.get();
        mMove = Move.get();
        mGameTime = Time.getGameTime();
    }

    public GameStat(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        mDate = new Date(json.getLong(JSON_DATE));
        mScore = json.getInt(JSON_SCORE);
        mScoreRank = json.getInt(JSON_SCORE_RANK);
        mMove = json.getInt(JSON_MOVE);
        mGameTime = json.getLong(JSON_GAME_TIME);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_DATE, mDate.getTime());
        json.put(JSON_SCORE, mScore);
        json.put(JSON_SCORE_RANK, mScoreRank);
        json.put(JSON_MOVE, mMove);
        json.put(JSON_GAME_TIME, mGameTime);
        return json;
    }

    public UUID getmId() {
        return mId;
    }

    public int getmScore() {
        return mScore;
    }

    public String getmScoreText() {
        return Integer.toString(mScore);
    }

    public int getmMove() {
        return mMove;
    }

    public String getmMoveText() {
        return Integer.toString(mMove);
    }

    public int getmScoreRank() {
        return mScoreRank;
    }

    public void setmScoreRank(int mScoreRank) {
        this.mScoreRank = mScoreRank;
    }

    public String getmDateFriendly() {
        return "" + DateUtils.getRelativeTimeSpanString(mDate.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
    }

    @Override
    public String toString() {
        try {
            return toJSON().toString();
        } catch (JSONException e) {
            Log.e("GameStat", "Error converting JSON to string");
            return null;
        }
    }
}
