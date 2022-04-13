package com.appdroid.ssbt_delivery_boy.Activity.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appdroid.ssbt_delivery_boy.R;
import com.appdroid.ssbt_delivery_boy.adapter.OrderdProductsAdeoter;
import com.appdroid.ssbt_delivery_boy.holder.OrderHolder;
import com.appdroid.ssbt_delivery_boy.holder.OrderProductHolder;
import com.appdroid.ssbt_delivery_boy.holder.ProductHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class OrderDetaild extends AppCompatActivity {

    private static final String TAG = "AAAAAAAAAAAAA";
    TextView userName,userContactNumber,userAddress,totalBill;
    RecyclerView recyclerView;
    OrderdProductsAdeoter orderdProductsAdepter;
    Button deliveryCompleted,cancel;

    ImageView imgBack;
    Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detaild);

        OrderHolder orderHolder = (OrderHolder) getIntent().getSerializableExtra("all");

        progressDialog = new Dialog(OrderDetaild.this);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);

        userAddress = findViewById(R.id.userAddress);
        userContactNumber = findViewById(R.id.userContactNumber);
        userName = findViewById(R.id.userName);
        totalBill = findViewById(R.id.totalBill);

        imgBack = findViewById(R.id.img_back);

        deliveryCompleted = findViewById(R.id.deliveryCompleted);
        cancel = findViewById(R.id.cancel);

        recyclerView = findViewById(R.id.productsList);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        orderdProductsAdepter = new OrderdProductsAdeoter(orderHolder.getProductDetails(),this);
        recyclerView.setAdapter(orderdProductsAdepter);

        userName.setText(orderHolder.getUserInfo().getName());
        userAddress.setText(orderHolder.getUserInfo().getAddress());
        userContactNumber.setText(orderHolder.getUserInfo().getNo());
        totalBill.setText("Total Amount : "+orderHolder.getOrderTotal());

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        deliveryCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(OrderDetaild.this);
                dialog.setContentView(R.layout.delivery_confurmation);
                Button yesBTN = dialog.findViewById(R.id.yesBTN);
                Button noBTN = dialog.findViewById(R.id.noBTN);

                dialog.show();

                yesBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        updateStaturForDeliveryCompleted(orderHolder,dialog);

                        //updateOrderStatus(orderHolder,dialog);

                        //
                    }
                });

                noBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(OrderDetaild.this);
                dialog.setContentView(R.layout.order_cancel_confurmation);
                Button yesBTN = dialog.findViewById(R.id.yesBTN);
                Button noBTN = dialog.findViewById(R.id.noBTN);



                yesBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        Dialog reasonDialog = new Dialog(OrderDetaild.this);
                        reasonDialog.setContentView(R.layout.order_cancel_reason_dialog);
                        EditText reason = reasonDialog.findViewById(R.id.reason);
                        Button submitBTN = reasonDialog.findViewById(R.id.submitBTN);

                        reasonDialog.show();

                        submitBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!reason.getText().toString().isEmpty()){
                                        reasonDialog.dismiss();
                                        progressDialog.show();
                                        updateCanceletionReson(orderHolder,reason.getText().toString(),reasonDialog);
                                }else {
                                    Toast.makeText(OrderDetaild.this, "Enter Valid Reason...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



                    }
                });
                noBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });





                dialog.show();
            }
        });
    }

    private void updateStaturForDeliveryCompleted(OrderHolder orderHolder, Dialog dialog) {

        DocumentReference firestore = FirebaseFirestore.getInstance().collection("Orders")
                .document(orderHolder.getDocID());

        Log.d(TAG, "updateOrderStatus: "+orderHolder.getDocID());

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status","delivered");
        firestore.update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Intent intent = new Intent(OrderDetaild.this,DeliveryCompletedActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                   // updateProductsStock(orderHolder.getProductDetails());
                }
            }
        });


    }

    private void updateCanceletionReson(OrderHolder orderHolder, String reason, Dialog reasonDialog) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Orders")
                .document(orderHolder.getDocID());

        HashMap<String,Object> hashMap  = new HashMap<>();
        hashMap.put("orderCancelBy","delivery boy");
        hashMap.put("cancellationReason",reason);
        hashMap.put("status","cancelled");

        documentReference.update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        reasonDialog.dismiss();
                        updateProductsStock(orderHolder.getProductDetails());
                        Toast.makeText(OrderDetaild.this, "Order Cancel..", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    private void updateOrderStatus(OrderHolder orderHolder, Dialog dialog) {
        DocumentReference firestore = FirebaseFirestore.getInstance().collection("Orders")
                .document(orderHolder.getDocID());

        Log.d(TAG, "updateOrderStatus: "+orderHolder.getDocID());

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status","delivered");
        firestore.update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
             public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        dialog.dismiss();
                        updateProductsStock(orderHolder.getProductDetails());
                    }
            }
        });
    }

    private void updateProductsStock(List<OrderProductHolder> orderProductHolderList) {
       /* int i=0;
        for (OrderProductHolder orderProductHolder : orderProductHolderList){
            *//*Log.d(TAG, "onSuccess: final stock updated ..... test : "+lastPosstionTest+" size :"+size+" product Name : "+productHolder.getTitle());
            if (lastPosstionTest == (size-1)){
                Intent intent = new Intent(OrderDetaild.this,DeliveryCompletedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }*//*

           if (orderProductHolder.getProductPack().contains("gm")){

                int packValueInGram = Integer.parseInt((orderProductHolder.getProductPack().replaceAll("[^0-9]", "")));
                int finalValue = packValueInGram * (orderProductHolder.getProductQty());
                Log.d(TAG, "onBindViewHolder: "+finalValue +" product Name "+orderProductHolder.getProductTitle());

                updateProductStockValues(orderProductHolder,finalValue);

            }else if (orderProductHolder.getProductPack().contains("kg")){

                int packValueInKG = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                packValueInKG = (packValueInKG*1000);
                int finalValue = packValueInKG * (orderProductHolder.getProductQty());
                updateProductStockValues(orderProductHolder,finalValue);
                Log.d(TAG, "onBindViewHolder:kg "+finalValue+" product Name "+orderProductHolder.getProductTitle());

            }else if (orderProductHolder.getProductPack().contains("ml")){
                int packValueInMl = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                int finalValue = packValueInMl*(orderProductHolder.getProductQty());
                updateProductStockValues(orderProductHolder,finalValue);
            }else if (orderProductHolder.getProductPack().contains("liter")){
                int packValueInLiter = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                int packInML = packValueInLiter*1000;
                int finalValue = packInML*(orderProductHolder.getProductQty());
                updateProductStockValues(orderProductHolder,finalValue);
            }else if (orderProductHolder.getProductPack().contains("pcs")){
                int packValueInMl = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                packValueInMl = packValueInMl*(orderProductHolder.getProductQty());
                updateProductStockValues(orderProductHolder,packValueInMl);

            }else if (orderProductHolder.getProductPack().contains("dozen") && !orderProductHolder.getProductPack().contains("half-dozen")) {
                int packValueInDozen = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                int packValueInDozenToPices  = (packValueInDozen*12)*(orderProductHolder.getProductQty());
                Log.d(TAG, "onBindViewHolderdozen: " + packValueInDozenToPices);
                updateProductStockValues(orderProductHolder,packValueInDozenToPices);
            }else if (orderProductHolder.getProductPack().contains("half-dozen")) {
                int packValueInDozenToPices  = 6;
                packValueInDozenToPices = packValueInDozenToPices*(orderProductHolder.getProductQty());
                Log.d(TAG, "onBindViewHolderdozen: " + packValueInDozenToPices);
                updateProductStockValues(orderProductHolder,packValueInDozenToPices);
            }


        }  // pisc and dusen work remain
*/

        int i=0;
        for (OrderProductHolder orderProductHolder : orderProductHolderList){


            if(i++ == orderProductHolderList.size() - 1){
                // Last iteration
                if (orderProductHolder.getProductPack().contains("gm")){

                    int packValueInGram = Integer.parseInt((orderProductHolder.getProductPack().replaceAll("[^0-9]", "")));
                    int finalValue = packValueInGram * (orderProductHolder.getProductQty());
                    Log.d(TAG, "onBindViewHolder: "+finalValue +" product Name "+orderProductHolder.getProductTitle());

                    updateProductStockValues(orderProductHolder,finalValue,true);

                }else if (orderProductHolder.getProductPack().contains("kg")){

                    int packValueInKG = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                    packValueInKG = (packValueInKG*1000);
                    int finalValue = packValueInKG * (orderProductHolder.getProductQty());
                    updateProductStockValues(orderProductHolder,finalValue,true);
                    Log.d(TAG, "onBindViewHolder:kg "+finalValue+" product Name "+orderProductHolder.getProductTitle());

                }else if (orderProductHolder.getProductPack().contains("ml")){
                    int packValueInMl = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                    int finalValue = packValueInMl*(orderProductHolder.getProductQty());
                    updateProductStockValues(orderProductHolder,finalValue,true);
                }else if (orderProductHolder.getProductPack().contains("liter")){
                    int packValueInLiter = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                    int packInML = packValueInLiter*1000;
                    int finalValue = packInML*(orderProductHolder.getProductQty());
                    updateProductStockValues(orderProductHolder,finalValue,true);


                }else if (orderProductHolder.getProductPack().contains("dozen") && !orderProductHolder.getProductPack().contains("half-dozen")) {
                    int packValueInDozen = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                    int packValueInDozenToPices  = (packValueInDozen*12)*(orderProductHolder.getProductQty());
                    Log.d(TAG, "onBindViewHolderdozen: " + packValueInDozenToPices);
                    updateProductStockValues(orderProductHolder,packValueInDozenToPices,false);

                }else if (orderProductHolder.getProductPack().contains("half-dozen")) {
                    int packValueInDozenToPices  = 6;
                    packValueInDozenToPices = packValueInDozenToPices*(orderProductHolder.getProductQty());
                    Log.d(TAG, "onBindViewHolderdozen: " + packValueInDozenToPices);
                    updateProductStockValues(orderProductHolder,packValueInDozenToPices,true);

                }else if (orderProductHolder.getProductPack().contains("pcs")){
                    int packValueInMl = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                    packValueInMl = packValueInMl*(orderProductHolder.getProductQty());
                    updateProductStockValues(orderProductHolder,packValueInMl,true);
                }

            }else {
                if (orderProductHolder.getProductPack().contains("gm")){

                    int packValueInGram = Integer.parseInt((orderProductHolder.getProductPack().replaceAll("[^0-9]", "")));
                    int finalValue = packValueInGram * (orderProductHolder.getProductQty());
                    Log.d(TAG, "onBindViewHolder: "+finalValue +" product Name "+orderProductHolder.getProductTitle());

                    updateProductStockValues(orderProductHolder,finalValue,false);

                }else if (orderProductHolder.getProductPack().contains("kg")){

                    int packValueInKG = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                    packValueInKG = (packValueInKG*1000);
                    int finalValue = packValueInKG * (orderProductHolder.getProductQty());
                    updateProductStockValues(orderProductHolder,finalValue,false);
                    Log.d(TAG, "onBindViewHolder:kg "+finalValue+" product Name "+orderProductHolder.getProductTitle());

                }else if (orderProductHolder.getProductPack().contains("ml")){
                    int packValueInMl = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                    int finalValue = packValueInMl*(orderProductHolder.getProductQty());
                    updateProductStockValues(orderProductHolder,finalValue,false);
                }else if (orderProductHolder.getProductPack().contains("liter")){
                    int packValueInLiter = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                    int packInML = packValueInLiter*1000;
                    int finalValue = packInML*(orderProductHolder.getProductQty());
                    updateProductStockValues(orderProductHolder,finalValue,false);


                }else if (orderProductHolder.getProductPack().contains("dozen") && !orderProductHolder.getProductPack().contains("half-dozen")) {
                    int packValueInDozen = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                    int packValueInDozenToPices  = (packValueInDozen*12)*(orderProductHolder.getProductQty());
                    Log.d(TAG, "onBindViewHolderdozen: " + packValueInDozenToPices);
                    updateProductStockValues(orderProductHolder,packValueInDozenToPices,false);

                }else if (orderProductHolder.getProductPack().contains("half-dozen")) {
                    int packValueInDozenToPices  = 6;
                    packValueInDozenToPices = packValueInDozenToPices*(orderProductHolder.getProductQty());
                    Log.d(TAG, "onBindViewHolderdozen: " + packValueInDozenToPices);
                    updateProductStockValues(orderProductHolder,packValueInDozenToPices,false);

                }else if (orderProductHolder.getProductPack().contains("pcs")){
                    int packValueInMl = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                    packValueInMl = packValueInMl*(orderProductHolder.getProductQty());
                    updateProductStockValues(orderProductHolder,packValueInMl,false);
                }
            }

        }

    }

    private void updateProductStockValues(OrderProductHolder orderProductHolder, int finalValue,boolean isLastPosition) {

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Product")
                .document(orderProductHolder.getProductDocId());

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ProductHolder productHolder = documentSnapshot.toObject(ProductHolder.class);
                long updatedStock = (productHolder.getQuntity()+finalValue);
                Log.d(TAG, "product name  : "+productHolder.getTitle()+" product quntity : "+productHolder.getQuntity() +" final updated Stock "+updatedStock);

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("quntity",updatedStock);

                DocumentReference reference = FirebaseFirestore.getInstance().collection("Product")
                        .document(orderProductHolder.getProductDocId());

                reference.update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (isLastPosition){
                            Log.d(TAG, "product name  : "+productHolder.getTitle()+" product quntity : "+productHolder.getQuntity() +" final updated Stock "+updatedStock);
                            Intent intent = new Intent(OrderDetaild.this,DeliveryCompletedActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        }
                        Log.d(TAG, "onSuccess: final stock updated .....");
                    }
                });
            }
        });

    }
}