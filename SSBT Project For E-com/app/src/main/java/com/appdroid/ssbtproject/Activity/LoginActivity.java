package com.appdroid.ssbtproject.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appdroid.ssbtproject.R;
import com.appdroid.ssbtproject.VerifyOtpActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    EditText edMobileNum;
    Button sendOTP;
    private final static int RESOLVE_HINT = 1011;
    Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        progressDialog = new Dialog(LoginActivity.this);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);

        ImageView img_back = findViewById(R.id.img_back);
        edMobileNum = findViewById(R.id.edMobileNum);
        sendOTP = findViewById(R.id.sendOTP);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edMobileNum.getText().toString().length() == 10){
                    progressDialog.show();
                    getVerificationCode("+91"+edMobileNum.getText().toString());
                }else {
                    Toast.makeText(LoginActivity.this, "Mobile number must be 10 digits", Toast.LENGTH_SHORT).show();
                }

            }
        });

        getPhone();

    }

    private void getPhone() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) LoginActivity.this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) LoginActivity.this)
                .build();

        googleApiClient.connect();
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient,hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(),
                    RESOLVE_HINT,null,0,0,0);
        } catch (IntentSender.SendIntentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_HINT){
            if (resultCode == RESULT_OK){
                com.google.android.gms.auth.api.credentials.Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (credential != null){
                    String  mobNumber = credential.getId();
                    Log.d("TAGGGGGGG", "onActivityResult: "+mobNumber);
                    edMobileNum.setText(mobNumber.substring(3));
                    sendOTP.setEnabled(false);
                    sendOTP.setAlpha(0.7f);
                    progressDialog.show();
                    getVerificationCode(mobNumber);
                    // progressDialog.show();
                }else {
                    Toast.makeText(this, "error",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void getVerificationCode(String phoneNo){
        Log.d("GGGGGGGGG", "getVerificationCode: "+phoneNo);
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(phoneNo)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {


   /*         Intent intent = new Intent(LoginActivity.this,CreateAccount.class);
            startActivity(intent);
            finish();*/

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "Failed...", Toast.LENGTH_SHORT).show();

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
                Log.d("GGGGGGGGGGG", "onVerificationFailed: +"+e.getLocalizedMessage());
            } else if (e instanceof FirebaseTooManyRequestsException) {

                Log.d("GGGGGGGGGGG", "onVerificationFailed: +"+e.getLocalizedMessage());
            }

        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {

            Toast.makeText(LoginActivity.this, "OTP Sent Successfully", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();


            Intent i = new Intent(getApplicationContext(), VerifyOtpActivity.class);
            i.putExtra("code",verificationId);
            i.putExtra("mobile","+91"+edMobileNum.getText().toString());
            startActivity(i);
            finish();


        }
    };


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}