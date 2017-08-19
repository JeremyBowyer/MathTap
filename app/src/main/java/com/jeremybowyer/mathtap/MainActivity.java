package com.jeremybowyer.mathtap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jeremybowyer.mathtap.model.ThemeWheel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private String mPlayerName;
    private ThemeWheel mThemeWheel = new ThemeWheel();
    private int mThemeId = mThemeWheel.getThemeId();

    @BindView(R.id.howToPlayView) TextView mHowToPlayView;
    @BindView(R.id.playButtonView) TextView mPlayButtonView;
    @BindView(R.id.nameView) EditText mNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(mThemeId);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPlayButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerName = mNameView.getText().toString();
                startGame(mPlayerName, mThemeId);
            }
        });
        mHowToPlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewRules();
            }
        });
    }

    private void viewRules() {
        Intent intent = new Intent(MainActivity.this, HowToPlayActivity.class);
        startActivity(intent);
    }

    private void startGame(String name, int themeId) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("themeid", themeId);
        startActivity(intent);
    }
}
