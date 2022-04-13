package com.appdroid.ssbtproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdroid.ssbtproject.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OrderConfurm extends AppCompatActivity {

    Button backToHome;
    TextView orderNumber,date;
    ImageView img_close;
    String orderID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confurm);
        Intent intent = getIntent();

        orderID = intent.getStringExtra("orderID");

        backToHome = findViewById(R.id.backToHome);
        orderNumber = findViewById(R.id.orderNumber);
        img_close = findViewById(R.id.img_close);
        date = findViewById(R.id.date);

        orderNumber.setText(orderID);
        date.setText(getDateWithConvertion());

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private String getDateWithConvertion() {
        String pattern = "EEEE, MMMM d, yyyy 'at' h:m a";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();

        String todayAsString = df.format(today);

        return todayAsString;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent  = new Intent(OrderConfurm.this,Dashboard.class);
        startActivity(intent);
    }
}