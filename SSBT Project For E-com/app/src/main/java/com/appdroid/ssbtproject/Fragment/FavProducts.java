package com.appdroid.ssbtproject.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.appdroid.ssbtproject.Activity.LoginActivity;
import com.appdroid.ssbtproject.Activity.MyLishHolder;
import com.appdroid.ssbtproject.Adapter.MyProductAdepter;
import com.appdroid.ssbtproject.Holder.ProductHolder;
import com.appdroid.ssbtproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavProducts extends Fragment {
    private static final String TAG = "DDDDDDDDPPPPPPSLF";
    RecyclerView recyclerView;
    LottieAnimationView progressBar;
    GridLayoutManager gridLayoutManager;
    LottieAnimationView centerProgressBar;
    RelativeLayout noDataLayout;
    List<ProductHolder> list;

    MyProductAdepter  productAdepter;

    RelativeLayout not_login_layout;
    Button loginBTN;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_fav_products, container, false);
        centerProgressBar = view.findViewById(R.id.centerProgressBar);
        noDataLayout  =  view.findViewById(R.id.noDataLayout);
        not_login_layout = view.findViewById(R.id.not_login_layout);
        loginBTN = view.findViewById(R.id.loginBTN);

        list = new ArrayList<>();
        productAdepter = new MyProductAdepter(list, getContext());
        productAdepter.setIsFromeFavList("yes");

        recyclerView = view.findViewById(R.id.productsList);
        recyclerView.hasFixedSize();
        gridLayoutManager = new GridLayoutManager(getContext(),2);

        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(productAdepter);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            getMyListProducts();
        }else {
            noDataLayout.setVisibility(View.GONE);
            not_login_layout.setVisibility(View.VISIBLE);
            centerProgressBar.setVisibility(View.GONE);
        }

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        return view;
    }



    private void getMyListProducts() {

        Log.d("SSSSSS", "getMyListProducts: callll ");
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UsersWishList").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long i,j=0;
                i = dataSnapshot.getChildrenCount();
                if (i==0){
                    noDataLayout.setVisibility(View.VISIBLE);
                    not_login_layout.setVisibility(View.GONE);
                    centerProgressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MyLishHolder myLishHolder = snapshot.getValue(MyLishHolder.class);
                    j++;
                    if (i==j){
                        getProducts(myLishHolder.getDocId(),true);
                        Log.d("chetanss", "onDataChange: "+myLishHolder.getpName()+" last product");
                    }else {
                        getProducts(myLishHolder.getDocId(),false);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getProducts(String docid,boolean isLast) {
        list.clear();
        DocumentReference firestore  =  FirebaseFirestore.getInstance().collection("Product")
                .document(docid);

        firestore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull @NotNull DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()){
                    ProductHolder holder = documentSnapshot.toObject(ProductHolder.class);
                    holder.setDocId(documentSnapshot.getId());
                    if (holder.getVisibility() == null || !holder.getVisibility().equals("hide")){
                        list.add(holder);
                    }
                    if (isLast){
                        Collections.reverse(list);
                        productAdepter.setList(list);
                        productAdepter.notifyDataSetChanged();
                        centerProgressBar.setVisibility(View.GONE);
                    }
                }else {
                    Log.d(TAG, "onSuccess:dddddnotexist");
                }

            }
        });
        /*




        firestore.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot snapshot : task.getResult()) {
                        ProductHolder holder = snapshot.toObject(ProductHolder.class);
                        holder.setDocId(snapshot.getId());
                        if (holder.getVisibility() == null || !holder.getVisibility().equals("hide")){
                            list.add(holder);
                        }
                    }
                    centerProgressBar.setVisibility(View.GONE);
                    productAdepter.notifyDataSetChanged();

                    Log.d("lastvisible", "onComplete: "+task.getResult().size());
                }
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        if (productAdepter != null){
            productAdepter.notifyDataSetChanged();
        }else {
            Log.d(TAG, "onResume: null");
        }


    }
}