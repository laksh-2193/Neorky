package com.example.neorky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;

public class NetworkNotConnected extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_not_connected);
        networkcheck();
    }
    void networkcheck(){

        new CountDownTimer(1800000000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(null!=networkInfo){
                    finish();
                }
                else{


                }


            }

            @Override
            public void onFinish() {

            }
        }.start();
    }


}
