package com.appdroid.ssbt_delivery_boy.Activity.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.appdroid.ssbt_delivery_boy.R;
import com.appdroid.ssbt_delivery_boy.adapter.OrderAdapter;
import com.appdroid.ssbt_delivery_boy.holder.DeliveryBoyHolder;
import com.appdroid.ssbt_delivery_boy.holder.OrderHolder;

import java.util.ArrayList;
import java.util.List;

public class ViewOrders extends AppCompatActivity {

    RecyclerView recyclerView;
    OrderHolder orderHolder;
    List<DeliveryBoyHolder> list;

    OrderAdapter orderAdapter;
    DeliveryBoyHolder deliveryBoyHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        recyclerView = findViewById(R.id.ordersRecycerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        list = new ArrayList<>();
    }
}