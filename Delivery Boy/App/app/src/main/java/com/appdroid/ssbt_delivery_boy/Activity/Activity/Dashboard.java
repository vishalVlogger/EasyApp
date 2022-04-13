package com.appdroid.ssbt_delivery_boy.Activity.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.appdroid.ssbt_delivery_boy.R;

public class Dashboard extends AppCompatActivity {

    LinearLayout viewDelivarys,viewDailyDelaiverys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        viewDailyDelaiverys = findViewById(R.id.viewDailyDelaiverys);
        viewDelivarys = findViewById(R.id.viewDelivarys);

        viewDelivarys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,NormalDeliveryActivity.class));
            }
        });

        viewDailyDelaiverys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,DailyOrderDelivery.class));
            }
        });

    }
}