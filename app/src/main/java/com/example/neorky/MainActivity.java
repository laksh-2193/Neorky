package com.example.neorky;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;

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
        askpermission();


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
    void askpermission(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                TastyToast.makeText(getApplicationContext(),"Location Permission Accessible",TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                TastyToast.makeText(getApplicationContext(),"Permission Required to run the app",TastyToast.LENGTH_LONG,TastyToast.WARNING).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                askpermission();

            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();

    }

}
