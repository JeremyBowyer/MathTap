package com.jeremybowyer.mathtap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreScreenActivity extends AppCompatActivity {

    private String mPlayerName;
    private String mTotalPoints;
    private String mRoundsCompleted;
    private int mThemeId;

    @BindView(R.id.roundsCompletedView) TextView mRoundsCompletedView;
    @BindView(R.id.totalPointsView) TextView mTotalPointsView;
    @BindView(R.id.playAgainButtonView) Button mPlayAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent =  getIntent();
        mPlayerName = intent.getStringExtra("name");
        mThemeId = intent.getIntExtra("themeid", 0);
        mRoundsCompleted = intent.getStringExtra("rounds");
        mTotalPoints = intent.getStringExtra("points");

        setTheme(mThemeId);
        setContentView(R.layout.activity_score_screen);
        ButterKnife.bind(this);

        mRoundsCompletedView.setText(mRoundsCompleted);
        mTotalPointsView.setText(mTotalPoints);

        mPlayAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
