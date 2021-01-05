package com.example.neorky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sdsmdg.tastytoast.TastyToast;

import com.xw.repo.BubbleSeekBar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Service_Place_Request extends AppCompatActivity implements LocationListener {
    DatabaseReference reff;
    private FirebaseAuth firebaseAuth;
    String sname, sphone, sstate, sdistrict, sprofession, snote, sbudget, sarea;
    LocationManager locationManager;
    Dialog dialog;
    private BubbleSeekBar seekBar;
    private TextView dialog_price_tag, dialog_okay_btn;
    public static ImageSlider topimgslider;
    public static List<SlideModel> slideModels;


    TextView note_in, area_in, budget_in;
    TextView pname;
    CheckBox checkBox;
    Requests requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__place__request);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        firebaseAuth = FirebaseAuth.getInstance();
        fetchData();
        sprofession = getIntent().getStringExtra("profession_selected").toUpperCase();
        note_in = findViewById(R.id.note);
        budget_in = findViewById(R.id.budget);
        area_in = findViewById(R.id.area);
        checkBox = findViewById(R.id.tnccheckbox);
        pname = findViewById(R.id.profession_name);
        pname.setText(sprofession);
       // priceslider();


        checkenabledsettings();
        getLocation();
        askpermission();
        findViewById(R.id.budget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceslider();

            }
        });
        findViewById(R.id.budget).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                priceslider();
            }
        });


        findViewById(R.id.place_req).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                snote = note_in.getText().toString();
                sbudget = budget_in.getText().toString();
                sarea = area_in.getText().toString();

                if (snote.trim().equalsIgnoreCase("") || (sbudget.equalsIgnoreCase("")) || (sarea.equalsIgnoreCase(""))) {
                    Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();


                } else {
                    if (checkBox.isChecked()) {
                        fetchData();
                        addrequest();

                        TastyToast.makeText(getApplicationContext(),"Request Placed",TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();
                        uidialognext();

                    } else {
                        Toast.makeText(getApplicationContext(), "Please tick the checkbox in bottom", Toast.LENGTH_LONG).show();

                    }

                }

            }
        });
        topimgslider = findViewById(R.id.slide_image);
        changedetailsacctoprefession();
        loader();






    }
    void uidialognext(){
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(Service_Place_Request.this);
        builder.setMessage("Please delete the request once it is completed by Neorky Service man");
        builder.setTitle("Attention");
        builder.setCancelable(false);
        builder.setPositiveButton("Delete now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(),My_Requests.class));
                overridePendingTransition(0,0);
                finish();

            }
        });
        builder.setNegativeButton("New Service", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), Services.class));
                overridePendingTransition(0,0);
                finish();

            }
        });
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();


    }
    void changedetailsacctoprefession(){
        if(sprofession.equalsIgnoreCase("PAINTER")){
            findViewById(R.id.profile_image).setBackgroundResource(R.drawable.painter);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://res.cloudinary.com/twenty20/private_images/t_watermark-criss-cross-10/v1480999028000/photosp/63a61411-2d4c-4f9a-97c2-04062a81e325/stock-photo-wall-paint-painting-teamwork-man-street-art-painter-street-scene-street-photography-63a61411-2d4c-4f9a-97c2-04062a81e325.jpg",""));
            slideModels.add(new SlideModel("https://rp-prod-wordpress-b-content.s3.amazonaws.com/assets/2018/08/20151733/sm-external-wall-painter-4.jpg",""));
            slideModels.add(new SlideModel("https://www.homestratosphere.com/wp-content/uploads/2020/02/ceiling-paint-vs-wall-paint-top-February52020-min.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("CARPENTER")){
            findViewById(R.id.profile_image).setBackgroundResource(R.drawable.carpenter);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://www.thebalancesmb.com/thmb/c0ZhcxWQuXWC91tV8cE73Nqd6Ug=/3863x2173/smart/filters:no_upscale()/young-stylish-cabinet-maker-with--glasses-and-hairstyle--strong--handsome-craftsman-holding-saw-and-wood-blank-at-workplace-944613244-5af9afc2a18d9e003c17040c.jpg",""));
            slideModels.add(new SlideModel("https://www.okanagan.bc.ca/sites/default/files/styles/large_wide/public/2020-07/Carpentry-Apprenticeship.jpg?itok=pJavcHyf",""));
            slideModels.add(new SlideModel("https://www.viu.ca/sites/default/files/styles/full_bg_focal_breakpoints_theme_viu_theme_bg_xxl_landscape_1x/public/pages/steven-remple.jpg?itok=Z5zajRQq&timestamp=1547580652",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("boutique")){
            findViewById(R.id.profile_image).setBackgroundResource(R.drawable.boutique);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://cdn.choosechicago.com/uploads/2019/06/neighborhood-boutiques-1800x900.jpg",""));
            slideModels.add(new SlideModel("https://imgstaticcontent.lbb.in/lbbnew/wp-content/uploads/2017/07/24144741/httpswww.facebook.com-1.jpg",""));
            slideModels.add(new SlideModel("https://charlotteagenda-charlotteagenda.netdna-ssl.com/wp-content/uploads/2018/11/best-womens-boutiques-in-charlotte.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("House maid")){
            findViewById(R.id.profile_image).setBackgroundResource(R.drawable.maid);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://www.mychores.in/Images/Housemaid-in-Mumbai.jpg",""));
            slideModels.add(new SlideModel("https://www.ics-facilitymanagement.com/img/services/House-Maid-Services.jpg",""));
            slideModels.add(new SlideModel("https://profiles.sulekha.com/mstore/43530795/albums/default/thumbnailfull/shutterstock-648398542.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("LABOUR")){
            findViewById(R.id.profile_image).setBackgroundResource(R.drawable.labout);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://blog.ipleaders.in/wp-content/uploads/2020/02/avoiding-fatal-four.jpg",""));
            slideModels.add(new SlideModel("https://images.newindianexpress.com/uploads/user/imagelibrary/2020/4/9/w900X450/CVBCVBHJhs.jpg",""));
            slideModels.add(new SlideModel("https://www.on-sitemag.com/wp-content/uploads/2019/04/workers_construction_concrete_road.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("IT Services")){
            findViewById(R.id.profile_image).setBackgroundResource(R.drawable.it);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://www.fidelus.com/wp-content/uploads/2017/09/managed-it-services-og.jpg",""));
            slideModels.add(new SlideModel("https://www.inceptionnet.com/wp-content/uploads/2019/11/small-business-IT-services.jpeg",""));
            slideModels.add(new SlideModel("https://www.ricoh-me.com/en/media/IT%20Services%20-%20Infrastructure%20-%20Thumbnail%20504%20x%20252_tcm89-32451_w504.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("Rooms")){
            findViewById(R.id.profile_image).setBackgroundResource(R.drawable.rooms);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://cdn.styleblueprint.com/wp-content/uploads/2015/12/SB-ATL-ZookHome-9-e1538165814448.jpg",""));
            slideModels.add(new SlideModel("https://images.oyoroomscdn.com/uploads/hotel_image/81612/large/f0af88ee7aca453a.jpg",""));
            slideModels.add(new SlideModel("https://www.shalomluxuryrooms.gr/assets/img/slider/new/5.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("food")){
            findViewById(R.id.profile_image).setBackgroundResource(R.drawable.food);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://www.blueosa.com/wp-content/uploads/2020/01/the-best-top-10-indian-dishes.jpg",""));
            slideModels.add(new SlideModel("https://c.ndtvimg.com/2020-05/9iuj3h1g_indian-food_625x300_19_May_20.jpg",""));
            slideModels.add(new SlideModel("https://img.etimg.com/thumb/msid-72299767,width-1200,height-900,imgsize-365179,overlay-etpanache/photo.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("Coaching")){
            findViewById(R.id.profile_image).setBackgroundResource(R.drawable.coaching);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://www.indiaeducation.net/imagesvr_ce/9374/maxresdefault.jpg",""));
            slideModels.add(new SlideModel("https://www.rgu.ac.in/uploads/CKImages/images/net-coaching.jpg",""));
            slideModels.add(new SlideModel("https://akm-img-a-in.tosshub.com/indiatoday/images/story/202008/tea_0.jpeg?ndJOhaA2O6_kIEP_RkQU9F3hcjUuT9i9&size=770:433",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("Electrician")){
            findViewById(R.id.profile_image).setBackgroundResource(R.drawable.electrician);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://www.bpmelectric.com/wp-content/uploads/2019/03/Fotolia_170179753_Subscription_Monthly_M.jpg",""));
            slideModels.add(new SlideModel("https://lh3.googleusercontent.com/proxy/X0MAMcGnBXyYk-VSoVZEX9TRLb5kjtlzAjzTmB6Vc4QoYe7tWobaDuXLFtT3fGWusl669aMP8FfAbQCtV-WBQVS8mD_maZfTeQu1gcNm0d-6SRIIMgKmybWWyqF1chYAsKnj9dr1jX16_90ASkV7ZCAECTk",""));
            slideModels.add(new SlideModel("https://blog.herzing.ca/hubfs/electrician-2.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("Counselling")){
            findViewById(R.id.profile_image).setBackgroundResource(R.drawable.counselling);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://s3-ap-south-1.amazonaws.com/blogmindler/bloglive/wp-content/uploads/2019/03/11113602/How-to-Become-a-Counsellor-in-India.png",""));
            slideModels.add(new SlideModel("https://miro.medium.com/max/4000/1*8RU80tmjaY8yLz2howNn-g.jpeg",""));
            slideModels.add(new SlideModel("https://counselingchennai.com/wp-content/uploads/2020/05/Online-Live-Diploma-in-School-Corporate-Family-Counselling-via-Google-Meet.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("Plumber")){
            findViewById(R.id.profile_image).setBackgroundResource(R.drawable.plumber);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://static.independent.co.uk/s3fs-public/thumbnails/image/2019/09/17/16/istock-1013437722.jpg",""));
            slideModels.add(new SlideModel("https://cahillinc.com/wp-content/uploads/sites/8148/2018/10/emergency-plumber-1-2000x800.jpg",""));
            slideModels.add(new SlideModel("https://www.rd.com/wp-content/uploads/2018/10/07-1.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }




    }
    void priceslider(){
        try{
            dialog = new Dialog(Service_Place_Request.this);
            dialog.setContentView(R.layout.price_slider);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            seekBar = dialog.findViewById(R.id.price_bar);
            dialog_price_tag = dialog.findViewById(R.id.price);
            dialog_okay_btn = dialog.findViewById(R.id.ok_btn);
            dialog.setCancelable(true);



            seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(int progress, float progressFloat) {
                    dialog_price_tag.setText("Rs "+Integer.toString(progress)+"/-");
                }

                @Override
                public void getProgressOnActionUp(int progress, float progressFloat) {

                }

                @Override
                public void getProgressOnFinally(int progress, float progressFloat) {

                }
            });

            dialog_okay_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dpt = dialog_price_tag.getText().toString();
                    if(dpt.equalsIgnoreCase("Rs 0/-")){
                        Toast.makeText(getApplicationContext(),"Please select minimum budget",Toast.LENGTH_SHORT).show();

                    }
                    else{
                        budget_in.setText(dpt);
                        dialog.dismiss();

                    }
                }
            });
            dialog.show();

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }





    }



    void fetchData() {
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Profile");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    String name1 = snapshot.child("name").getValue().toString();
                    sname = name1;
                    String phone1 = snapshot.child("phone_Number").getValue().toString();
                    sphone = phone1;
                    String state1 = snapshot.child("state").getValue().toString();
                    sstate = state1;
                    String district1 = snapshot.child("district").getValue().toString();
                    sdistrict = district1;

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Please update your profile correctly", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Profile_Page.class));
                    finish();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    void addrequest() {
        String message_my_req, busss_req;
        requests = new Requests();
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
        requests.setName(sname);
        requests.setPhone_number(sphone);
        sdistrict = sdistrict.toUpperCase();
        sstate = sstate.toUpperCase();
        requests.setDistrict(sdistrict);
        requests.setState(sstate);
        requests.setNote(snote);
        requests.setBudget(sbudget);
        requests.setArea(sarea);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        requests.setDatentime(currentDateandTime);
        message_my_req = "\n" + sprofession + "\n-'" + snote + "'\n" + currentDateandTime;
        busss_req = "\n" + firebaseAuth.getUid() + "\n" + "\n" + sname.toUpperCase() + "\nNote - '" + snote + "'\n" + currentDateandTime + "\n" + sphone;
        requests.setProfession(sprofession);
        reff.child("Requests").child(sprofession).setValue(requests);
        reff.child("Requests").child("Show_My_Request").child(sprofession).setValue(message_my_req);
        FirebaseDatabase.getInstance().getReference().child("Services").child(sstate).child(sdistrict).child(sprofession).child(firebaseAuth.getUid()).setValue(requests);
        FirebaseDatabase.getInstance().getReference().child("Services").child(sstate).child(sdistrict).child(sprofession).child("Message").child(firebaseAuth.getUid()).setValue(busss_req);

    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) Service_Place_Request.this);

        }
        catch (Exception e){
            e.printStackTrace();

        }


    }
    void checkenabledsettings(){
        if((ContextCompat.checkSelfPermission(Service_Place_Request.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)){

            ActivityCompat.requestPermissions(Service_Place_Request.this,new String[]{
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
            try{
                AlertDialog.Builder builder = new AlertDialog.Builder(Service_Place_Request.this);
                builder.setTitle("Enable GPS Service");
                builder.setCancelable(false);
                builder.setMessage("Please enable GPS service to check your location");
                builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
            catch (Exception e){
                TastyToast.makeText(Service_Place_Request.this,"Please enable GPS location service",TastyToast.LENGTH_LONG,TastyToast.WARNING);
            }

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
    void loader(){
        final ProgressDialog progress = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        progress.setTitle("Checking your location");
        progress.setMessage("We are checking in which place you live in so that people can reach you accurately");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);

        //progress.show();
        new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String uname = area_in.getText().toString().trim();
                if(uname.equalsIgnoreCase("")){

                    progress.show();

                }
                else{
                    progress.dismiss();
                }

                if(millisUntilFinished<6000){
                    progress.setTitle("Please switch on GPS services");
                    progress.setMessage("Fetching data is taking longer time than usual. Please switch on GPS service");


                }


            }

            @Override
            public void onFinish() {

               progress.dismiss();
               if(area_in.getText().toString().trim().equalsIgnoreCase("")){
                   onBackPressed();
                   TastyToast.makeText(Service_Place_Request.this,"Failed to get Location. Please try again",TastyToast.LENGTH_LONG,TastyToast.WARNING);

               }

            }
        }.start();






    }

    @Override
    public void onLocationChanged(Location location) {


        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            area_in.setText(addresses.get(0).getAddressLine(0));

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
