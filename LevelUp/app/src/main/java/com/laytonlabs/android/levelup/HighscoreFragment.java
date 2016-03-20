package com.laytonlabs.android.levelup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.laytonlabs.android.levelup.game.GameStat;
import com.laytonlabs.android.levelup.game.GameStats;

import java.util.ArrayList;

/**
 * Created by matthewlayton on 17/03/2016.
 */
public class HighscoreFragment extends ListFragment {

    private Button mBackButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highscore, container, false);

        mBackButton = (Button)view.findViewById(R.id.highscore_back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SplashScreenActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HighscoreAdapter adapter = new HighscoreAdapter(GameStats.get(getActivity()).getTopXStatsByScore(99));
        setListAdapter(adapter);
    }

    private class HighscoreAdapter extends ArrayAdapter<GameStat> {
        public HighscoreAdapter(ArrayList<GameStat> gameStats) {
            super(getActivity(), 0, gameStats);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //If we wern't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_highscore,  null);
            }

            //Configure the view for this GameStat
            GameStat g = getItem(position);

            TextView value = (TextView)convertView.findViewById(R.id.highscore_li_value);
            value.setText("" + g.getmScore());
            value.setTextColor(ContextCompat.getColor(getContext(), R.color.purple));
            TextView rank = (TextView)convertView.findViewById(R.id.highscore_li_rank);
            rank.setText("" + g.getmScoreRank());
            TextView date = (TextView)convertView.findViewById(R.id.highscore_li_date);
            date.setText(g.getmDateFriendly());
            TextView alt_value = (TextView)convertView.findViewById(R.id.highscore_li_alt_value);
            alt_value.setText("" + g.getmLevel());
            alt_value.setTextColor(ContextCompat.getColor(getContext(), R.color.light_blue));

            return convertView;
        }
    }
}
