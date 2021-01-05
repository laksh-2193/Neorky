package com.example.neorky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MainActivity extends AppCompatActivity {
    TextView txtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intentBackgroundService = new Intent(MainActivity.this, FirebaseMessagingService.class);
        startService(intentBackgroundService);
        txtv = findViewById(R.id.version);
        String s = Dashboard.version;
        txtv.setText("\nVersion "+s);


        new CountDownTimer(3000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(MainActivity.this,Dashboard.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();

            }
        }.start();

    }

}
