package com.appdroid.ssbtproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.appdroid.ssbtproject.BuildConfig;
import com.appdroid.ssbtproject.Fragment.CartFragment;
import com.appdroid.ssbtproject.Fragment.FavProducts;
import com.appdroid.ssbtproject.Fragment.HomeFragment;
import com.appdroid.ssbtproject.Fragment.ProfileFragment;
import com.appdroid.ssbtproject.Fragment.SearchFragment;
import com.appdroid.ssbtproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    public static LinearLayout li_home, li_search, li_favorites, li_cart, li_profile;
    public static ImageView img_circle1, img_circle2, img_circle3, img_circle4, img_circle5;
    public static ImageView img_home, img_search, img_favorites, img_cart, img_profile;
    public static TextView txt_home, txt_search, txt_favorites, txt_cart, txt_profile;
    private int white;

    Button tryAgainBTN,updateAppButton;
    SharedPreferences.Editor editor;
    Dialog notAvailberPopUp;
    private int grey;
    Dialog updateAppDialog,maintainceDialog;
    LottieAnimationView animationView;
    TextView message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        li_home = findViewById(R.id.li_home);
        li_search = findViewById(R.id.li_search);
        li_favorites = findViewById(R.id.li_favorites);
        li_cart = findViewById(R.id.li_cart);
        li_profile = findViewById(R.id.li_profile);

        img_circle1 = findViewById(R.id.img_circle1);
        img_circle2 = findViewById(R.id.img_circle2);
        img_circle3 = findViewById(R.id.img_circle3);
        img_circle4 = findViewById(R.id.img_circle4);
        img_circle5 = findViewById(R.id.img_circle5);

        img_home = findViewById(R.id.img_home);
        img_search = findViewById(R.id.img_search);
        img_favorites = findViewById(R.id.img_favorites);
        img_cart = findViewById(R.id.img_cart);
        img_profile = findViewById(R.id.img_profile);

        txt_home = findViewById(R.id.txt_home);
        txt_search = findViewById(R.id.txt_search);
        txt_favorites = findViewById(R.id.txt_favorites);
        txt_cart = findViewById(R.id.txt_cart);
        txt_profile = findViewById(R.id.txt_profile);

        li_home.setOnClickListener(this);
        li_search.setOnClickListener(this);
        li_favorites.setOnClickListener(this);
        li_cart.setOnClickListener(this);
        li_profile.setOnClickListener(this);
        img_circle1.setVisibility(View.VISIBLE);

        updateAppDialog = new Dialog(this);
        updateAppDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        updateAppDialog.setCancelable(false);
        updateAppDialog.setContentView(R.layout.out_of_date_dialog);
        updateAppButton = updateAppDialog.findViewById(R.id.updateApp);

        maintainceDialog = new Dialog(this);
        maintainceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        maintainceDialog.setCancelable(false);
        maintainceDialog.setContentView(R.layout.under_maintenance_dialog);
        animationView = maintainceDialog.findViewById(R.id.animationView);
        message = maintainceDialog.findViewById(R.id.message);

        Resources res = getResources();
        white = res.getColor(R.color.primary);
        grey = res.getColor(R.color.dark_gray);

        editor = getSharedPreferences("pay",MODE_PRIVATE).edit();
        if (getIntent().getAction() != null){
             if (getIntent().getAction().equals("fromProductDetail")){
                 img_circle1.setVisibility(View.INVISIBLE);
                 img_circle2.setVisibility(View.INVISIBLE);
                 img_circle3.setVisibility(View.INVISIBLE);
                 img_circle4.setVisibility(View.VISIBLE);
                 img_circle5.setVisibility(View.INVISIBLE);

                 img_home.setImageResource(R.drawable.ic_home_gray);
                 img_search.setImageResource(R.drawable.ic_search_gray);
                img_favorites.setImageResource(R.drawable.ic_heart_gary);
                 img_cart.setImageResource(R.drawable.ic_cart_green);
                 img_profile.setImageResource(R.drawable.ic_profile_gray);

                 txt_home.setTextColor(grey);
                 txt_search.setTextColor(grey);
                 txt_favorites.setTextColor(grey);
                 txt_cart.setTextColor(white);
                 txt_profile.setTextColor(grey);
                 replace_fragment(new CartFragment());
             }else if (getIntent().getAction().equals("fromLogin")){
                 img_circle1.setVisibility(View.INVISIBLE);
                 img_circle2.setVisibility(View.INVISIBLE);
                 img_circle3.setVisibility(View.INVISIBLE);
                 img_circle4.setVisibility(View.VISIBLE);
                 img_circle5.setVisibility(View.INVISIBLE);

                 img_home.setImageResource(R.drawable.ic_home_gray);
                 img_search.setImageResource(R.drawable.ic_search_gray);
                 img_favorites.setImageResource(R.drawable.ic_heart_gary);
                 img_cart.setImageResource(R.drawable.ic_cart_green);
                 img_profile.setImageResource(R.drawable.ic_profile_gray);

                 txt_home.setTextColor(grey);
                 txt_search.setTextColor(grey);
                 txt_favorites.setTextColor(grey);
                 txt_cart.setTextColor(white);
                 txt_profile.setTextColor(grey);
                 replace_fragment(new CartFragment());

                 Toast.makeText(this, "Login Successful..", Toast.LENGTH_SHORT).show();
             }
        }else {
            replace_fragment(new HomeFragment());
            img_home.setImageResource(R.drawable.ic_home_green);
            txt_home.setTextColor(white);

        }

        getRazerPayID();
        getCurrentVersion();

        updateAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.appdroid.dandagezmart"));
                startActivity(intent);
            }
        });
    }

    private void getCurrentVersion() {
        int CurrentAppVersionCode;
        CurrentAppVersionCode = BuildConfig.VERSION_CODE;

        DocumentReference firestore = FirebaseFirestore.getInstance().collection("AppInfo")
                .document("document");


        firestore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull @NotNull DocumentSnapshot value) {
                if (value.getBoolean("underMaintenance") != null){
                    try {

                        if (value.getBoolean("underMaintenance")){
                            message.setText(value.getString("massage"));
                            animationView.setAnimationFromUrl(value.getString("animationrul"));
                            maintainceDialog.show();
                        }

                    }catch (Exception e){
                        Toast.makeText(Dashboard.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("DDDDDDASS", "onEvent: "+e.getLocalizedMessage());
                    }
                }


                if (value.getLong("code") != null){
                    if (CurrentAppVersionCode < value.getLong("code")){
                        if (updateAppDialog != null){
                            updateAppDialog.show();
                        }
                    }else {
                        if (updateAppDialog != null)
                            updateAppDialog.dismiss();
                    }
                }
            }
        });

  /*
        firestore.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("SSSSSSSS", "Listen failed.", error);
                    return;
                }
                Log.d("JJJJJJFFFF", "onEvent: "+ value.getLong("code"));

                if (value.getBoolean("underMaintenance") != null){
                    try {

                        if (value.getBoolean("underMaintenance")){
                            message.setText(value.getString("massage"));
                            animationView.setAnimationFromUrl(value.getString("animationrul"));
                            updateAppDialog.show();
                        }

                    }catch (Exception e){
                        Toast.makeText(Dashboard.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("DDDDDDASS", "onEvent: "+e.getLocalizedMessage());
                    }
                }

                if (value.getLong("code") != null){
                    if (CurrentAppVersionCode < value.getLong("code")){
                        if (updateAppDialog != null){
                            updateAppDialog.show();
                        }
                    }else {
                        if (updateAppDialog != null)
                            updateAppDialog.dismiss();
                    }
                }
            }
        });*/

    }

    private void getRazerPayID() {
        DocumentReference firestore = FirebaseFirestore.getInstance().collection("razorpay")
                .document("doc");
        firestore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String id = documentSnapshot.getString("id");
                    editor.putString("key",id);
                    editor.apply();
                   Log.d("chetan", "onSuccess: "+id);
            }
        });
    }

    public void replace_fragment(Fragment fragment) {
        fragment.setRetainInstance(true);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (fragment.getClass().getSimpleName().equals("CartFragment")){
            transaction.replace(R.id.main_frame, fragment,"card_fragment");
        }else if (fragment.getClass().getSimpleName().equals("ProfileFragment")) {

            transaction.replace(R.id.main_frame, fragment,"profileFragment");
        }else if (fragment.getClass().getSimpleName().equals("FavProducts")) {
            transaction.replace(R.id.main_frame, fragment,"favproducts");
        }else {
                transaction.replace(R.id.main_frame, fragment);
        }
        transaction.commit();

        Log.d("DDDDDDDDDDDSSSSSSSS", "replace_fragment: "+fragment.getClass().getSimpleName());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.li_home:
                img_circle1.setVisibility(View.VISIBLE);
                img_circle2.setVisibility(View.INVISIBLE);
                img_circle3.setVisibility(View.INVISIBLE);
                img_circle4.setVisibility(View.INVISIBLE);
                img_circle5.setVisibility(View.INVISIBLE);

                img_home.setImageResource(R.drawable.ic_home_green);
                img_search.setImageResource(R.drawable.ic_search_gray);
                img_favorites.setImageResource(R.drawable.ic_heart_gary);
                img_cart.setImageResource(R.drawable.ic_cart_gray);
                img_profile.setImageResource(R.drawable.ic_profile_gray);

                txt_home.setTextColor(white);
                txt_search.setTextColor(grey);
                txt_favorites.setTextColor(grey);
                txt_cart.setTextColor(grey);
                txt_profile.setTextColor(grey);
                replace_fragment(new HomeFragment());
                break;
            case R.id.li_search:
                img_circle1.setVisibility(View.INVISIBLE);
                img_circle2.setVisibility(View.VISIBLE);
                img_circle3.setVisibility(View.INVISIBLE);
                img_circle4.setVisibility(View.INVISIBLE);
                img_circle5.setVisibility(View.INVISIBLE);

                img_home.setImageResource(R.drawable.ic_home_gray);
                img_search.setImageResource(R.drawable.ic_search_green);
                img_favorites.setImageResource(R.drawable.ic_heart_gary);
                img_cart.setImageResource(R.drawable.ic_cart_gray);
                img_profile.setImageResource(R.drawable.ic_profile_gray);

                txt_home.setTextColor(grey);
                txt_search.setTextColor(white);
                txt_favorites.setTextColor(grey);
                txt_cart.setTextColor(grey);
                txt_profile.setTextColor(grey);
                replace_fragment(new SearchFragment());
                break;
            case R.id.li_favorites:
                img_circle1.setVisibility(View.INVISIBLE);
                img_circle2.setVisibility(View.INVISIBLE);
                img_circle3.setVisibility(View.VISIBLE);
                img_circle4.setVisibility(View.INVISIBLE);
                img_circle5.setVisibility(View.INVISIBLE);

                img_home.setImageResource(R.drawable.ic_home_gray);
                img_search.setImageResource(R.drawable.ic_search_gray);
                img_favorites.setImageResource(R.drawable.ic_heart_green);
                img_cart.setImageResource(R.drawable.ic_cart_gray);
                img_profile.setImageResource(R.drawable.ic_profile_gray);

                txt_home.setTextColor(grey);
                txt_search.setTextColor(grey);
                txt_favorites.setTextColor(white);
                txt_cart.setTextColor(grey);
                txt_profile.setTextColor(grey);
                replace_fragment(new FavProducts());
                break;
            case R.id.li_cart:
                img_circle1.setVisibility(View.INVISIBLE);
                img_circle2.setVisibility(View.INVISIBLE);
                img_circle3.setVisibility(View.INVISIBLE);
                img_circle4.setVisibility(View.VISIBLE);
                img_circle5.setVisibility(View.INVISIBLE);

                img_home.setImageResource(R.drawable.ic_home_gray);
                img_search.setImageResource(R.drawable.ic_search_gray);
                img_favorites.setImageResource(R.drawable.ic_heart_gary);
                img_cart.setImageResource(R.drawable.ic_cart_green);
                img_profile.setImageResource(R.drawable.ic_profile_gray);

                txt_home.setTextColor(grey);
                txt_search.setTextColor(grey);
                txt_favorites.setTextColor(grey);
                txt_cart.setTextColor(white);
                txt_profile.setTextColor(grey);
                replace_fragment(new CartFragment());
                break;
            case R.id.li_profile:
                img_circle1.setVisibility(View.INVISIBLE);
                img_circle2.setVisibility(View.INVISIBLE);
               img_circle3.setVisibility(View.INVISIBLE);
                img_circle4.setVisibility(View.INVISIBLE);
                img_circle5.setVisibility(View.VISIBLE);

                img_home.setImageResource(R.drawable.ic_home_gray);
                img_search.setImageResource(R.drawable.ic_search_gray);
               img_favorites.setImageResource(R.drawable.ic_heart_gary);
                img_cart.setImageResource(R.drawable.ic_cart_gray);
                img_profile.setImageResource(R.drawable.ic_profile_green);

                txt_home.setTextColor(grey);
                txt_search.setTextColor(grey);
                txt_favorites.setTextColor(grey);
                txt_cart.setTextColor(grey);
                txt_profile.setTextColor(white);
                replace_fragment(new ProfileFragment());
                break;
        }
    }
}