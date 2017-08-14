package com.jeremybowyer.mathtap;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremybowyer.mathtap.model.AnimationListenerHide;
import com.jeremybowyer.mathtap.model.AnimationListenerShow;
import com.jeremybowyer.mathtap.model.Equation;

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
    private int mSuccessfulGuesses = 0;
    private int mThemeId;
    private CountDownTimer mCountdownClock;
    private CountDownTimer mPointsCountdownClock;
    private CountDownTimer mRemoveButtonsClock;
    private CountDownTimer mLoadViewsClock;

    public MediaPlayer wrong_sound;
    public MediaPlayer correct_sound;
    public MediaPlayer countdown_sound;
    public MediaPlayer countdown_end_sound;

    @BindView(R.id.equationView) TextView mEquationView;

    @BindView(R.id.countdownView) TextView mCountdownView;
    @BindView(R.id.countdownTitleView) TextView mCountdownTitleView;

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
        Intent intent =  getIntent();
        mPlayerName = intent.getStringExtra("name");
        mThemeId = intent.getIntExtra("themeid", 0);
        setTheme(mThemeId);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        mCountdownClock = getCountdownClock();
        mPointsCountdownClock = getPointsCountdownClock();
        mRemoveButtonsClock = getRemoveButtonsClock();
        mLoadViewsClock = getLoadViewsClock();

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

        // MP and OnTouchListener for wrong buttons
        final MediaPlayer wrong_sound = MediaPlayer.create(GameActivity.this, R.raw.wrong_answer);
        View.OnTouchListener wrongButtonSound = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        wrong_sound.start();
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        };

        // Mp and OnTouchListenber for correct button
        final MediaPlayer correct_sound = MediaPlayer.create(GameActivity.this, R.raw.correct_answer);
        View.OnTouchListener correctButtonSound = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        correct_sound.start();
                        return false; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        return false; // if you want to handle the touch event
                }
                return false;
            }
        };


        for(Button wrongbtn: buttonsClone){
            wrongbtn.setOnTouchListener(wrongButtonSound);
        }

        correctButton.setOnTouchListener(correctButtonSound);

        Collections.shuffle(buttonsClone);
        buttonsClone.add(correctButton);
        return buttonsClone;
    }

    private void wrongGuess(Button button) {
        mButtonsToRemove.remove(button); // ensure button isn't selected during countdown
        removeView(button, false);
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

    private void removeView(View view, boolean instant) {

        if (instant) {
            view.setVisibility(View.INVISIBLE);
            view.setScaleX(0);
            view.setScaleY(0);
            view.setAlpha(0);
        } else{
            Animation removeViewAnim = getViewAnimation(view, R.anim.view_disappear, true);
            view.startAnimation(removeViewAnim);
        }
    }

    public CountDownTimer getRemoveButtonsClock() {
        CountDownTimer removeButtonsClock = new CountDownTimer(22000, 2000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (mButtonsToRemove.size() == 1) {
                    nextRound();
                } else if (millisUntilFinished < 21500) {
                    final Button button = mButtonsToRemove.remove(0);
                    removeView(button, false);
                    if (mButtonsToRemove.size() == 1) {
                        takeHit();
                    }
                }

            }

            @Override
            public void onFinish() {
            }
        };
        return removeButtonsClock;
    }

    public CountDownTimer getPointsCountdownClock() {
        CountDownTimer pointsCountdownClock = new CountDownTimer(16000, 100) {

            public void onTick(long millisUntilFinished) {
                mBonusPointsView.setText("" + Math.round((millisUntilFinished)) / 16);
            }

            public void onFinish() {
                mBonusPointsView.setText("0");
            }

        };
        return pointsCountdownClock;
    }

    public CountDownTimer getLoadViewsClock() {
        CountDownTimer loadViewsClock = new CountDownTimer(1100, 100) {

            public void onTick(long millisUntilFinished) {
                if (mGameViews.size() > 0) {
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
                mPointsCountdownClock.start();
                mRemoveButtonsClock.start();
            }

        };

        return loadViewsClock;
    }

    public CountDownTimer getCountdownClock() {
        CountDownTimer countdownClock = new CountDownTimer(4100, 1000) {
            final MediaPlayer countdown_sound = MediaPlayer.create(GameActivity.this, R.raw.countdown);
            final MediaPlayer countdown_end_sound = MediaPlayer.create(GameActivity.this, R.raw.countdown_end);


            public void onTick(long millisUntilFinished) {
                if(countdown_sound.isPlaying() == true){
                    countdown_sound.stop();
                }
                if(countdown_end_sound.isPlaying() == true){
                    countdown_end_sound.stop();
                }


                Log.v(TAG, Long.toString(millisUntilFinished));
                if (millisUntilFinished > 3900) {
                    mCountdownTitleView.setVisibility(View.VISIBLE);
                    mCountdownView.setVisibility(View.VISIBLE);
                }
                if (millisUntilFinished < 2000) {
                    mCountdownView.setText("GO!");
                    countdown_end_sound.start();
                } else {
                    mCountdownView.setText("" + (int) Math.floor((millisUntilFinished - 1000) / 1000));
                    mCountdownView.startAnimation(AnimationUtils.loadAnimation(GameActivity.this, R.anim.view_fade_out));
                    countdown_sound.start();
                }
            }

            public void onFinish() {
                mCountdownTitleView.setVisibility(View.INVISIBLE);
                mCountdownView.setVisibility(View.INVISIBLE);
                mCountdownView.setText("4");
                mLoadViewsClock.start();
            }

        };

        return countdownClock;
    }

    View.OnClickListener guessButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            String answer = button.getText().toString();

            if(mEquation.isAnswer(answer)) {
                mSuccessfulGuesses++;
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

    private void startRound(int level) {
        mGameViews.clear();
        mGameViews.add(mEquationView);
        for (Button button: mButtons) {
            button.setOnClickListener(guessButton);
            mGameViews.add(button);
        }
        hideViews();
        resetStats(false);
        clearTimers();
        mEquation = new Equation(level);
        mEquationView.setText(mEquation.getEquation());
        mButtonsToRemove = setButtons(mButtons, mEquation);
        mCountdownClock.start();
    }

    private void resetStats(boolean gameover) {
        mBonusPointsView.setText("1000");
        if(gameover){
            mTotalPoints = 0;
            mTotalPointsView.setText("0");
            mHitPoints = 3;
            mSuccessfulGuesses = 0;
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
        startRound(1); // todo: change this to load level based on score.
    }

    private void endGame() {
        clearTimers();
        hideViews();
        Intent intent = new Intent(this, ScoreScreenActivity.class);
        intent.putExtra("name", mPlayerName);
        intent.putExtra("themeid", mThemeId);
        intent.putExtra("points", Integer.toString(mTotalPoints));
        intent.putExtra("rounds", Integer.toString(mSuccessfulGuesses));
        resetStats(true);
        startActivity(intent);
    }

    private void clearTimers() {
        mCountdownClock.cancel();
        mLoadViewsClock.cancel();
        mPointsCountdownClock.cancel();
        mRemoveButtonsClock.cancel();
    }

    private void hideViews() {
        for(View view: mGameViews) {
            removeView(view, true);
        }
    }
}