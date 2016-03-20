package com.laytonlabs.android.levelup.game;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateUtils;
import android.util.Log;

public class GameStat {

	private static final String JSON_ID = "id";
	private static final String JSON_DATE = "date";
	private static final String JSON_SCORE = "score";
	private static final String JSON_SCORE_RANK = "scoreRank";
	private static final String JSON_LEVEL = "level";
	private static final String JSON_LEVEL_RANK = "levelRank";
	private static final String JSON_TIME_ELAPSED = "timeElapsed";
	private static final String JSON_TIME_REMAINING = "timeRemaining";
	private static final String JSON_CURRENT_ANSWER = "currentAnswer";
	
	private UUID mId;
	private Date mDate;
	private int mScore;
	private int mScoreRank;
	private int mLevel;
	private int mLevelRank;
	private long mTimeElapsed;
	private long mTimeRemaining;
	private int mCurrentAnswer;

	public GameStat() {
		// Generate Unique identifier
		mId = UUID.randomUUID();
		mDate = new Date();
		mScore = Score.get();
		mLevel = Level.get();
		mTimeElapsed = Time.getTimeElapsed();
		mTimeRemaining = Time.getTimeRemaining();
		mCurrentAnswer = CurrentAnswer.get();
	}
	
	public GameStat(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		mDate = new Date(json.getLong(JSON_DATE));
		mScore = json.getInt(JSON_SCORE);
		mScoreRank = json.getInt(JSON_SCORE_RANK);
		mLevel = json.getInt(JSON_LEVEL);
		mLevelRank = json.getInt(JSON_LEVEL_RANK);
		mTimeElapsed = json.getLong(JSON_TIME_ELAPSED);
		mTimeRemaining = json.getLong(JSON_TIME_REMAINING);
		mCurrentAnswer = json.getInt(JSON_CURRENT_ANSWER);
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_DATE, mDate.getTime());
		json.put(JSON_SCORE, mScore);
		json.put(JSON_SCORE_RANK, mScoreRank);
		json.put(JSON_LEVEL, mLevel);
		json.put(JSON_LEVEL_RANK, mLevelRank);
		json.put(JSON_TIME_ELAPSED, mTimeElapsed);
		json.put(JSON_TIME_REMAINING, mTimeRemaining);
		json.put(JSON_CURRENT_ANSWER, mCurrentAnswer);
		return json;
	}
	
	public UUID getmId() {
		return mId;
	}

	public int getmScore() {
		return mScore;
	}

	public int getmLevel() {
		return mLevel;
	}

	public int getmScoreRank() {
		return mScoreRank;
	}

	public void setmScoreRank(int mScoreRank) {
		this.mScoreRank = mScoreRank;
	}

	public int getmLevelRank() {
		return mLevelRank;
	}

	public void setmLevelRank(int mLevelRank) {
		this.mLevelRank = mLevelRank;
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
