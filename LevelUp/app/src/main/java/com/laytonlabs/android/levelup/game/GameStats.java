package com.laytonlabs.android.levelup.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class GameStats {

	private static final String TAG = "GameStats";
	private static final String FILENAME = "GameStats.json";
	
	private static GameStats sGameStats;
	private Context mAppContext;
	
	private ArrayList<GameStat> mGameStats;
	private GameStat latestGameStat;
	private LevelUpJSONSerializer mSerializer;
	
	private GameStats(Context appContext) {
		mAppContext = appContext;
		mSerializer = new LevelUpJSONSerializer(mAppContext, FILENAME);
		
		try {
			mGameStats = mSerializer.loadGameStats();
		} catch (Exception e) {
			mGameStats = new ArrayList<GameStat>();
			Log.e(TAG, "Error loading GameStats: ", e);
		}
		//this.printArray();
	}
	
	public static GameStats get(Context c) {
		if (sGameStats == null) {
			sGameStats = new GameStats(c.getApplicationContext());
		}
		return sGameStats;
	}

	public void addGameStat(GameStat gs) {
		setLatestGameStat(gs);
		mGameStats.add(gs);
		updateRankings();
	}
	
	public void deleteGameStat(GameStat gs) {
		mGameStats.remove(gs);
		updateRankings();
	}
	
	private void updateRankings() {
		updateScoreRank();
		updateLevelRank();
	}
	
	private void updateScoreRank() {
        mGameStats = sortByScore(mGameStats);

        //Update the ranks now that its sorted.
		int newRank = 0;
		for (int i = 0; i < mGameStats.size(); i++) {
			newRank = i+1;
			if (mGameStats.get(i).getmScoreRank() != newRank) {
				mGameStats.get(i).setmScoreRank(newRank);
			}
		}
	}

    private ArrayList<GameStat> sortByScore(ArrayList<GameStat> stats) {
        Collections.sort(stats, new Comparator<GameStat>() {
            public int compare(GameStat gs1, GameStat gs2) {
                if (gs1.getmScore() == gs2.getmScore())
                    return 0;
                return gs1.getmScore() > gs2.getmScore() ? -1 : 1;
            }
        });
        return stats;
    }

    private void updateLevelRank() {
		Collections.sort(mGameStats, new Comparator<GameStat>(){
		     public int compare(GameStat gs1, GameStat gs2){
		         if(gs1.getmLevel() == gs2.getmLevel())
		             return 0;
		         return gs1.getmLevel() > gs2.getmLevel() ? -1 : 1;
		     }
		});
		
		//Update the ranks now that its sorted.
		int newRank = 0;
		for (int i = 0; i < mGameStats.size(); i++) {
			newRank = i+1;
			if (mGameStats.get(i).getmLevelRank() != newRank) {
				mGameStats.get(i).setmLevelRank(newRank);
			}
		}
	}
	
	public boolean saveGameStats() {
		try {
			mSerializer.saveGameStats(mGameStats);
			Log.d(TAG, "GameStats saved to file");
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Error saving GameStats: ", e);
			return false;
		}
	}
	
	public ArrayList<GameStat> getGameStats() {
		return mGameStats;
	}
	
	public GameStat getGameStat(UUID id) {
		for (GameStat gs : mGameStats) {
			if (gs.getmId().equals(id))
				return gs;
		}
		return null;
	}

	public GameStat getLatestGameStat() {
		return latestGameStat;
	}

	public void setLatestGameStat(GameStat latestGameStat) {
		this.latestGameStat = latestGameStat;
	}
	
	public void printArray() {
		Log.d(TAG,"Array");
		for (int i = 0; i < mGameStats.size(); i++) {
			Log.d(TAG,mGameStats.get(i).toString());
		}
	}

    public ArrayList<GameStat> getTopXStatsByScore(int topX) {
        ArrayList<GameStat> stats = sortByScore(mGameStats);

        if (stats.size() < topX) {
            return stats;
        }

        //Make a new ArrayList thats only the size of the stats.
        ArrayList<GameStat> topXStats = new ArrayList<GameStat>();
        for (int i = 0; i < topX; i++) {
            topXStats.add(stats.get(i));
        }
        return topXStats;
    }
}
