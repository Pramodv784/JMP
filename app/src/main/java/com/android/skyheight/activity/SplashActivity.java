package com.android.skyheight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.android.skyheight.R;
import com.android.skyheight.utils.Prefrence;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    Prefrence youprefrence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        youprefrence=Prefrence.getInstance(this);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
            }
        },3000);
    }
}