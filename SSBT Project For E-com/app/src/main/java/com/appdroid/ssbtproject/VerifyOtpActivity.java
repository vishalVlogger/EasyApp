package com.appdroid.ssbtproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appdroid.ssbtproject.Activity.Dashboard;
import com.appdroid.ssbtproject.OTP.OtpReceivedInterface;
import com.appdroid.ssbtproject.OTP.SmsBroadcastReceiver;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class VerifyOtpActivity extends AppCompatActivity{

    private static final String TAG = "appdroidTech";

    private EditText otp;
    TextView codeTime,contactNumber;
    Dialog progressDialog;
    String codeFromServer,registerMobileNumber;
    SmsBroadcastReceiver mSmsBroadcastReceiver;
    Button verifyBTN;
    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        mSmsBroadcastReceiver = new SmsBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);

        getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        otp = findViewById(R.id.otp);
        codeTime = findViewById(R.id.codeTime);
        verifyBTN = findViewById(R.id.verifyBTN);
        contactNumber = findViewById(R.id.contactNumber);

        img_back = findViewById(R.id.img_back);

        progressDialog = new Dialog(VerifyOtpActivity.this);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);

        Intent intent = getIntent();

        codeFromServer = intent.getStringExtra("code");
        registerMobileNumber = intent.getStringExtra("mobile");
        contactNumber.setText(registerMobileNumber);

        startSMSListener();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                codeTime.setText("" + millisUntilFinished / 1000);

                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                codeTime.setText("");
                codeTime.setTextColor(getResources().getColor(R.color.bg_color));
            }

        }.start();

        verifyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otp.getText().toString().length() == 6){
                    progressDialog.show();
                    codeVerification();
                }else {
                    Toast.makeText(VerifyOtpActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSmsBroadcastReceiver.setOnOtpListeners(new OtpReceivedInterface() {
            @Override
            public void onOtpReceived(String otsp) {
                String otpFromMassage = otsp.substring(0, 6);
                otp.setText(otpFromMassage);
                codeVerification();

                Log.d("HHHHHHH", "onOtpReceived: "+otsp);
            }

            @Override
            public void onOtpTimeout() {
                Log.d("HHHHHHH", "time  out: ");
            }
        });

    }

    private void codeVerification(){
        String code = otp.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeFromServer, code);
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Log.d(TAG, "codeVerification: "+credential.getSmsCode()+" otp from edit "+code);
            if (credential.getSmsCode().equals(code)){
                updateMobileNumber();
            }
        }else {
            signInWithPhoneAuthCredential(credential);
        }

    }

    private void updateMobileNumber() {

        String  userID = FirebaseAuth.getInstance().getUid();
        Query query = FirebaseFirestore.getInstance().collection("userInfo").whereEqualTo("userId",userID);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String dociD = null;
                if (task.isSuccessful()) {
                    for (DocumentSnapshot snapshot : task.getResult().getDocuments()){
                        Log.d("CCCCCCCCC", "onComplete: "+snapshot.getId());
                        dociD = snapshot.getId();
                    }
                    Log.d("CCCCCCCCC", "onComplete:outside "+dociD);
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("no",registerMobileNumber);

                    Log.d(TAG, "onSuccess:outside  "+dociD);

                    CollectionReference firestore = FirebaseFirestore.getInstance().collection("userInfo");
                    firestore.document(dociD).update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(VerifyOtpActivity.this, Dashboard.class);
                                intent.setAction("fromLogin");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                progressDialog.dismiss();
                                finish();
                            }
                        }
                    });
                }
            }
        });


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                        Intent intent = new Intent(VerifyOtpActivity.this, Dashboard.class);
                        intent.setAction("fromLogin");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        progressDialog.dismiss();
                        finish();
                        Log.d("JJJJJJJJ", "onComplete: "+"Login with allready");
                    }else {
                    Toast.makeText(VerifyOtpActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        });
    }

    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override public void onSuccess(Void aVoid) {
              /*  layoutInput.setVisibility(View.GONE);
                layoutVerify.setVisibility(View.VISIBLE);*/
                //Toast.makeText(OTPActivity.this, "SMS Retriever starts", Toast.LENGTH_LONG).show();
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
                Toast.makeText(VerifyOtpActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}