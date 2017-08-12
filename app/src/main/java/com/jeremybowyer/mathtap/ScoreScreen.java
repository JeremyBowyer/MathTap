package com.jeremybowyer.mathtap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreScreen extends AppCompatActivity {

    private String mPlayerName;
    private String mTotalPoints;
    private String mRoundsCompleted;

    @BindView(R.id.roundsCompletedView) TextView mRoundsCompletedView;
    @BindView(R.id.totalPointsView) TextView mTotalPointsView;
    @BindView(R.id.playAgainButtonView) Button mPlayAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);
        ButterKnife.bind(this);

        Intent intent =  getIntent();
        mPlayerName = intent.getStringExtra("name");
        mRoundsCompleted = intent.getStringExtra("rounds");
        mTotalPoints = intent.getStringExtra("points");

        mRoundsCompletedView.setText(mRoundsCompleted);
        mTotalPointsView.setText(mTotalPoints);

        mPlayAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
