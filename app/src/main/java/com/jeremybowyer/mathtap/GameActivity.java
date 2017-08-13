package com.jeremybowyer.mathtap;

import android.content.Intent;
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

public class GameActivity extends AppCompatActivity {
    public static final String TAG = GameActivity.class.getSimpleName();

    private int mTotalPoints = 0;
    private int mHitPoints = 3;
    private ArrayList<Button> mButtons = new ArrayList<>();
    private ArrayList<Button> mButtonsToRemove;
    private ArrayList<View> mGameViews = new ArrayList<>();
    private Equation mEquation;
    private String mPlayerName;
    private int mRoundsCompleted = 0;

    @BindView(R.id.equationView) TextView mEquationView;

    @BindView(R.id.countdownView) TextView mCountdownView;

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
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        Intent intent =  getIntent();
        mPlayerName = intent.getStringExtra("name");

        mButtons.add(mAnswer1);
        mButtons.add(mAnswer2);
        mButtons.add(mAnswer3);
        mButtons.add(mAnswer4);
        mButtons.add(mAnswer5);
        mButtons.add(mAnswer6);
        mButtons.add(mAnswer7);
        mButtons.add(mAnswer8);
        mButtons.add(mAnswer9);

        startNewGame();

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

        Collections.shuffle(buttonsClone);
        buttonsClone.add(correctButton);
        return buttonsClone;
    }

    private void wrongGuess(Button button) {
        mButtonsToRemove.remove(button); // ensure button isn't selected during countdown
        removeView(button);
        takeHit();
    }

    private void takeHit() {
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
        if (mHitPoints == 0) {
            endGame();
        }
    }

    private void removeView(View view) {
        Animation removeViewAnim = getViewAnimation(view, R.anim.view_disappear, true);
        view.startAnimation(removeViewAnim);
    }

    CountDownTimer removeButtonsClock = new CountDownTimer(22000, 2000) {

        @Override
        public void onTick(long millisUntilFinished) {
            if (mButtonsToRemove.size() == 1) {
                nextRound();
            } else if(millisUntilFinished < 21500){
                final Button button = mButtonsToRemove.remove(0);
                removeView(button);
                if (mButtonsToRemove.size() == 1) {
                    takeHit();
                }
            }

        }

        @Override
        public void onFinish() {
        }
    };

    CountDownTimer pointsCountdownClock = new CountDownTimer(16000, 100) {

        public void onTick(long millisUntilFinished) {
            mBonusPointsView.setText("" + Math.round((millisUntilFinished)) / 16);
        }

        public void onFinish() {
            mBonusPointsView.setText("0");
        }

    };

    CountDownTimer loadViewsClock = new CountDownTimer(1100, 100) {

        public void onTick(long millisUntilFinished) {
            if(mGameViews.size() > 0) {
                View view = mGameViews.remove(0);
//                Animation loadViewAnim = getViewAnimation(view, R.anim.view_appear, false);
//                view.startAnimation(loadViewAnim);
                view.setVisibility(View.VISIBLE);
                view.animate().alpha(1);
                view.animate().scaleX(1);
                view.animate().scaleY(1);
            }
        }

        public void onFinish() {
            pointsCountdownClock.start();
            removeButtonsClock.start();
        }

    };

    CountDownTimer countdownClock = new CountDownTimer(4000, 1000) {

        public void onTick(long millisUntilFinished) {
            if(millisUntilFinished > 3900){
                mCountdownView.setVisibility(View.VISIBLE);
            }

            mCountdownView.setText("" + Math.round(millisUntilFinished / 1000));
        }

        public void onFinish() {
            mCountdownView.setVisibility(View.INVISIBLE);
            mCountdownView.setText("4");
            loadViewsClock.start();
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
                nextRound();
            } else {
                wrongGuess(button);
            }
        }
    };

    private Animation getViewAnimation(View view, int animid, boolean hide) {

        Animation viewAnim;

        if (hide) {
            AnimationListenerHide animListener = new AnimationListenerHide();
            animListener.setView(view);
            viewAnim = AnimationUtils.loadAnimation(this, animid);
            viewAnim.setAnimationListener(animListener);
        } else {
            AnimationListenerShow animListener = new AnimationListenerShow();
            animListener.setView(view);
            viewAnim = AnimationUtils.loadAnimation(this, animid);
            viewAnim.setAnimationListener(animListener);
        }

        return viewAnim;

    }

    private Animation getHeartAnimation(ImageView heart) {
        AnimationListenerHide heartListener = new AnimationListenerHide();
        heartListener.setView(heart);
        Animation removeHeartAnim = AnimationUtils.loadAnimation(this, R.anim.remove_heart);
        removeHeartAnim.setAnimationListener(heartListener);
        return removeHeartAnim;
    }

    private void startRound(int level) {
        mGameViews.clear();
        mGameViews.add(mEquationView);
        for (Button button: mButtons) {
            button.setOnClickListener(guessButton);
            mGameViews.add(button);
        }
        resetStats(false);
        clearTimers();
        hideViews();
        mEquation = new Equation(level);
        mEquationView.setText(mEquation.getEquation());
        mButtonsToRemove = setButtons(mButtons, mEquation);
        countdownClock.start();
    }

    private void resetStats(boolean gameover) {
        mBonusPointsView.setText("1000");
        if(gameover){
            mTotalPoints = 0;
            mTotalPointsView.setText("0");
            mHitPoints = 3;
            mRoundsCompleted = 0;
            mHeart1.setVisibility(View.VISIBLE);
            mHeart2.setVisibility(View.VISIBLE);
            mHeart3.setVisibility(View.VISIBLE);
        }
    }

    private void startNewGame() {
        resetStats(true);
        startRound(1);
    }

    private void nextRound() {
        mRoundsCompleted++;
        startRound(1); // todo: change this to load level based on score.
    }

    private void endGame() {
        clearTimers();
        hideViews();
        Intent intent = new Intent(this, ScoreScreen.class);
        intent.putExtra("name", mPlayerName);
        intent.putExtra("points", Integer.toString(mTotalPoints));
        intent.putExtra("rounds", Integer.toString(mRoundsCompleted));
        resetStats(true);
        startActivity(intent);
    }

    private void clearTimers() {
        countdownClock.cancel();
        loadViewsClock.cancel();
        pointsCountdownClock.cancel();
        removeButtonsClock.cancel();
    }

    private void hideViews() {
        for(View view: mGameViews) {
            removeView(view);
        }
    }
}