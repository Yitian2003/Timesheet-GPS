package com.witlife.timesheet.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.witlife.timesheet.R;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1500;
    TextView tvTitle;
    TextView tvTitle1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle1 = (TextView) findViewById(R.id.tvTitle1);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/lobster_regular.ttf");
        tvTitle.setTypeface(custom_font);
        tvTitle1.setTypeface(custom_font);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
