package com.jeremybowyer.mathtap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private String mPlayerName;

    @BindView(R.id.howToPlayView) TextView mHowToPlayView;
    @BindView(R.id.playButtonView) TextView mPlayButtonView;
    @BindView(R.id.nameView) TextView mNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPlayerName = mNameView.getText().toString();
        mPlayButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(mPlayerName);
            }
        });
        mHowToPlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HowToPlayActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startGame(String name) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }
}
