package com.example.neorky;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class My_Requests extends AppCompatActivity {

    DatabaseReference reff;

    private FirebaseAuth firebaseAuth;
    Dialog dialog;
    ListView Mylistview;
    String sname, sphone, sstate, sdistrict, sprofession, snote, psint;
    ArrayList<String> myarraylist = new ArrayList<>();
    String profession_sent;
    TextView tr;
    String value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__requests);

        Mylistview = findViewById(R.id.recyclerview);


        firebaseAuth = firebaseAuth.getInstance();
        final ArrayAdapter<String> myarrayadapter = new ArrayAdapter<String>(My_Requests.this,android.R.layout.simple_list_item_1,myarraylist){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if(position%2==0){
                    view.setBackgroundColor(Color.parseColor("#F5BE2A"));
                }
                else{
                    view.setBackgroundColor(Color.parseColor("#C0CA33"));
                }
                return view;
            }

        };

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Requests").child("Show_My_Request");
        Mylistview.setAdapter(myarrayadapter);
        reff.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                value = snapshot.getValue(String.class)+"\n";
                myarraylist.add(value);
                myarrayadapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                value = snapshot.getValue(String.class)+"\n";
                myarraylist.remove(value);
                myarrayadapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                value = myarrayadapter.getItem(position)+"\n";
                try{
                    customdialog();

                }
                catch (Exception e){
                    TastyToast.makeText(getApplicationContext(),e.getMessage().toString(),TastyToast.LENGTH_LONG,TastyToast.WARNING).show();
                }






            }
        });







            fetchData();


        networkcheck();



    }
    void fetchData(){
        try{
            reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Profile");
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    try{
                        String name1 = snapshot.child("name").getValue().toString();
                        sname = name1;
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
                        TastyToast.makeText(getApplicationContext(),"Please update your profile correctly",TastyToast.LENGTH_LONG,TastyToast.WARNING).show();
                        startActivity(new Intent(getApplicationContext(),Profile_Page.class));
                        finish();

                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"No data exists",Toast.LENGTH_SHORT).show();

        }

    }

    void customdialog(){
        try{
            int ppos = value.indexOf("-");
            profession_sent = value.substring(0,ppos).trim();
            psint = profession_sent;
            dialog = new Dialog(My_Requests.this );
            dialog.setContentView(R.layout.custom_user_req);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Requests").child(profession_sent.toUpperCase()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String note2 = snapshot.child("note").getValue().toString();
                        String dnt22 = snapshot.child("datentime").getValue().toString();
                        String phnno2 = snapshot.child("phone_number").getValue().toString();
                        String budget2 = snapshot.child("budget").getValue().toString();
                        String area2 = snapshot.child("area").getValue().toString();
                        TextView dname = dialog.findViewById(R.id.name);
                        dname.setText(profession_sent.toUpperCase());
                        TextView dnote = dialog.findViewById(R.id.note);
                        dnote.setText(note2);
                        TextView ddnt = dialog.findViewById(R.id.time);
                        ddnt.setText(dnt22);
                        TextView dphn = dialog.findViewById(R.id.phone);
                        dphn.setText(phnno2);
                        TextView dbudget = dialog.findViewById(R.id.budget);
                        dbudget.setText(budget2);
                        TextView darea = dialog.findViewById(R.id.area);
                        darea.setText(area2);
                        dialog.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(My_Requests.this);
                                builder.setMessage("Deleting this request will not let you be in contact with the concerned person")
                                        .setTitle("Delete this request??")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                try{


                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Requests").child("Show_My_Request").child(profession_sent.toUpperCase()).setValue(null);
                                                    FirebaseDatabase.getInstance().getReference().child("Services").child(sstate).child(sdistrict).child(profession_sent.toUpperCase()).child(firebaseAuth.getUid()).setValue(null);
                                                    FirebaseDatabase.getInstance().getReference().child("Services").child(sstate).child(sdistrict).child(profession_sent.toUpperCase()).child("Message").child(firebaseAuth.getUid()).setValue(null);




//                                    startActivity(new Intent(getApplicationContext(),Dashboard.class));
//                                    overridePendingTransition(0,0);
                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Requests").child(profession_sent.toUpperCase()).setValue(null);
                                                    startActivity(new Intent(My_Requests.this,Dashboard.class));
                                                    overridePendingTransition(0,0);
                                                    TastyToast.makeText(getApplicationContext(),"Request deleted",TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();
                                                    onBackPressed();


                                                }
                                                catch (Exception e){

                                                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                                                }

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


                    }






                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });
            dialog.show();


        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();

        }

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
