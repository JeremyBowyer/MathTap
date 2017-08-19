package com.jeremybowyer.mathtap;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeremybowyer.mathtap.model.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class HighscoresFragment extends Fragment {

    public static final String TAG = HighscoresFragment.class.getSimpleName();
    public static final String PREFS_NAME = "HighScores";
    private HashMap<String, Integer> mHighScores;

    TextView mName1;
    TextView mScore1;
    TextView mName2;
    TextView mScore2;
    TextView mName3;
    TextView mScore3;
    TextView mName4;
    TextView mScore4;
    TextView mName5;
    TextView mScore5;

    TextView mPlayerScore;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_highscores, container, false);

        mName1 = (TextView) rootView.findViewById(R.id.topNameView1);
        mScore1 = (TextView) rootView.findViewById(R.id.topScoreView1);

        mName2 = (TextView) rootView.findViewById(R.id.topNameView2);
        mScore2 = (TextView) rootView.findViewById(R.id.topScoreView2);

        mName3 = (TextView) rootView.findViewById(R.id.topNameView3);
        mScore3 = (TextView) rootView.findViewById(R.id.topScoreView3);

        mName4 = (TextView) rootView.findViewById(R.id.topNameView4);
        mScore4 = (TextView) rootView.findViewById(R.id.topScoreView4);

        mName5 = (TextView) rootView.findViewById(R.id.topNameView5);
        mScore5 = (TextView) rootView.findViewById(R.id.topScoreView5);

        mPlayerScore = (TextView) rootView.findViewById(R.id.yourScoreView);

        // Get high scores
        SharedPreferences highscores = this.getActivity().getSharedPreferences(PREFS_NAME, 0);
        mHighScores = (HashMap<String, Integer>) highscores.getAll();
        Set<Map.Entry<String,Integer>> scoresEntries = mHighScores.entrySet();
        List<Map.Entry<String,Integer>> scoresList = new LinkedList<Map.Entry<String,Integer>>(scoresEntries);
        Collections.sort(scoresList, new Comparator<Map.Entry<String,Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> ele1, Map.Entry<String, Integer> ele2) {
                return ele1.getValue().compareTo(ele2.getValue());
            }
        });

        try {
            mName1.setText(scoresList.get(scoresList.size() - 1).getKey());
            mScore1.setText(scoresList.get(scoresList.size() - 1).getValue().toString());

            mName2.setText(scoresList.get(scoresList.size() - 2).getKey());
            mScore2.setText(scoresList.get(scoresList.size() - 2).getValue().toString());

            mName3.setText(scoresList.get(scoresList.size() - 3).getKey());
            mScore3.setText(scoresList.get(scoresList.size() - 3).getValue().toString());

            mName4.setText(scoresList.get(scoresList.size() - 4).getKey());
            mScore4.setText(scoresList.get(scoresList.size() - 4).getValue().toString());

            mName5.setText(scoresList.get(scoresList.size() - 5).getKey());
            mScore5.setText(scoresList.get(scoresList.size() - 5).getValue().toString());
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, e.getMessage());
        }

        // Catch data from score screen activity
        String playerString = getArguments().getString("playerString");
        // Parse into Player object
        Gson gS = new Gson();
        Player player = gS.fromJson(playerString, Player.class);

        mPlayerScore.setText(Integer.toString(player.getPlayerPoints()));

        return rootView;
    }
}
