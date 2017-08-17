package com.jeremybowyer.mathtap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeremybowyer.mathtap.HighscoresFragment;
import com.jeremybowyer.mathtap.model.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreScreenActivity extends FragmentActivity {

    private static final int NUM_PAGES = 2;
    public static final String TAG = ScoreScreenActivity.class.getSimpleName();

    private PagerAdapter mPagerAdapter;

    private String mPlayerString;
    private int mThemeId;

    @BindView(R.id.playAgainButtonView) Button mPlayAgainButton;
    @BindView(R.id.pager)
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent =  getIntent();
        mThemeId = intent.getIntExtra("themeid", 0);
        setTheme(mThemeId);
        setContentView(R.layout.activity_score_screen);
        ButterKnife.bind(this);

        // Instantiate a ViewPager and a PagerAdapter.
        mPagerAdapter = new ScoreScreenActivity.ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mPlayAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // Catch data from previous activity
            Intent intent =  getIntent();
            mPlayerString = intent.getStringExtra("playerString");

            // Store in bundle
            Bundle bundle = new Bundle();
            bundle.putString("playerString", mPlayerString);

            switch (position) {
                case 0:
                    PlayerScoresFragment playerfrag = new PlayerScoresFragment();

                    // Send bundle to player scores fragment
                    playerfrag.setArguments(bundle);
                    return playerfrag;
                case 1:
                    HighscoresFragment highscorefrag = new HighscoresFragment();
                    // Send bundle to player scores fragment
                    highscorefrag.setArguments(bundle);
                    return highscorefrag;
            }

            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
