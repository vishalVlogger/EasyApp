package com.appdroid.ssbt_delivery_boy.Activity.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.appdroid.ssbt_delivery_boy.R;
import com.appdroid.ssbt_delivery_boy.adapter.DailyOrderAdepter;
import com.appdroid.ssbt_delivery_boy.holder.DailyOrderHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DailyOrderDelivery extends AppCompatActivity {
    private static final String TAG = "dashboard_delivery_boy";
    RecyclerView recyclerView;
    List<DailyOrderHolder> list;
    String deliveryBoyNumber;
    DailyOrderAdepter dailyOrderAdepter;

    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_order_delivery);
        recyclerView = findViewById(R.id.dailyOrderDelivery);

        imgBack = findViewById(R.id.img_back);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        list = new ArrayList<>();
        getDailyOrderListForDeliveryBoy();
        deliveryBoyNumber  = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        deliveryBoyNumber = deliveryBoyNumber.substring(3);
    }

    private void getDailyOrderListForDeliveryBoy() {
        Query query = FirebaseFirestore.getInstance().collection("DailyOrders")
                .orderBy("assignDate", Query.Direction.DESCENDING)
                .whereEqualTo("status","delivery");


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "listen:error", error);
                    return;
                }

                for (DocumentSnapshot dc : value.getDocuments()) {
                    DailyOrderHolder orderHolder = dc.toObject(DailyOrderHolder.class);
                    if (orderHolder.getDeliveryBoyNo().equals(deliveryBoyNumber)){
                        orderHolder.setDocId(dc.getId());
                        list.add(orderHolder);
                        Log.d(TAG, "onSuccess: "+orderHolder.getDeliveryBoyName());
                    }
                }
                dailyOrderAdepter= new DailyOrderAdepter(DailyOrderDelivery.this,list);
                recyclerView.setAdapter(dailyOrderAdepter);
                dailyOrderAdepter.notifyDataSetChanged();
            }
        });
    }
}