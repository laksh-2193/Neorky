package com.example.neorky;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserBusinessAccount extends AppCompatActivity {
    DatabaseReference reff;



    private FirebaseAuth firebaseAuth;
    public static ImageSlider topimgslider;
    public static List<SlideModel> slideModels;
    ListView Mylistview;
    String sname, sphone, sstate, sdistrict, sprofession, snote;
    ArrayList<String> myarraylist = new ArrayList<>();
    TextView tr;
    Dialog dialog;
    String showmessageindialog;
    String value, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_user_business_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        sstate = getIntent().getStringExtra("State");
        sdistrict = getIntent().getStringExtra("District");
        sprofession = getIntent().getStringExtra("Profession");
        Mylistview = findViewById(R.id.recyclerview);
        topimgslider = findViewById(R.id.slide_image);
        firebaseAuth = firebaseAuth.getInstance();
        final ArrayAdapter<String> myarrayadapter = new ArrayAdapter<String>(UserBusinessAccount.this,android.R.layout.simple_list_item_1,myarraylist){
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


        reff = FirebaseDatabase.getInstance().getReference().child("Services").child(sstate).child(sdistrict).child(sprofession).child("Message");
        Mylistview.setAdapter(myarrayadapter);
        TextView tvc = findViewById(R.id.head_1);
        tvc.setText(sprofession);
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
                value = myarrayadapter.getItem(position);

                uid = value.substring(value.length()-11,value.length());

                fetchdata(value);

            }
        });
        changedetailsacctoprefession();
        //fetchdata(FirebaseAuth.getInstance().getCurrentUser().getUid());


    }
    public void calluserselectedacc(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UserBusinessAccount.this);
        builder.setMessage("Call this person")
                .setTitle("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Calling : "+uid,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+Uri.encode(uid)));
                        startActivity(intent);

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

//    public void showextramenus(){
//
//        AlertDialog.Builder builder
//                = new AlertDialog
//                .Builder(UserBusinessAccount.this);
//
//
//
//        showmessageindialog =showmessageindialog+"";
//
//
//        builder.setMessage(showmessageindialog);
//        builder.setTitle("User info");
//        builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                calluserselectedacc();
//
//            }
//        });
//        builder.setNegativeButton("Refresh", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                fetchdata(value);
//
//
//
//            }
//        });
//        // Create the Alert dialog
//        AlertDialog alertDialog = builder.create();
//        builder.setCancelable(false);
//
//        // Show the Alert Dialog box
//        alertDialog.show();
//
//
//    }
    void fetchdata(String uidofuser){
        String authiduid = uidofuser.substring(0,29).trim();
        Toast.makeText(getApplicationContext(),authiduid,Toast.LENGTH_SHORT).show();
        reff = FirebaseDatabase.getInstance().getReference().child("Services").child(sstate.toUpperCase()).child(sdistrict.toUpperCase()).child(sprofession.toUpperCase()).child(authiduid);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    dialog = new Dialog(UserBusinessAccount.this );
                    dialog.setContentView(R.layout.custom_business_acc_userinfo);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);


                    String name2 = snapshot.child("name").getValue().toString();
                    String note2 = snapshot.child("note").getValue().toString();
                    String dnt22 = snapshot.child("datentime").getValue().toString();
                    String phnno2 = snapshot.child("phone_number").getValue().toString();
                    String budget2 = snapshot.child("budget").getValue().toString();
                    String area2 = snapshot.child("area").getValue().toString();
                    showmessageindialog = "Name : "+name2+"\nNote : "+note2+"\nTimestamp : "+dnt22+"\nPhone : "+phnno2+"\nBudget : Rs "+budget2+"/-\nArea : "+area2;
                    TextView dname = dialog.findViewById(R.id.name);
                    dname.setText(name2);
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
                    dialog.findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            calluserselectedacc();
                        }
                    });
                    dialog.findViewById(R.id.refesh).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            fetchdata(value);
                        }
                    });
                    dialog.show();

                   // showmessageindialog = "Name: "+name2;

                }
                catch(Exception e){
                    showmessageindialog = "Cannot get data, reason : "+e.getMessage().toString();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       // showextramenus();



    }

    void changedetailsacctoprefession(){
        if(sprofession.equalsIgnoreCase("PAINTER")){

            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://res.cloudinary.com/twenty20/private_images/t_watermark-criss-cross-10/v1480999028000/photosp/63a61411-2d4c-4f9a-97c2-04062a81e325/stock-photo-wall-paint-painting-teamwork-man-street-art-painter-street-scene-street-photography-63a61411-2d4c-4f9a-97c2-04062a81e325.jpg",""));
            slideModels.add(new SlideModel("https://rp-prod-wordpress-b-content.s3.amazonaws.com/assets/2018/08/20151733/sm-external-wall-painter-4.jpg",""));
            slideModels.add(new SlideModel("https://www.homestratosphere.com/wp-content/uploads/2020/02/ceiling-paint-vs-wall-paint-top-February52020-min.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("CARPENTER")){

            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://www.thebalancesmb.com/thmb/c0ZhcxWQuXWC91tV8cE73Nqd6Ug=/3863x2173/smart/filters:no_upscale()/young-stylish-cabinet-maker-with--glasses-and-hairstyle--strong--handsome-craftsman-holding-saw-and-wood-blank-at-workplace-944613244-5af9afc2a18d9e003c17040c.jpg",""));
            slideModels.add(new SlideModel("https://www.okanagan.bc.ca/sites/default/files/styles/large_wide/public/2020-07/Carpentry-Apprenticeship.jpg?itok=pJavcHyf",""));
            slideModels.add(new SlideModel("https://www.viu.ca/sites/default/files/styles/full_bg_focal_breakpoints_theme_viu_theme_bg_xxl_landscape_1x/public/pages/steven-remple.jpg?itok=Z5zajRQq&timestamp=1547580652",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("boutique")){

            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://cdn.choosechicago.com/uploads/2019/06/neighborhood-boutiques-1800x900.jpg",""));
            slideModels.add(new SlideModel("https://imgstaticcontent.lbb.in/lbbnew/wp-content/uploads/2017/07/24144741/httpswww.facebook.com-1.jpg",""));
            slideModels.add(new SlideModel("https://charlotteagenda-charlotteagenda.netdna-ssl.com/wp-content/uploads/2018/11/best-womens-boutiques-in-charlotte.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("House maid")){
            //findViewById(R.id.profile_image).setBackgroundResource(R.drawable.carpenter);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://www.mychores.in/Images/Housemaid-in-Mumbai.jpg",""));
            slideModels.add(new SlideModel("https://www.ics-facilitymanagement.com/img/services/House-Maid-Services.jpg",""));
            slideModels.add(new SlideModel("https://profiles.sulekha.com/mstore/43530795/albums/default/thumbnailfull/shutterstock-648398542.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("LABOUR")){
           // findViewById(R.id.profile_image).setBackgroundResource(R.drawable.carpenter);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://blog.ipleaders.in/wp-content/uploads/2020/02/avoiding-fatal-four.jpg",""));
            slideModels.add(new SlideModel("https://images.newindianexpress.com/uploads/user/imagelibrary/2020/4/9/w900X450/CVBCVBHJhs.jpg",""));
            slideModels.add(new SlideModel("https://www.on-sitemag.com/wp-content/uploads/2019/04/workers_construction_concrete_road.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("IT Services")){
           // findViewById(R.id.profile_image).setBackgroundResource(R.drawable.carpenter);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://www.fidelus.com/wp-content/uploads/2017/09/managed-it-services-og.jpg",""));
            slideModels.add(new SlideModel("https://www.inceptionnet.com/wp-content/uploads/2019/11/small-business-IT-services.jpeg",""));
            slideModels.add(new SlideModel("https://www.ricoh-me.com/en/media/IT%20Services%20-%20Infrastructure%20-%20Thumbnail%20504%20x%20252_tcm89-32451_w504.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("Rooms")){
           // findViewById(R.id.profile_image).setBackgroundResource(R.drawable.carpenter);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://cdn.styleblueprint.com/wp-content/uploads/2015/12/SB-ATL-ZookHome-9-e1538165814448.jpg",""));
            slideModels.add(new SlideModel("https://images.oyoroomscdn.com/uploads/hotel_image/81612/large/f0af88ee7aca453a.jpg",""));
            slideModels.add(new SlideModel("https://www.shalomluxuryrooms.gr/assets/img/slider/new/5.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("food")){
            //findViewById(R.id.profile_image).setBackgroundResource(R.drawable.carpenter);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://www.blueosa.com/wp-content/uploads/2020/01/the-best-top-10-indian-dishes.jpg",""));
            slideModels.add(new SlideModel("https://c.ndtvimg.com/2020-05/9iuj3h1g_indian-food_625x300_19_May_20.jpg",""));
            slideModels.add(new SlideModel("https://img.etimg.com/thumb/msid-72299767,width-1200,height-900,imgsize-365179,overlay-etpanache/photo.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("Coaching")){
           // findViewById(R.id.profile_image).setBackgroundResource(R.drawable.carpenter);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://www.indiaeducation.net/imagesvr_ce/9374/maxresdefault.jpg",""));
            slideModels.add(new SlideModel("https://www.rgu.ac.in/uploads/CKImages/images/net-coaching.jpg",""));
            slideModels.add(new SlideModel("https://akm-img-a-in.tosshub.com/indiatoday/images/story/202008/tea_0.jpeg?ndJOhaA2O6_kIEP_RkQU9F3hcjUuT9i9&size=770:433",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("Electrician")){
           // findViewById(R.id.profile_image).setBackgroundResource(R.drawable.carpenter);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://www.bpmelectric.com/wp-content/uploads/2019/03/Fotolia_170179753_Subscription_Monthly_M.jpg",""));
            slideModels.add(new SlideModel("https://lh3.googleusercontent.com/proxy/X0MAMcGnBXyYk-VSoVZEX9TRLb5kjtlzAjzTmB6Vc4QoYe7tWobaDuXLFtT3fGWusl669aMP8FfAbQCtV-WBQVS8mD_maZfTeQu1gcNm0d-6SRIIMgKmybWWyqF1chYAsKnj9dr1jX16_90ASkV7ZCAECTk",""));
            slideModels.add(new SlideModel("https://blog.herzing.ca/hubfs/electrician-2.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("Counselling")){
           // findViewById(R.id.profile_image).setBackgroundResource(R.drawable.carpenter);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://s3-ap-south-1.amazonaws.com/blogmindler/bloglive/wp-content/uploads/2019/03/11113602/How-to-Become-a-Counsellor-in-India.png",""));
            slideModels.add(new SlideModel("https://miro.medium.com/max/4000/1*8RU80tmjaY8yLz2howNn-g.jpeg",""));
            slideModels.add(new SlideModel("https://counselingchennai.com/wp-content/uploads/2020/05/Online-Live-Diploma-in-School-Corporate-Family-Counselling-via-Google-Meet.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }
        if(sprofession.equalsIgnoreCase("Plumber")){
            //findViewById(R.id.profile_image).setBackgroundResource(R.drawable.carpenter);
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://static.independent.co.uk/s3fs-public/thumbnails/image/2019/09/17/16/istock-1013437722.jpg",""));
            slideModels.add(new SlideModel("https://cahillinc.com/wp-content/uploads/sites/8148/2018/10/emergency-plumber-1-2000x800.jpg",""));
            slideModels.add(new SlideModel("https://www.rd.com/wp-content/uploads/2018/10/07-1.jpg",""));

            topimgslider.setImageList(slideModels,true);

        }




    }

}
