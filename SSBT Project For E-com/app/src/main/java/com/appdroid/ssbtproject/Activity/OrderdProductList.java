package com.appdroid.ssbtproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdroid.ssbtproject.Adapter.OrderdProductsAdeoter;
import com.appdroid.ssbtproject.Holder.DeliveryBoyHolder;
import com.appdroid.ssbtproject.Holder.OrderHolder;
import com.appdroid.ssbtproject.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class OrderdProductList extends AppCompatActivity {

    private static final String TAG = "InOrderProductList";
    RecyclerView recyclerView;
    OrderdProductsAdeoter productsAdeoter;
    OrderHolder orderHolder;
    TextView totalPrice;

    TextView orderCancelBy,cancelationReson;
    RelativeLayout cancelationLayout;
    RatingBar rattingBar;

    DeliveryBoyHolder deliveryBoyHolder;

    RoundedImageView boyImage;
    TextView boyName,mobileNumber,deliveryTime;
    LinearLayout boyLayout,rattingLayout;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderd_product_list);

        totalPrice  =  findViewById(R.id.totalPrice);
        deliveryTime = findViewById(R.id.deliveryTime);
        orderHolder = (OrderHolder) getIntent().getSerializableExtra("all");

        imgBack = findViewById(R.id.img_back);
        cancelationLayout = findViewById(R.id.cancelationLayout);
        orderCancelBy = findViewById(R.id.orderCancelBy);
        cancelationReson = findViewById(R.id.cancelationReson);

        recyclerView = findViewById(R.id.orderProductsList);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rattingLayout = findViewById(R.id.rattingLayout);
        rattingBar = findViewById(R.id.rattingBar);

        boyLayout = findViewById(R.id.boyLayout);
        boyImage = findViewById(R.id.img_product);
        boyName = findViewById(R.id.userName);
        mobileNumber = findViewById(R.id.userNumber);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        productsAdeoter = new OrderdProductsAdeoter(orderHolder.getProductDetails(),OrderdProductList.this);
        recyclerView.setAdapter(productsAdeoter);

        totalPrice.setText("Total Amount  : "+orderHolder.getOrderTotal());

        if (orderHolder.getStatus().equals("cancelled")){
            cancelationLayout.setVisibility(View.VISIBLE);
            rattingLayout.setVisibility(View.GONE);
            boyLayout.setVisibility(View.GONE);
            orderCancelBy.setText("Order cancel by : "+orderHolder.getOrderCancelBy());
            cancelationReson.setText(Html.fromHtml("Cancellation Reason: "+"<b>"+orderHolder.getCancellationReason()+"</b>"));
        }else if (orderHolder.getStatus().equals("delivered")){
           // rattingLayout.setVisibility(View.VISIBLE);
            cancelationLayout.setVisibility(View.GONE);
            boyLayout.setVisibility(View.VISIBLE);
            getDeliveryBoyInfo();
            checkIsRattingAllredyGiven();


            if (orderHolder.getDeliveryDate() != null){
                deliveryTime.setVisibility(View.VISIBLE);
                chekTimeForDelivery();
            }else {
                deliveryTime.setVisibility(View.GONE);
            }

        }else if(orderHolder.getStatus().equals("delivery")){
            rattingLayout.setVisibility(View.GONE);
            cancelationLayout.setVisibility(View.GONE);
            boyLayout.setVisibility(View.VISIBLE);

            getDeliveryBoyInfo();

        }else {
            rattingLayout.setVisibility(View.GONE);
            cancelationLayout.setVisibility(View.GONE);
            boyLayout.setVisibility(View.GONE);
        }

        boyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(OrderdProductList.this,DeliveryBoyProfile.class);
                intent.putExtra("all",orderHolder);
                intent.putExtra("boyDetail", (Serializable) deliveryBoyHolder);
                startActivity(intent);
            }
        });

        rattingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                Dialog dialog = new Dialog(OrderdProductList.this);
                dialog.setContentView(R.layout.ratting_dialog_item);

                EditText massage = dialog.findViewById(R.id.massage);
                Button submitBTN = dialog.findViewById(R.id.submit);

                submitBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!massage.getText().toString().isEmpty()){
                            updateReview(massage.getText().toString(),dialog);
                        }else {
                            Toast.makeText(OrderdProductList.this, "Please Enter Massage...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                dialog.show();
                Toast.makeText(OrderdProductList.this, ""+rating, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chekTimeForDelivery() {
        long postDate = orderHolder.getDeliveryDate().getTime();
        long todatTime = orderHolder.getOrderDate().getTime();

        long diff  = postDate - todatTime;

        int numOfday  = (int) (diff/(1000*60*60*24));
        int min = (int) (diff/(1000*60));
        int hours = (int) (diff/(1000*60*60));
        String s =  DateFormat.getDateTimeInstance().format(orderHolder.getDeliveryDate());



        if (min>=60){
            if (hours > 24){
                if (numOfday>2){
                    deliveryTime.setText(Html.fromHtml(s));
                }else {
                    deliveryTime.setText("Order delivered in "+numOfday+" days");
                }
            }else {
                deliveryTime.setText("Order delivered in "+hours+" hours");
            }

        }else if (min == 0){
            deliveryTime.setText("Order delivered in some seconds");
        }else if(min<60){
            if (min<10){
                deliveryTime.setText("Order delivered in "+min+" minutes");
            }else {
                deliveryTime.setText("Order delivered in "+min+" minutes");
            }


        }


    }
    public static Date getFormattedDate(String oldDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).parse(oldDate);


            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void getDeliveryBoyInfo() {
        DocumentReference firestore = FirebaseFirestore.getInstance().collection("DeliveryBoy")
                .document(orderHolder.getDeliveryBoyDocId());
        firestore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                deliveryBoyHolder =  documentSnapshot.toObject(DeliveryBoyHolder.class);
                Glide.with(OrderdProductList.this).load(deliveryBoyHolder.getUidPhoto()).into(boyImage);
                boyName.setText(deliveryBoyHolder.getName());
                mobileNumber.setText(deliveryBoyHolder.getMobileNo());
            }
        });
    }

    private void checkIsRattingAllredyGiven() {
        DocumentReference firestore = FirebaseFirestore.getInstance().collection("DeliveryBoy")
                .document(orderHolder.getDeliveryBoyDocId());

        firestore.collection("Ratting").document(orderHolder.getDocID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "onSuccess: ratting allredy give");
                        rattingLayout.setVisibility(View.GONE);
                    }else {
                        Log.d(TAG, "onSuccess: Not rated");
                        rattingLayout.setVisibility(View.VISIBLE);
                    }
            }
        });
    }

    private void updateReview(String review, Dialog dialog) {
        DocumentReference reference  =  FirebaseFirestore.getInstance().collection("DeliveryBoy")
                .document(orderHolder.getDeliveryBoyDocId());

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("review",review);
        hashMap.put("ratting",rattingBar.getRating());
        hashMap.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("date",new Timestamp(new Date()));

        Log.d(TAG, "updateReview: "+orderHolder.getDocID());
        reference.collection("Ratting").document(orderHolder.getDocID())
                .set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    rattingLayout.setVisibility(View.GONE);
                    Toast.makeText(OrderdProductList.this, "Thanks For Review...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}