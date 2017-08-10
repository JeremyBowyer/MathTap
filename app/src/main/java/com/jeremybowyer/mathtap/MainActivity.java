package com.jeremybowyer.mathtap;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.animation;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private int mTotalPoints = 0;
    private int mHitPoints = 3;
    private ArrayList<Button> mButtons = new ArrayList<>();
    private ArrayList<Button> mWrongButtons;
    private Equation mEquation;
    private Animation mWrongButtonAnim;

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

    @BindView(R.id.totalPointsView) TextView mTotalPointsView;
    @BindView(R.id.bonusPointsView) TextView mBonusPointsView;

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

        mWrongButtonAnim = AnimationUtils.loadAnimation(this, R.anim.wrong_button);
        mWrongButtonAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(TAG, "It worked.");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

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

    private void wrongGuess(Button button) {
        button.startAnimation(AnimationUtils.loadAnimation(this, R.anim.wrong_button));
        switch (mHitPoints) {
            case 1:
                mHeart3.startAnimation(AnimationUtils.loadAnimation(this, R.anim.wrong_button));
                break;
            case 2:
                mHeart2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.wrong_button));
                break;
            case 3:
                mHeart1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.wrong_button));
                break;
        }
        mHitPoints--;
    }

    CountDownTimer removeButtonsClock = new CountDownTimer(19000, 2000) {
        boolean firstTick = true;
        @Override
        public void onTick(long millisUntilFinished) {
            if (mWrongButtons.size() == 0) {
                cancel();
            } else if (!firstTick) {
                final Button button = mWrongButtons.remove(0);
                button.startAnimation(mWrongButtonAnim);
            }
            firstTick = false;
        }

        @Override
        public void onFinish() {

        }
    };

    CountDownTimer startClock = new CountDownTimer(18000, 100) {

        public void onTick(long millisUntilFinished) {
            mBonusPointsView.setText("" + Math.round((millisUntilFinished)) / 18);
        }

        public void onFinish() {
            mBonusPointsView.setText("0"); // In case rounding results in a number > 0
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
                mTotalPoints += Integer.parseInt(mBonusPointsView.getText().toString());
                mTotalPointsView.setText(Integer.toString(mTotalPoints));
                mBonusPointsView.setText("0");
                startClock.cancel();
                removeButtonsClock.cancel();
            } else {
                wrongGuess(button);
            }
        }
    };

    private void endGame() {
        mHeart1.setVisibility(View.INVISIBLE);
        mHeart2.setVisibility(View.INVISIBLE);
        mHeart3.setVisibility(View.INVISIBLE);
    }
}
