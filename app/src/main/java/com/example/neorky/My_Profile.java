package com.example.neorky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class My_Profile extends AppCompatActivity {
    TextView name;
    TextView id;
    private FirebaseAuth firebaseAuth;
    DatabaseReference reff;
    TextView t_req;
    String sname, sphone, sstate, sdistrict, sprofession, snote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__profile);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentuser = firebaseAuth.getCurrentUser();

        name = findViewById(R.id.username);
        id = findViewById(R.id.userid);
        t_req = findViewById(R.id.t_req);
        id.setText("ID : "+firebaseAuth.getUid());
        fetchdata();
        networkcheck();
        findViewById(R.id.edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Profile_Page.class));
                overridePendingTransition(0,0);
                finish();
            }
        });
        findViewById(R.id.buy_acc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), webview_activity.class);
                String user_register_id = FirebaseAuth.getInstance().getUid();
                intent.putExtra("URL","https://docs.google.com/forms/d/e/1FAIpQLSdTqe_GDrNeCxbB__PYBSNVB0iG8ooEugj3xmI1BDgbvU09CA/viewform?usp=pp_url&entry.392025998="+user_register_id);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
        findViewById(R.id.sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(My_Profile.this);
                builder.setMessage("Are you sure that you want to signout.\nSigning out wont delete your data. To delete the data, go to 'My Requests'\nDo you still want to continue?")
                        .setTitle("Signout")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();

                                Intent intent = new Intent(My_Profile.this,Phonenumber_activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                overridePendingTransition(0,0);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
        findViewById(R.id.help_support).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:lakshaykumar2193@gmail.com?subject=Neorky : "+FirebaseAuth.getInstance().getUid()+"&body = "+"<Message>"));
                startActivity(intent);
            }
        });
        findViewById(R.id.profile_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Profile_Page.class));
                overridePendingTransition(0,0);
                finish();
            }
        });
        findViewById(R.id.t_req).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),My_Requests.class));
                overridePendingTransition(0,0);
            }
        });
        findViewById(R.id.serv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Services.class));
                overridePendingTransition(0,0);
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
                    String phone1 = snapshot.child("phone_Number").getValue().toString();
                    sphone = phone1;
                    String state1 = snapshot.child("state").getValue().toString();
                    sstate = state1;
                    String district1 = snapshot.child("district").getValue().toString();
                    sdistrict = district1;
                    sstate = sstate.toUpperCase();
                    sdistrict = sdistrict.toUpperCase();

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
        reff =  FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Requests");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int req = ((int)snapshot.getChildrenCount())-1;
                    t_req.setText(Integer.toString(req));

                }
                else{

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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
}
