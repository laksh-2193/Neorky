package com.example.neorky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.spec.ECField;

public class Dashboard extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    DatabaseReference reff;
    TextView name;
    TextView id;
    Dialog dialog;
    public static final String version = "1.4.7";
    String district_b, profession_b, state_b;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        firebaseAuth = FirebaseAuth.getInstance();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        final FirebaseUser currentuser = firebaseAuth.getCurrentUser();
        if(currentuser==null){
            Intent intent = new Intent(Dashboard.this,Phonenumber_activity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();

        }
        else{
            account_enabled();
            fetchdata();
            mainmenu();
            business_account();
            loader();
            networkcheck();
            allloggedin();
            versioncheck();


        }


        name = findViewById(R.id.username);
        id = findViewById(R.id.userid);
        id.setText("ID : "+firebaseAuth.getUid());
        findViewById(R.id.anouncement_bell).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // announcementshow();
            }
        });







    }
        void fetchdata(){
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Profile");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try{
                    String name1 = snapshot.child("name").getValue().toString();
                    name.setText(name1);
                    name.append(".");

                }
                catch (Exception e){
                    startActivity(new Intent(getApplicationContext(),Profile_Page.class));
                    overridePendingTransition(0,0);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    void business_account(){

        findViewById(R.id.business_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("BusinessAccount").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        district_b = snapshot.child("district").getValue().toString();
                        state_b = snapshot.child("state").getValue().toString();
                        profession_b = snapshot.child("profession").getValue().toString();
                        if(profession_b.equalsIgnoreCase("N/A")){
                            Toast.makeText(getApplicationContext(),"Sorry your business account is not registered",Toast.LENGTH_LONG).show();
                            businessDialogShow();
                        }
                        else{
                            Intent intent = new Intent(Dashboard.this,UserBusinessAccount.class);
                            intent.putExtra("District",district_b.toUpperCase());
                            intent.putExtra("State",state_b.toUpperCase());
                            intent.putExtra("Profession",profession_b.toUpperCase());

                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }

    void mainmenu(){
        findViewById(R.id.profile_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Profile_Page.class));
                overridePendingTransition(0,0);

            }
        });
        findViewById(R.id.serivices_intent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Services.class));
                overridePendingTransition(0,0);
            }
        });
        findViewById(R.id.my_requests).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),My_Requests.class));
                overridePendingTransition(0,0);

            }
        });
        findViewById(R.id.my_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),My_Profile.class));
                overridePendingTransition(0,0);

            }
        });

    }

    void account_enabled(){
        try{
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String st = snapshot.child("Account_Enabled").getValue().toString();
                        if(st.equalsIgnoreCase("enabled")){

                        }
                        else{
                            FirebaseAuth.getInstance().signOut();

                            Intent intent = new Intent(Dashboard.this,Phonenumber_activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            Toast.makeText(getApplicationContext(),"Please login again",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        FirebaseAuth.getInstance().signOut();

                        Intent intent = new Intent(Dashboard.this,Phonenumber_activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        Toast.makeText(getApplicationContext(),"Please login again",Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        catch (Exception e){
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(Dashboard.this,Phonenumber_activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(0,0);
            Toast.makeText(getApplicationContext(),"Please login again",Toast.LENGTH_SHORT).show();

        }

    }
    void loader(){
        final ProgressDialog progress = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        progress.setTitle("Fetching Data");
        progress.setMessage("This may take a while depending on your network connectivity.\nMake sure you are connected to network");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);

        //progress.show();
        new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String uname = name.getText().toString();
                if(uname.equalsIgnoreCase("Username")){

                    progress.show();

                }
                else{
                    progress.dismiss();
                }

                if(millisUntilFinished<4000){
                    progress.setTitle("Low Network bandwidth");
                    progress.setMessage("Fetching data is taking longer time than usual. Seems your network is slow");

                }


            }

            @Override
            public void onFinish() {
                progress.setTitle("Low Network bandwidth");
                progress.setMessage("Fetching data is taking longer time than usual. Please restart the application ");
            }
        }.start();






    }
    void networkcheck(){

        new CountDownTimer(1800000000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(null!=networkInfo){



                }
                else{
                    startActivity(new Intent(getApplicationContext(),NetworkNotConnected.class));
                   // overridePendingTransition(0,0);
                    cancel();

                }


            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    void allloggedin(){
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name1 = snapshot.child("AllLoggedIn").getValue().toString();
                if(name1.equalsIgnoreCase("yes")){

                }
                else{
                    FirebaseAuth.getInstance().signOut();

                    Intent intent = new Intent(Dashboard.this,Phonenumber_activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    Toast.makeText(getApplicationContext(),"Please login again",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void versioncheck(){
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String vr = snapshot.child("Version").getValue().toString();
                if(vr.equalsIgnoreCase(version)){

                }
                else{
                    Toast.makeText(getApplicationContext(),"Please update your app",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void businessDialogShow(){
        dialog = new Dialog(Dashboard.this);
        dialog.setContentView(R.layout.buy_business_account);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Button bs = dialog.findViewById(R.id.subscribe_now);
        bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), webview_activity.class);
                String user_register_id = FirebaseAuth.getInstance().getUid();
                intent.putExtra("URL","https://docs.google.com/forms/d/e/1FAIpQLSdTqe_GDrNeCxbB__PYBSNVB0iG8ooEugj3xmI1BDgbvU09CA/viewform?usp=pp_url&entry.392025998="+user_register_id);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
        dialog.show();

    }

    void announcementshow(){
        try{
            FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String ann_txt = snapshot.child("Announcement").getValue().toString();
                    dialog = new Dialog(Dashboard.this);
                    dialog.setContentView(R.layout.announcement_dialog);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    TextView bs = dialog.findViewById(R.id.announcement_txtv);
                    bs.setText(ann_txt);
                    bs.append("\n\nCurrent Version "+version);

                    dialog.show();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        catch(Exception e) {
            dialog = new Dialog(Dashboard.this);
            dialog.setContentView(R.layout.announcement_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView bs = dialog.findViewById(R.id.announcement_txtv);
            bs.setText("No announcements to show");

            dialog.show();

        }


    }

    @Override
    public void onBackPressed() {

        finishAffinity();
        System.exit(0);
    }

}
