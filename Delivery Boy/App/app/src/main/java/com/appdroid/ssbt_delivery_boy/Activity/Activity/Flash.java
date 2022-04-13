package com.appdroid.ssbt_delivery_boy.Activity.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.appdroid.ssbt_delivery_boy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Flash extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user!= null){

                    Intent i = new Intent(getApplicationContext(), NormalDeliveryActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);










    }
}