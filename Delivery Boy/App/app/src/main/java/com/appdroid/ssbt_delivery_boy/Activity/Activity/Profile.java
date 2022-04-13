package com.appdroid.ssbt_delivery_boy.Activity.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appdroid.ssbt_delivery_boy.R;
import com.appdroid.ssbt_delivery_boy.holder.Holder;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Profile extends AppCompatActivity {
    ImageView photo;
    TextView name,no,email;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        photo = findViewById(R.id.photo);
        name = findViewById(R.id.name);
        no = findViewById(R.id.contactno);
        email = findViewById(R.id.email);

         auth = FirebaseAuth.getInstance();
        String s = auth.getCurrentUser().getPhoneNumber();

        String finalMobileNo = s.substring(
                3,13);

        Log.d("mobilenooooooooo", "onCreate: "+finalMobileNo);


        Query query = FirebaseFirestore.getInstance().collection("DeliveryBoy")
                .whereEqualTo("mobileNo",finalMobileNo);

        Log.d("QQQQQQQQQQQQQQ", "onCreate: "+query);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot queryDocumentSnapshot :queryDocumentSnapshots) {
                    Holder holder = queryDocumentSnapshot.toObject(Holder.class);


                    Holder holder1 = new Holder();

                    String cno = holder.getMobileNo();
                    String dname =holder.getName();
                    String demail = holder.getEmail();
                    Log.d("nooooooooooooooo", "onSuccess: "+cno);


                    name.setText(dname);
                    email.setText(demail);
                    no.setText(cno);
                    Glide.with(getApplicationContext()).load(holder.getUidPhoto()).into(photo);



                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Profile.this, "errrrror", Toast.LENGTH_SHORT).show();

            }
        });








    }
}