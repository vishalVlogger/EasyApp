package com.appdroid.ssbtproject.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdroid.ssbtproject.Adapter.ReviewsAdepter;
import com.appdroid.ssbtproject.Holder.DeliveryBoyHolder;
import com.appdroid.ssbtproject.Holder.OrderHolder;
import com.appdroid.ssbtproject.Holder.ReviewHolder;
import com.appdroid.ssbtproject.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class DeliveryBoyProfile extends AppCompatActivity {

    RecyclerView recyclerView;
    OrderHolder orderHolder;
    DeliveryBoyHolder deliveryBoyHolder;
    List<ReviewHolder> list;
    ReviewsAdepter reviewsAdepter;

    RoundedImageView boyImage;
    TextView boyName, mobileNo;

    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_profile);

        orderHolder = (OrderHolder) getIntent().getSerializableExtra("all");
        deliveryBoyHolder = (DeliveryBoyHolder) getIntent().getSerializableExtra("boyDetail");

        recyclerView = findViewById(R.id.reviewsRecyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        imgBack = findViewById(R.id.img_back);
        boyImage = findViewById(R.id.boyImage);
        boyName = findViewById(R.id.boyName);
        mobileNo =findViewById(R.id.mobileNumber);

        boyName.setText(deliveryBoyHolder.getName());
        mobileNo.setText(deliveryBoyHolder.getMobileNo());

        Glide.with(this).load(deliveryBoyHolder.getUidPhoto()).into(boyImage);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        list = new ArrayList<>();
        getReviews();
    }

    private void getReviews() {
        Query firestore =  FirebaseFirestore.getInstance().collection("DeliveryBoy")
                .document(orderHolder.getDeliveryBoyDocId()).collection("Ratting");

        firestore.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot snapshot  : queryDocumentSnapshots.getDocuments()){
                       ReviewHolder reviewHolder = snapshot.toObject(ReviewHolder.class);
                       list.add(reviewHolder);
                    }
                     reviewsAdepter = new ReviewsAdepter(DeliveryBoyProfile.this,list);
                     recyclerView.setAdapter(reviewsAdepter);

            }
        });
    }
}