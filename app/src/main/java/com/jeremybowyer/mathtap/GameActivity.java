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

public class GameActivity extends AppCompatActivity {
    public static final String TAG = GameActivity.class.getSimpleName();

    private int mTotalPoints = 0;
    private int mHitPoints = 3;
    private ArrayList<Button> mButtons = new ArrayList<>();
    private ArrayList<Button> mButtonsToRemove;
    private ArrayList<View> mGameViews = new ArrayList<>();
    private Equation mEquation;

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


        mButtons.add(mAnswer1);
        mButtons.add(mAnswer2);
        mButtons.add(mAnswer3);
        mButtons.add(mAnswer4);
        mButtons.add(mAnswer5);
        mButtons.add(mAnswer6);
        mButtons.add(mAnswer7);
        mButtons.add(mAnswer8);
        mButtons.add(mAnswer9);

        startRound();

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
        Animation wrongButtonAnim = getViewAnimation(button, R.anim.button_disappear, true);
        button.startAnimation(wrongButtonAnim);
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

    CountDownTimer removeButtonsClock = new CountDownTimer(19000, 2000) {

        @Override
        public void onTick(long millisUntilFinished) {
            if (mButtonsToRemove.size() == 0) {
                cancel();
            } else if(millisUntilFinished < 18000){
                final Button button = mButtonsToRemove.remove(0);
                Animation buttonHideAnim = getViewAnimation(button, R.anim.button_disappear, true);
                button.startAnimation(buttonHideAnim);
            }

        }

        @Override
        public void onFinish() {
            mEquationView.setVisibility(View.INVISIBLE);
            endGame();
        }
    };

    CountDownTimer mainGameClock = new CountDownTimer(18000, 100) {

        public void onTick(long millisUntilFinished) {
            mBonusPointsView.setText("" + Math.round((millisUntilFinished)) / 18);
        }

        public void onFinish() {
            mBonusPointsView.setText("0"); // In case rounding results in a number > 0
        }

    };

    CountDownTimer loadViewsClock = new CountDownTimer(1100, 100) {

        public void onTick(long millisUntilFinished) {
            if(mGameViews.size() > 0) {
                View view = mGameViews.remove(0);
                view.setVisibility(View.VISIBLE);
                view.animate().alpha(1);
                view.animate().scaleX(1);
                view.animate().scaleY(1);
            }
        }

        public void onFinish() {
            mainGameClock.start();
            removeButtonsClock.start();
        }

    };

    CountDownTimer countdownClock = new CountDownTimer(3000, 1000) {


        public void onTick(long millisUntilFinished) {
            if(millisUntilFinished > 2900){
                hideViews();
                mCountdownView.setVisibility(View.VISIBLE);
            }
            mCountdownView.setText("" + ( millisUntilFinished + 1000 )/ 1000);
        }

        public void onFinish() {
            mCountdownView.setVisibility(View.INVISIBLE);
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

    private void startRound() {

        mGameViews.add(mEquationView);
        for (Button button: mButtons) {
            button.setOnClickListener(guessButton);
            mGameViews.add(button);
        }
        mEquation = new Equation(1);
        mEquationView.setText(mEquation.getEquation());
        mButtonsToRemove = setButtons(mButtons, mEquation);
        countdownClock.start();
    }

    private void nextRound() {
        clearTimers();
        startRound();
    }

    private void endGame() {
        mTotalPoints = 0;
        mHitPoints = 3;
        mHeart1.setVisibility(View.VISIBLE);
        mHeart2.setVisibility(View.VISIBLE);
        mHeart3.setVisibility(View.VISIBLE);
        clearTimers();
        startRound();
    }

    private void clearTimers() {
        countdownClock.cancel();
        loadViewsClock.cancel();
        mainGameClock.cancel();
        removeButtonsClock.cancel();
    }

    private void hideViews() {
        for(View view: mGameViews) {
            view.setVisibility(View.INVISIBLE);
        }
    }
}