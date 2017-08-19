package com.jeremybowyer.mathtap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.jeremybowyer.mathtap.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity {
    public static final String TAG = GameActivity.class.getSimpleName();
    public static final String PREFS_NAME = "HighScores";

    private Equation mEquation;

    private String mPlayerName;
    private int mThemeId;
    private Player player;
    private int mPlayerTopScore;

    private boolean mRunning = true;

    private CountDownTimer mCountdownClock;
    private CountDownTimer mPointsCountdownClock;
    private CountDownTimer mRemoveButtonsClock;
    private CountDownTimer mLoadViewsClock;

    private ArrayList<Button> mButtons = new ArrayList<>();
    private ArrayList<Button> mWrongButtons;
    private ArrayList<Button> mButtonsToRemove;
    private ArrayList<View> mGameViews = new ArrayList<>();

    @BindView(R.id.levelTitleView) TextView mLevelTitleView;

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

        player = new Player(mPlayerName);

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

    @Override
    protected void onStart() {
        super.onStart();
        mRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        clearTimers();
        mRunning = false;
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

        mWrongButtons = (ArrayList<Button>) buttonsClone.clone();
        Collections.shuffle(buttonsClone);
        buttonsClone.add(correctButton);
        return buttonsClone;
    }

    private void wrongGuess(Button button) {
        mButtonsToRemove.remove(button); // ensure button isn't selected during countdown
        removeView(button, false);
        if(takeHit() == 0) {
            endGame();
        };
    }

    private int takeHit() {
        int currentHp = player.takeHit();
        switch (currentHp) {
            case 0:
                mHeart3.setVisibility(View.INVISIBLE);
                break;
            case 1:
                mHeart2.setVisibility(View.INVISIBLE);
                break;
            case 2:
                mHeart1.setVisibility(View.INVISIBLE);
                break;
        }
        return currentHp;
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
                if (mButtonsToRemove.size() <= 2) {
                    final MediaPlayer wrong_sound = MediaPlayer.create(GameActivity.this, R.raw.wrong_answer);
                    wrong_sound.start();
                    if (takeHit() == 0) {
                        endGame();
                    } else {
                        endRound();
                    }
                } else if (millisUntilFinished < 21500) {
                    final Button button = mButtonsToRemove.remove(0);
                    removeView(button, false);
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
//            final MediaPlayer countdown_end_sound = MediaPlayer.create(GameActivity.this, R.raw.countdown_end);


            public void onTick(long millisUntilFinished) {
//                countdown_sound.reset();
//                countdown_end_sound.reset();

                if (millisUntilFinished > 3900) {
                    mCountdownTitleView.setVisibility(View.VISIBLE);
                    mCountdownView.setVisibility(View.VISIBLE);
                }
                if (millisUntilFinished < 2000) {
                    mCountdownTitleView.setVisibility(View.INVISIBLE);
                    mCountdownView.setText("GO!");
//                    countdown_end_sound.start();
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
                player.addSuccess();
                player.addPoints(Integer.parseInt(mBonusPointsView.getText().toString()));
                mTotalPointsView.setText(Integer.toString(player.getPlayerPoints()));
                mBonusPointsView.setText("0");
                endRound();
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
        mLevelTitleView.setText("Level " + player.getLevel());
        mEquationView.setText(mEquation.getEquation());
        mButtonsToRemove = setButtons(mButtons, mEquation);
        mCountdownClock.start();
    }

    private void startNewGame() {
        resetStats(true);
        startRound(1);
    }

    private void endRound() {
        clearTimers();
        for (Button button : mWrongButtons) {
            removeView(button, false);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mRunning) {
                    startRound(player.getLevel());
                }
            }
        }, 2000);
    }

    private void endGame() {
        clearTimers();
        for (Button button : mWrongButtons) {
            removeView(button, false);
        }

        // Update player's top score
        SharedPreferences highscores = this.getSharedPreferences(PREFS_NAME, 0);
        mPlayerTopScore = highscores.getInt(player.getPlayerName(), 0);
        mPlayerTopScore = Math.max(mPlayerTopScore, player.getPlayerPoints());
        highscores.edit().putInt(player.getPlayerName(), mPlayerTopScore).apply();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(GameActivity.this, ScoreScreenActivity.class);
                intent.putExtra("themeid", mThemeId);
                intent.putExtra("playerString", player.getJsonString());
                resetStats(true);
                startActivity(intent);
            }
        }, 2000);
    }

    private void resetStats(boolean gameover) {
        mBonusPointsView.setText("1000");
        if(gameover){
            player.resetStats();
            mTotalPointsView.setText("0");
            mHeart1.setVisibility(View.VISIBLE);
            mHeart2.setVisibility(View.VISIBLE);
            mHeart3.setVisibility(View.VISIBLE);
        }
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