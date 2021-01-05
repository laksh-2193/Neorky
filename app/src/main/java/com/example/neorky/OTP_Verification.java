package com.example.neorky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OTP_Verification extends AppCompatActivity {
    Button otp_verity;
    EditText otp_message;
    TextView waitmessage;
    String otp, phone_number;
    private FirebaseAuth firebaseAuth;
    DatabaseReference reff;
    BusinessAccount businessAccount;
    Button verify_btn2;

    Users users;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p__verification);
        otp = getIntent().getStringExtra("auth");
        phone_number = getIntent().getStringExtra("phoneNo");
        otp_message = findViewById(R.id.otp);
        waitmessage = findViewById(R.id.progress_text);
        firebaseAuth = FirebaseAuth.getInstance();
        verify_btn2 = findViewById(R.id.verify_btn);

        findViewById(R.id.back_btn_phn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        findViewById(R.id.verify_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closekeyboard();
                verify_btn2.setEnabled(false);
                verify_btn2.setText("Please wait...");
                findViewById(R.id.progress_circular).setVisibility(View.VISIBLE);
                waitmessage.setVisibility(View.VISIBLE);
                waitmessage.setText("Please wait..");
                waitmessage.setTextColor(Color.DKGRAY);
                if(otp_message.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(OTP_Verification.this,"Please enter OTP",Toast.LENGTH_LONG).show();


                }
                else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp,otp_message.getText().toString());
                    signin(credential);


                }
                verify_btn2.setEnabled(true);
                verify_btn2.setText("Verify OTP");





            }
        });
    }
    void signin(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(OTP_Verification.this,"Verification Done",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(OTP_Verification.this,Profile_Page.class);
                    senddatatofirebase();


                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();

                }
                else{
                    Toast.makeText(OTP_Verification.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    waitmessage.setText("Failed to verify OTP : "+task.getException().getMessage().toString());
                    waitmessage.setTextColor(Color.RED);
                    findViewById(R.id.progress_circular).setVisibility(View.INVISIBLE);


                }
            }
        });
        verify_btn2.setEnabled(true);
        verify_btn2.setText("Verify OTP");



    }

    void senddatatofirebase(){
        users = new Users();
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());

        users.setPhone_Number(phone_number);

        reff.child("Profile").setValue(users);



        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("BusinessAccount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                }
                else{
                    businessAccount = new BusinessAccount();
                    reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
                    businessAccount.setProfession("N/A");
                    businessAccount.setDistrict("N/A");
                    businessAccount.setState("N/A");
                    reff.child("BusinessAccount").setValue(businessAccount);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    void closekeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }


}
