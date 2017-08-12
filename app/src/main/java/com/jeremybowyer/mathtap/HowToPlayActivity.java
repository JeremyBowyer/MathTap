package com.jeremybowyer.mathtap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HowToPlayActivity extends AppCompatActivity {

    public static final String TAG = HowToPlayActivity.class.getSimpleName();

    private String mPlayerName;

    @BindView(R.id.goBackButton) Button mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
        ButterKnife.bind(this);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HowToPlayActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
