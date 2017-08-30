package com.jeremybowyer.mathtap;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HowToPlayActivity extends FragmentActivity {

    private static final int NUM_PAGES = 3;

    private PagerAdapter mPagerAdapter;

    public static final String TAG = HowToPlayActivity.class.getSimpleName();

    @BindView(R.id.goBackButton) Button mBackButton;
    @BindView(R.id.pager) ViewPager mPager;
    private boolean mAutoSwipe = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.HowToTheme);
        setContentView(R.layout.activity_how_to_play);
        ButterKnife.bind(this);

        // Instantiate a ViewPager and a PagerAdapter.
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                mAutoSwipe = false;
            }
        });

        changePages();

    }

    public void changePages() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAutoSwipe) {
                    mPager.setCurrentItem(1);
                    mAutoSwipe = true;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(mAutoSwipe) {
                                mPager.setCurrentItem(2);
                                mAutoSwipe = true;
                            }
                        }
                    }, 5000);
                }
            }
        }, 5000);

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
            switch (position) {
                case 0:
                    return new Rules1Fragment();
                case 1:
                    return new Rules2Fragment();
                case 2:
                    return new Rules3Fragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
