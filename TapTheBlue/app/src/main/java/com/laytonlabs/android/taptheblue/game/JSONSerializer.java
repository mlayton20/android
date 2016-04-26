package com.laytonlabs.android.taptheblue.game;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by matthewlayton on 24/04/2016.
 */
public class JSONSerializer {

    private Context mContext;
    private String mFilename;

    public JSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    public void saveGameStats(ArrayList<GameStat> GameStats) throws JSONException, IOException {
        //Build an array in JSON
        JSONArray array = new JSONArray();
        for (GameStat c : GameStats)
            array.put(c.toJSON());

        writeJSON(array);
    }

    private void writeJSON(JSONArray array) throws FileNotFoundException,
            IOException {
        //Write the file to disk
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public ArrayList<GameStat> loadGameStats() throws IOException, JSONException {
        ArrayList<GameStat> GameStats = new ArrayList<GameStat>();
        JSONArray array = readJSON();

        //Build the array of items from JSONObjects
        for (int i = 0; i < array.length(); i++) {
            GameStats.add(new GameStat(array.getJSONObject(i)));
        }

        return GameStats;
    }

    private JSONArray readJSON() throws IOException, JSONException {
        JSONArray array = new JSONArray();
        BufferedReader reader = null;
        try {
            //Open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //Line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            //Parse the JSON using JSONTokener
            array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
        } catch (FileNotFoundException e) {
            //Ignore this one; it happens when starting fresh
        } finally {
            if (reader != null)
                reader.close();
        }
        return array;
    }
}
