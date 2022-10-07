package com.example.attendance.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.attendance.R;

public class SplashActivity extends AppCompatActivity {

    private String TAG = "wow123";

    /* renamed from: a */
    private int a = 0;
    Dialog dialog;
    private ImageView logo;
    private LottieAnimationView lottieAnimationView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_splash);
        this.logo = (ImageView) findViewById(R.id.lottie);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        }, 1800);
    }

    public void onBackPressed() {
        if (this.a == 0) {
            this.a = 1;
            Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
        this.a = 0;
    }
}