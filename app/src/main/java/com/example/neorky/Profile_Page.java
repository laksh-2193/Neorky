package com.example.neorky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Profile_Page extends AppCompatActivity implements LocationListener {

    EditText phone_txt;
    private FirebaseAuth firebaseAuth;
    DatabaseReference reff;

    Users users;
    String nm_intent = "";
    LocationManager locationManager;
    Button bt;


    EditText name, phone, district, state;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__page);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentuser = firebaseAuth.getCurrentUser();
        if(currentuser==null){
            Intent intent = new Intent(Profile_Page.this,Phonenumber_activity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();

        }
        else{
            //Toast.makeText(Profile_Page.this,firebaseAuth.getUid(),Toast.LENGTH_LONG).show();
            try{


                checkenabledsettings();
                getLocation();
                FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Account_Enabled").setValue("enabled");
                fetchdata();
                loader();
                askpermission();


            }
            catch (Exception e){
                Toast.makeText(Profile_Page.this,"Please fill your details",Toast.LENGTH_SHORT).show();

            }




        }
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone_no);
        district = findViewById(R.id.district);
        bt = findViewById(R.id.verify_btn);
        state = findViewById(R.id.state);
        findViewById(R.id.verify_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senddatatofirebase();

            }
        });
        findViewById(R.id.back_btn_phn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dr = district.getText().toString();
                String st = state.getText().toString();

                if(dr.equalsIgnoreCase("Please wait...") || st.equalsIgnoreCase("Please wait...")|| name.getText().toString().trim().equalsIgnoreCase("") || phone.getText().toString().trim().equalsIgnoreCase("")){
                    Intent refresh = new Intent(Profile_Page.this,Profile_Page.class);
                    refresh.putExtra("name_of_user",name.getText().toString());
                    startActivity(refresh);
                    overridePendingTransition(0,0);
                    Toast.makeText(Profile_Page.this,"Please switch on your location service and Fill in your name", Toast.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(Profile_Page.this,Dashboard.class));
                    overridePendingTransition(0,0);

                }
            }
        });
        if(nm_intent.trim().equalsIgnoreCase("")){

        }
        else{
            Intent intent = getIntent();
            String nm_i = intent.getStringExtra("name_of_user");
            name.setText(nm_i);

        }




    }
    void loader(){
        final ProgressDialog progress = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        progress.setTitle("Please wait..");
        progress.setMessage("Checking your profile data");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        progress.setCancelable(false);

        //progress.show();
        new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String username = name.getText().toString();
                String uname = phone.getText().toString();
                String nm = state.getText().toString();

                if(uname.trim().equalsIgnoreCase("")) {

                    progress.show();

                }
                else{
                    progress.dismiss();
                }
                if(nm.equalsIgnoreCase("Please wait...")){
                    bt.setText("Retrace Location");
                }
                else{
                    bt.setText("Save");
                }


            }

            @Override
            public void onFinish() {
                progress.setTitle("Taking too long");
                progress.setMessage("Fetching data is taking longer time than usual. Please restart the application ");


            }
        }.start();





    }
    void fetchdata(){
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Profile");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try{
                    String phn1 = snapshot.child("phone_Number").getValue().toString();
                    phone.setText(phn1);
                    //phone.setText(phn1);
                    String name1 = snapshot.child("name").getValue().toString();
                    name.setText(name1);
                    String district1 = snapshot.child("district").getValue().toString();
                    district.setText(district1);
                    String state1 = snapshot.child("state").getValue().toString();
                    state.setText(state1);

                }
                catch (Exception e){
                    String name1 = "";
                    name.setText(name1);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    void senddatatofirebase(){
        String dr = district.getText().toString();
        String st = state.getText().toString();

        if(dr.equalsIgnoreCase("Please wait...") || st.equalsIgnoreCase("Please wait...")|| (name.getText().toString().trim().equalsIgnoreCase(""))){
            Intent refresh = new Intent(Profile_Page.this,Profile_Page.class);
            startActivity(refresh);
            overridePendingTransition(0,0);
            Toast.makeText(Profile_Page.this,"Please switch on your location service and Fill in your name", Toast.LENGTH_LONG).show();
        }
        else{
            users = new Users();
            reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
            users.setName(name.getText().toString());
            users.setPhone_Number(phone.getText().toString());
            users.setDistrict(district.getText().toString());
            users.setState(state.getText().toString());
            reff.child("Profile").setValue(users);
            Toast.makeText(Profile_Page.this,"Data Updated",Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Account_Enabled").setValue("enabled");
            startActivity(new Intent(Profile_Page.this,Dashboard.class));
            overridePendingTransition(0,0);
            finish();
        }

    }
    void checkenabledsettings(){
        if((ContextCompat.checkSelfPermission(Profile_Page.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)){

            ActivityCompat.requestPermissions(Profile_Page.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },100);

        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsenabled = false;
        boolean networkenabled = false;
        try{
            gpsenabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            networkenabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(!gpsenabled && !networkenabled){
            new AlertDialog.Builder(getApplicationContext())
                    .setTitle("Enable GPS Service")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                        }
                    }).setNegativeButton("Cancel", null)
                    .show();
        }

    }
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) Profile_Page.this);

        }
        catch (Exception e){
            e.printStackTrace();

        }


    }
    void askpermission(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //TastyToast.makeText(getApplicationContext(),"Permission Granted",TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                TastyToast.makeText(getApplicationContext(),"Permission Required to run the app",TastyToast.LENGTH_LONG,TastyToast.WARNING).show();
                askpermission();

            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();

    }

    @Override
    public void onBackPressed() {
        String dr = district.getText().toString();
        String st = state.getText().toString();

        if(dr.equalsIgnoreCase("Please wait...") || st.equalsIgnoreCase("Please wait...")|| (name.getText().toString().trim().equalsIgnoreCase(""))){
            Intent refresh = new Intent(Profile_Page.this,Profile_Page.class);
            startActivity(refresh);
            overridePendingTransition(0,0);
            Toast.makeText(Profile_Page.this,"Please switch on your location service and Fill in your name", Toast.LENGTH_LONG).show();
        }
        else{
            startActivity(new Intent(Profile_Page.this,Dashboard.class));
            overridePendingTransition(0,0);


        }

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            district.setText(addresses.get(0).getLocality());

            state.setText(addresses.get(0).getAdminArea());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
