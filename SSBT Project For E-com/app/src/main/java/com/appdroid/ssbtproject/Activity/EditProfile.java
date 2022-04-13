package com.appdroid.ssbtproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class EditProfile extends AppCompatActivity {
    EditText edName,edAddress,edPin,edEmail;
    Button save;
    TextView txtPin;
    ImageView back;
    UserHolder userHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        edName = findViewById(R.id.edName);
        back = findViewById(R.id.img_back);

        Intent  intent = getIntent();
        userHolder = (UserHolder) intent.getSerializableExtra("all");

        edAddress = findViewById(R.id.edAddress);
        edPin = findViewById(R.id.edPin);

        edEmail = findViewById(R.id.edEmail);

        save =findViewById(R.id.save);
        txtPin = findViewById(R.id.txtPin);

        inItData();

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

                        && !edAddress.getText().toString().isEmpty()
                        && !edPin.getText().toString().isEmpty()
                        && !edEmail.getText().toString().isEmpty()) {

                    if (edPin.getText().toString().length() == 6){
                        checkPincodeAvailbility(edPin.getText().toString());
                    }else {
                        Toast.makeText(EditProfile.this, "Enter 6-digits pincode.", Toast.LENGTH_SHORT).show();
                    }
                 //   updateUserInfo();

                }else {
                    Toast.makeText(EditProfile.this, "Enter All Details.", Toast.LENGTH_SHORT).show();
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
                    SharedPreferences.Editor editor = EditProfile.this.getSharedPreferences("pincode",MODE_PRIVATE).edit();
                    editor.putString("code",pinCode);
                    editor.apply();
                    updateUserInfo();
                }else {
                    Toast.makeText(EditProfile.this, getString(R.string.notAvailble), Toast.LENGTH_SHORT).show();
                    edPin.setTextColor(EditProfile.this.getResources().getColor(R.color.red));
                    txtPin.setTextColor(EditProfile.this.getResources().getColor(R.color.red));
                }
            }
        });
    }

    private void inItData() {
        edName.setText(userHolder.getName());
        edAddress.setText(userHolder.getAddress());
        edEmail.setText(userHolder.getEmail());
        edPin.setText(userHolder.getPin());
    }

    private void updateUserInfo() {

        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("name",edName.getText().toString());
        hashMap.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("address",edAddress.getText().toString());
        hashMap.put("pin",edPin.getText().toString());
        hashMap.put("email",edEmail.getText().toString());
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("userInfo")
                .document(userHolder.getDocID());
        documentReference.update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    onBackPressed();
                    Toast.makeText(EditProfile.this, "Details Updated.", Toast.LENGTH_SHORT).show();
                }
            }
        });






    }
}