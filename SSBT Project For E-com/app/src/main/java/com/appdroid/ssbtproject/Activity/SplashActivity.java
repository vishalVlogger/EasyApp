package com.appdroid.ssbtproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.appdroid.ssbtproject.Holder.ProductHolder;
import com.appdroid.ssbtproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                FirebaseDynamicLinks.getInstance()
                        .getDynamicLink(getIntent()).addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull PendingDynamicLinkData pendingDynamicLinkData) {
                        if (pendingDynamicLinkData != null){
                            Uri deepLink = null;
                            deepLink = pendingDynamicLinkData.getLink();
                            String s = String.valueOf(deepLink);
                            Log.d("SSSSSSAAA", "onSuccess: "+s);
                            String arr[] = s.split("https://ssbtproject.page.link/");
                            String docID[] = arr[1].split("/");
                            Log.d("SSSSSSAAA", "onSuccess: docid = "+docID[0] +" flag : "+docID[1]);

                            getProduct(docID[0]);
                        }else {
                            Intent intent = new Intent(SplashActivity.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        },2000);

    }

    private void getProduct(String s) {
        DocumentReference documentReference  = FirebaseFirestore.getInstance().collection("Product").document(s);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ProductHolder productHolder = task.getResult().toObject(ProductHolder.class);
                    productHolder.setDocId(s);
                    Intent intent = new Intent(SplashActivity.this, ProductDetails.class);
                    intent.putExtra("all",productHolder);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}