package com.example.neorky;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Phonenumber_activity extends AppCompatActivity {
    public EditText phone;
    TextView prg_txt;
    ProgressBar progressBar;
    String phoneNo;
    CheckBox checkBox;
    FirebaseAuth auth;

    int status_api = 1;
    Button btn_next;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber_activity);
        startActivity(new Intent(getApplicationContext(),OnBoarding.class));


        phone = findViewById(R.id.phn_no);
        prg_txt = findViewById(R.id.progress_text);

        progressBar = findViewById(R.id.progress_circular);
        checkBox = findViewById(R.id.checkbox);
        auth = FirebaseAuth.getInstance();
        btn_next = findViewById(R.id.continue_btn);
        askpermission();




        findViewById(R.id.continue_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closekeyboard();
                btn_next.setText("Please wait...");
                btn_next.setEnabled(false);

                    prg_txt.setVisibility(View.VISIBLE);
                    prg_txt.setText("Sending OTP on "+phone.getText().toString()+"");
                    progressBar.setVisibility(View.VISIBLE);
                    prg_txt.setTextColor(Color.DKGRAY);
                    phoneNo= phone.getText().toString();
                    phoneNo = "+91"+phoneNo;
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNo)
                            .setTimeout(60L,TimeUnit.SECONDS)
                            .setActivity(Phonenumber_activity.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);








            }

        });
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //SignIn(phoneAuthCredential);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                prg_txt.setText(e.getMessage().toString());
                progressBar.setVisibility(View.INVISIBLE);
                prg_txt.setTextColor(Color.RED);
                btn_next.setText("Continue");
                btn_next.setEnabled(true);

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressBar.setVisibility(View.INVISIBLE);
                prg_txt.setTextColor(Color.GREEN);
                prg_txt.setText("Code Sent");
                Intent intent = new Intent(Phonenumber_activity.this,OTP_Verification.class);
                intent.putExtra("auth",s);
                intent.putExtra("phoneNo",phoneNo);
                startActivity(intent);
                overridePendingTransition(0,0);
                btn_next.setText("Continue");
                btn_next.setEnabled(true);
                //finish();
            }
        };





    }
    void closekeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    void askpermission(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                TastyToast.makeText(getApplicationContext(),"Permission Granted",TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();

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







}
