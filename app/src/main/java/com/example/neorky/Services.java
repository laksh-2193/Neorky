package com.example.neorky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.denzcoskun.imageslider.models.SlideModel;

public class Services extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        navigationmenu();
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Services.this,Dashboard.class));
                overridePendingTransition(0,0);
                finish();
            }
        });

    }
    void navigationmenu(){
        findViewById(R.id.painter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Service_Place_Request.class);

                intent.putExtra("profession_selected","PAINTER");
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();

            }
        });
        findViewById(R.id.carpenter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Service_Place_Request.class);
                intent.putExtra("profession_selected","CARPENTER");
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();


            }
        });
        findViewById(R.id.boutique).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Service_Place_Request.class);
                intent.putExtra("profession_selected","boutique");
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();


            }
        });
        findViewById(R.id.house_maid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Service_Place_Request.class);
                intent.putExtra("profession_selected","House maid");
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();


            }
        });
        findViewById(R.id.labour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Service_Place_Request.class);
                intent.putExtra("profession_selected","LABOUR");
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();


            }
        });
        findViewById(R.id.itservices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Service_Place_Request.class);
                intent.putExtra("profession_selected","IT Services");
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();


            }
        });
        findViewById(R.id.rooms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Service_Place_Request.class);
                intent.putExtra("profession_selected","Rooms");
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();


            }
        });
        findViewById(R.id.food).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Service_Place_Request.class);
                intent.putExtra("profession_selected","food");
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();


            }
        });
        findViewById(R.id.coaching).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Service_Place_Request.class);
                intent.putExtra("profession_selected","Coaching");
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();


            }
        });
        findViewById(R.id.electrician).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Service_Place_Request.class);
                intent.putExtra("profession_selected","Electrician");
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();


            }
        });
        findViewById(R.id.counselling).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Service_Place_Request.class);
                intent.putExtra("profession_selected","Counselling");
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();


            }
        });
        findViewById(R.id.plumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Service_Place_Request.class);
                intent.putExtra("profession_selected","Plumber");
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Services.this,Dashboard.class));
        overridePendingTransition(0,0);
        finish();
    }
}
