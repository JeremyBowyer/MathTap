package com.jeremybowyer.mathtap;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private int mTotalPoints = 0;
    private int mHitPoints = 3;
    private ArrayList<Button> mButtons = new ArrayList<Button>();
    private ArrayList<Button> mWrongButtons;
    private Equation mEquation;

    @BindView(R.id.equationView) TextView mEquationView;

    @BindView(R.id.answer1) Button mAnswer1;
    @BindView(R.id.answer2) Button mAnswer2;
    @BindView(R.id.answer3) Button mAnswer3;
    @BindView(R.id.answer4) Button mAnswer4;
    @BindView(R.id.answer5) Button mAnswer5;
    @BindView(R.id.answer6) Button mAnswer6;
    @BindView(R.id.answer7) Button mAnswer7;
    @BindView(R.id.answer8) Button mAnswer8;
    @BindView(R.id.answer9) Button mAnswer9;

    @BindView(R.id.bonusPointsView) TextView mBonusPoints;

    @BindView(R.id.heart1) ImageView mHeart1;
    @BindView(R.id.heart2) ImageView mHeart2;
    @BindView(R.id.heart3) ImageView mHeart3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mEquation = new Equation(1);
        mEquationView.setText(mEquation.getEquation());

        mTotalPoints = 0;
        mHitPoints = 3;

        mButtons.add(mAnswer1);
        mButtons.add(mAnswer2);
        mButtons.add(mAnswer3);
        mButtons.add(mAnswer4);
        mButtons.add(mAnswer5);
        mButtons.add(mAnswer6);
        mButtons.add(mAnswer7);
        mButtons.add(mAnswer8);
        mButtons.add(mAnswer9);
        for (Button button: mButtons) {
            button.setOnClickListener(guessButton);
        }
        mWrongButtons = setButtons(mButtons, mEquation);
        startClock.start();
        Collections.shuffle(mWrongButtons);
        removeButtonsClock.start();

    }

    private ArrayList<Button> setButtons(ArrayList<Button> buttons, Equation equation) {
        // Create copy of button ArrayList
        ArrayList<Button> buttonsClone = (ArrayList<Button>) buttons.clone();

        // Set correct answer to random button
        Random r = new Random();
        int correctButtonIndex = r.nextInt(buttonsClone.size());
        Button correctButton = buttonsClone.remove(correctButtonIndex);
        correctButton.setText(equation.getAnswer());

        // Set wrong answers to all other buttons
        for(Button button: buttonsClone) {
            button.setText(equation.getWrongAnswer());
        }
        return buttonsClone;
    }

    private void wrongGuess() {
        switch (mHitPoints) {
            case 1:
                mHeart3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                mHeart2.setVisibility(View.INVISIBLE);
                break;
            case 3:
                mHeart1.setVisibility(View.INVISIBLE);
                break;
        }
        mHitPoints--;
    }

    CountDownTimer removeButtonsClock = new CountDownTimer(16100, 2000) {
        boolean firstTick = true;
        @Override
        public void onTick(long millisUntilFinished) {
            if (mWrongButtons.size() == 0) {
                cancel();
            } else if (!firstTick) {
                Button button = mWrongButtons.remove(0);
                button.setVisibility(View.INVISIBLE);
            }
            firstTick = false;
        }

        @Override
        public void onFinish() {

        }
    };

    CountDownTimer startClock = new CountDownTimer(16000, 100) {

        public void onTick(long millisUntilFinished) {
            mBonusPoints.setText("+" + Math.round((millisUntilFinished)) / 16);
        }

        public void onFinish() {
            mBonusPoints.setText("+0"); // In case rounding results in a number > 0
            endGame();
        }

    };

    View.OnClickListener guessButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            String answer = button.getText().toString();

            if(mEquation.isAnswer(answer)) {
                Log.v(TAG, "Great job, idiot");
                startClock.cancel();
                removeButtonsClock.cancel();
            } else {
                wrongGuess();
            }
        }
    };

    private void endGame() {
        mHeart1.setVisibility(View.INVISIBLE);
        mHeart2.setVisibility(View.INVISIBLE);
        mHeart3.setVisibility(View.INVISIBLE);
    }
}
