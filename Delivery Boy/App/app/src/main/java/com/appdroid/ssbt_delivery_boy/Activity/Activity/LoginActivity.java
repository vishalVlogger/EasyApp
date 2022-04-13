package com.appdroid.ssbt_delivery_boy.Activity.Activity;

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
import android.widget.Toast;

import com.appdroid.ssbt_delivery_boy.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "chetanGirnare";
    EditText mobileNo;
    Button nextBtn;

    private final static int RESOLVE_HINT = 1011;
    Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextBtn = findViewById(R.id.nextBtn);
        mobileNo = findViewById(R.id.mobileNo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        progressDialog = new Dialog(LoginActivity.this);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileNo.getText().toString().length() == 10){
                    progressDialog.show();
                    checkIsRegisterOrNot(mobileNo.getText().toString());
                  //  getVerificationCode("+91"+mobileNo.getText().toString());
                }else {
                    Toast.makeText(LoginActivity.this, "Mobile number must be 10 digits", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getPhone();
    }

    private void checkIsRegisterOrNot(String toString) {
        Query firestore = FirebaseFirestore.getInstance().collection("DeliveryBoy")
                .whereEqualTo("mobileNo",toString);
        firestore.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()>0) {
                    Log.d(TAG, "onSuccess: availbel");
                    getVerificationCode("+91"+toString);
                }else {
                    progressDialog.dismiss();

                    Intent intent = new Intent(LoginActivity.this,NotRegisterDeliveryBoy.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    Log.d(TAG, "onSuccess: Not availble");
                }
            }
        });

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
                    mobileNo.setText(mobNumber.substring(3));
                    nextBtn.setEnabled(false);
                    nextBtn.setAlpha(0.7f);
                    progressDialog.show();

                    Log.d(TAG, "onActivityResult: "+mobNumber);
                    checkIsRegisterOrNot(mobileNo.getText().toString());
                    //getVerificationCode(mobNumber);
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
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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


            Intent i = new Intent(getApplicationContext(), Otp.class);
            i.putExtra("code",verificationId);
            i.putExtra("mobile","+91"+mobileNo.getText().toString());
            startActivity(i);
            finish();


        }
    };

}