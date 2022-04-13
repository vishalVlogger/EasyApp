package com.appdroid.ssbt_delivery_boy.Activity.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.appdroid.ssbt_delivery_boy.R;
import com.appdroid.ssbt_delivery_boy.adapter.OrderAdapter;
import com.appdroid.ssbt_delivery_boy.holder.OrderHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NormalDeliveryActivity extends AppCompatActivity {

    private static final String TAG = "dashboard_delivery_boy";
    RecyclerView recyclerView;
    List<OrderHolder> list;
    OrderAdapter orderAdapter;
    String deliveryBoyNumber;
    RelativeLayout noOrdersLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dshboard);

        recyclerView = findViewById(R.id.dashrecyclerview);
        noOrdersLayout = findViewById(R.id.noOrdersLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(NormalDeliveryActivity.this));
        recyclerView.hasFixedSize();

        list = new ArrayList<>();

        deliveryBoyNumber  = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        deliveryBoyNumber = deliveryBoyNumber.substring(3);
        Log.d("KKKKKKKKKKKK", "onCreate: "+deliveryBoyNumber);


            getListForDeliveryBoy();
    }

    private void getListForDeliveryBoy() {

        list.clear();
        Query query = FirebaseFirestore.getInstance().collection("Orders")
                .orderBy("assignDate", Query.Direction.DESCENDING)
                .whereEqualTo("deliveryBoyNo",deliveryBoyNumber)
                .whereEqualTo("status","delivery");

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()) {
                    OrderHolder orderHolder = dc.toObject(OrderHolder.class);
                    if (orderHolder.getDeliveryBoyNo().equals(deliveryBoyNumber)){
                        orderHolder.setDocID(dc.getId());
                        list.add(orderHolder);
                        Log.d(TAG, "onSuccess: "+orderHolder.getDeliveryBoyName());
                    }
                }
                Log.d(TAG, "onEvent: "+list.size());

                if (list.size() == 0){
                    noOrdersLayout.setVisibility(View.VISIBLE);
                }else {
                    noOrdersLayout.setVisibility(View.GONE);
                }
                orderAdapter= new OrderAdapter(NormalDeliveryActivity.this,list);
                recyclerView.setAdapter(orderAdapter);
                orderAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
            }
        });


/*        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "listen:error", error);
                    return;
                }

                for (DocumentSnapshot dc : value.getDocuments()) {
                    OrderHolder orderHolder = dc.toObject(OrderHolder.class);
                    if (orderHolder.getDeliveryBoyNo().equals(deliveryBoyNumber)){
                        orderHolder.setDocID(dc.getId());
                        list.add(orderHolder);
                        Log.d(TAG, "onSuccess: "+orderHolder.getDeliveryBoyName());
                    }
                }
                Log.d(TAG, "onEvent: "+list.size());

                if (list.size() == 0){
                    noOrdersLayout.setVisibility(View.VISIBLE);
                }else {
                    noOrdersLayout.setVisibility(View.GONE);
                }
                orderAdapter= new OrderAdapter(NormalDeliveryActivity.this,list);
                recyclerView.setAdapter(orderAdapter);
                orderAdapter.notifyDataSetChanged();
            }
        });*/


    }
}