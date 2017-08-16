package com.jeremybowyer.mathtap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeremybowyer.mathtap.model.Player;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreScreenActivity extends AppCompatActivity {

    private String mPlayerString;
    private int mThemeId;

    @BindView(R.id.roundsCompletedView) TextView mSuccessfulGuesses;
    @BindView(R.id.levelReachedView) TextView mLevelReached;
    @BindView(R.id.totalPointsView) TextView mTotalPointsView;
    @BindView(R.id.playAgainButtonView) Button mPlayAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent =  getIntent();
        mThemeId = intent.getIntExtra("themeid", 0);
        mPlayerString = intent.getStringExtra("playerString");
        Gson gS = new Gson();
        Player player = gS.fromJson(mPlayerString, Player.class);

        setTheme(mThemeId);
        setContentView(R.layout.activity_score_screen);
        ButterKnife.bind(this);

        mSuccessfulGuesses.setText(Integer.toString(player.getSuccessfulGuesses()));
        mLevelReached.setText(Integer.toString(player.getLevel()));
        mTotalPointsView.setText(Integer.toString(player.getPlayerPoints()));

        mPlayAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
