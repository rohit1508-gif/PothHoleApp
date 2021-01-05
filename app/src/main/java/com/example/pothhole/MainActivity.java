package com.example.pothhole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    protected boolean _active = true;
    protected int _splashTime = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
//                Intent i = new Intent(MainActivity.this, OTPActivity.class);
                Intent i = new Intent(MainActivity.this,SubmitActivity.class);
                startActivity(i);
            }
        }, _splashTime);
    }
}
