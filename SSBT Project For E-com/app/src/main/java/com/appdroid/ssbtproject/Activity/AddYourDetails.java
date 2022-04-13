package com.appdroid.ssbtproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appdroid.ssbtproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class AddYourDetails extends AppCompatActivity {

    EditText edName,edContact,edAddress,edPin,edEmail;
    Button save;
    ImageView back;
    TextView txtPin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_details);
        back = findViewById(R.id.back);
        edName = findViewById(R.id.edName);
        edContact = findViewById(R.id.edContact);
        edAddress = findViewById(R.id.edAddress);
        edPin = findViewById(R.id.edPin);
        txtPin = findViewById(R.id.txtPin);
        edEmail = findViewById(R.id.edEmail);

        edContact.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        edContact.setEnabled(false);

        save =findViewById(R.id.save);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edName.getText().toString().isEmpty()
                        && !edContact.getText().toString().isEmpty()
                        && !edAddress.getText().toString().isEmpty()
                        && !edPin.getText().toString().isEmpty()
                        && !edEmail.getText().toString().isEmpty()) {


                    if (edPin.getText().toString().length() == 6){
                        checkPincodeAvailbility(edPin.getText().toString());
                    }else {
                        Toast.makeText(AddYourDetails.this, "Enter 6-digits pincode.", Toast.LENGTH_SHORT).show();
                    }

                    //saveUserInfo();

                }else {
                    Toast.makeText(AddYourDetails.this, "Enter All Details.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void checkPincodeAvailbility(String pinCode) {

        DocumentReference firestore = FirebaseFirestore.getInstance()
                .collection("Content").document("Categories");

        firestore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<String> list = (List<String>) documentSnapshot.get("pinCode");

                if (list.contains(pinCode)){
                    SharedPreferences.Editor editor = AddYourDetails.this.getSharedPreferences("pincode",MODE_PRIVATE).edit();
                    editor.putString("code",pinCode);
                    editor.apply();
                    saveUserInfo();
                }else {
                    Toast.makeText(AddYourDetails.this, getString(R.string.notAvailble), Toast.LENGTH_SHORT).show();
                    edPin.setTextColor(AddYourDetails.this.getResources().getColor(R.color.red));
                    txtPin.setTextColor(AddYourDetails.this.getResources().getColor(R.color.red));
                }
            }
        });
    }

    private void saveUserInfo() {
        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("name",edName.getText().toString());
        hashMap.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("no",FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        hashMap.put("address",edAddress.getText().toString());
        hashMap.put("pin",edPin.getText().toString());
        hashMap.put("email",edEmail.getText().toString());
        CollectionReference firestore = FirebaseFirestore.getInstance().collection("userInfo");
        firestore.add(hashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    onBackPressed();
                    Toast.makeText(AddYourDetails.this, "Details Uploaded.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}