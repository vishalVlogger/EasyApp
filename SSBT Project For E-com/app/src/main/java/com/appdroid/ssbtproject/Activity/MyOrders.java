package com.appdroid.ssbtproject.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appdroid.ssbtproject.Adapter.MyOrdersAdepter;
import com.appdroid.ssbtproject.Holder.OrderHolder;
import com.appdroid.ssbtproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyOrders extends AppCompatActivity {

    public static LinearLayout cardEmptyLayout;
    private static final String TAG = "SSSSSSSSSSS";
    RecyclerView recyclerView;
    List<OrderHolder> list;
    MyOrdersAdepter adepter;
    Dialog progressDialog;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        recyclerView = findViewById(R.id.myOrdersRecylerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imgBack = findViewById(R.id.img_back);
        cardEmptyLayout =   findViewById(R.id.cardEmptyLayout);

        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        progressDialog.show();

        list = new ArrayList<>();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getOders();
    }

    private void getOders() {
        Query query = FirebaseFirestore.getInstance()
                .collection("Orders")
                .orderBy("orderDate", Query.Direction.DESCENDING)
                .whereEqualTo("userNo", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                       OrderHolder orderHolder =  snapshot.toObject(OrderHolder.class);
                       orderHolder.setDocID(snapshot.getId());
                       list.add(orderHolder);

                    Log.d(TAG, "onSuccess: "+orderHolder.getProductDetails().size());
                }
                progressDialog.dismiss();

                if (list.size()>0){
                    cardEmptyLayout.setVisibility(View.GONE);
                }

                adepter  = new MyOrdersAdepter(list,MyOrders.this);
                recyclerView.setAdapter(adepter);

            }
        });
    }
}