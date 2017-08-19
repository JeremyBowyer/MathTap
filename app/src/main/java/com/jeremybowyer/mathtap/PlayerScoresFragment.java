package com.jeremybowyer.mathtap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeremybowyer.mathtap.model.Player;

public class PlayerScoresFragment extends Fragment {

    public static final String TAG = PlayerScoresFragment.class.getSimpleName();

    TextView mSuccessfulGuesses;
    TextView mLevelReached;
    TextView mTotalPointsView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_player_scores, container, false);

        mSuccessfulGuesses = (TextView) rootView.findViewById(R.id.roundsCompletedView);
        mLevelReached = (TextView) rootView.findViewById(R.id.levelReachedView);
        mTotalPointsView = (TextView) rootView.findViewById(R.id.totalPointsView);

        // Catch data from score screen activity
        String playerString = getArguments().getString("playerString");
        // Parse into Player object
        Gson gS = new Gson();
        Player player = gS.fromJson(playerString, Player.class);
        mSuccessfulGuesses.setText(Integer.toString(player.getSuccessfulGuesses()));
        mLevelReached.setText(Integer.toString(player.getLevel()));
        mTotalPointsView.setText(Integer.toString(player.getPlayerPoints()));

        return rootView;
    }
}
