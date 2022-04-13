package com.appdroid.ssbtproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appdroid.ssbtproject.Holder.UserHolder;
import com.appdroid.ssbtproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;

public class RequestProduct extends AppCompatActivity {

        ImageView backLayout;
        TextView userName,userEmail,userNumber;
        UserHolder userHolder;
        Button sendRequest;
        EditText productName,productDetails;
         Dialog pDialog;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_request_product);

            backLayout = findViewById(R.id.img_back);
            userName = findViewById(R.id.userName);
            userEmail = findViewById(R.id.userEmail);
            userNumber = findViewById(R.id.userNumber);

            sendRequest = findViewById(R.id.sendRequest);

            productName =findViewById(R.id.productName);
            productDetails = findViewById(R.id.productDetails);

            userHolder = (UserHolder) getIntent().getSerializableExtra("all");

            userName.setText(userHolder.getName());
            userEmail.setText(userHolder.getEmail());
            userNumber.setText(userHolder.getNo());

            userName.setText(userHolder.getName());
            userEmail.setText(userHolder.getEmail());
            userNumber.setText(userHolder.getNo());

            pDialog = new Dialog(RequestProduct.this);
            pDialog.setContentView(R.layout.progress_dialog);
            pDialog.setCancelable(false);

            backLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            sendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if (!productName.getText().toString().isEmpty() && !productDetails.getText().toString().isEmpty()){

                        pDialog.show();
                       uploadRequast();
                   }else {
                       Toast.makeText(RequestProduct.this, "Please enter product name & description.", Toast.LENGTH_SHORT).show();
                   }
                }
            });

        }

    private void uploadRequast() {
        CollectionReference firebaseFirestore = FirebaseFirestore.getInstance().collection("RequastedProducts");
        HashMap<String,Object> objectHashMap  = new HashMap<>();
        objectHashMap.put("productName",productName.getText().toString());
        objectHashMap.put("productDesc",productDetails.getText().toString());
        objectHashMap.put("userId",userHolder.getUserId());
        objectHashMap.put("date", new Timestamp(new Date()));
        firebaseFirestore.add(objectHashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RequestProduct.this, "Your request submitted successfully", Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                    onBackPressed();
                }
            }
        });
    }
}